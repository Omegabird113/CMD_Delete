package mc.omegabird.cmd_delete.mixin;

import mc.omegabird.cmd_delete.client.cmdDeleteClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin overrides the keyPressed method in TextFieldWidget to add:
 * word/line modifier based deletion and arrow navigation.
 */
@Mixin(value = TextFieldWidget.class, priority = 2000)
public abstract class TextFieldWidgetMixin {
    @Shadow
    protected abstract void erase(int offset, boolean words);
    @Shadow
    public abstract void eraseCharactersTo(int position);
    @Shadow
    public abstract void setCursor(int cursor, boolean select);
    @Shadow
    public abstract String getText();
    @Shadow
    public abstract int getWordSkipPosition(int wordOffset);

    static {
        cmdDeleteClient.LOGGER.info("Registering TextFieldWidgetMixin");
    }
    
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        int key = input.key();
        var window = MinecraftClient.getInstance().getWindow();
        boolean shift = input.hasShift(); // gets is shift down

        // gets left/right word/line keys down
        boolean word = InputUtil.isKeyPressed(window, cmdDeleteClient.WORD_MODIFIER_KEY) || InputUtil.isKeyPressed(window, cmdDeleteClient.RIGHT_WORD_MODIFIER_KEY);
        boolean line = InputUtil.isKeyPressed(window, cmdDeleteClient.LINE_MODIFIER_KEY) || InputUtil.isKeyPressed(window, cmdDeleteClient.RIGHT_LINE_MODIFIER_KEY);

        // Logs debug info
        cmdDeleteClient.LOGGER.debug("keyPressed triggered, key = {}, shift = {}", key, shift);
        cmdDeleteClient.LOGGER.debug("keyPressed trigger continued, word = {}. line = {}", word, line);

        // delete handling
        if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
            // if no word or line, then let vanilla delete
            if (!word && !line) {
                cmdDeleteClient.LOGGER.debug("returned from deletion because word and line are false");
                return;
            }

            // If backspace delete before, if delete key, delete after
            int direction = (key == GLFW.GLFW_KEY_BACKSPACE) ? -1 : 1;

            if (line) {
                // Deletes begining/end based off direction. If backspace before, delete to 0, else delete after delete to end length.
                this.eraseCharactersTo(direction < 0 ? 0 : this.getText().length());
                cmdDeleteClient.LOGGER.debug("Deletion handled as LINE. Direction = {}", direction);
            } else {
                // This handles the word deletion
                this.erase(direction, true);
                cmdDeleteClient.LOGGER.debug("Deletion handled as WORD. Direction = {}", direction);
            }

            cir.setReturnValue(true); // stops vanilla sense in this case, I handled
            return;
        }

        // Handle arrow key navigation
        if (key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT) {
            if (!word && !line) {
                cmdDeleteClient.LOGGER.debug("returned from navigation because word and line are false");
                return; // sends to vanilla if no modifiers
            }

            int direction = (key == GLFW.GLFW_KEY_LEFT) ? -1 : 1; // left -1, right 1

            if (line) {
                // moves to begining/end based on direction (if -1 left, then move to first, else move to right based opn length) & passes shift
                this.setCursor(direction < 0 ? 0 : this.getText().length(), shift);
                cmdDeleteClient.LOGGER.debug("Navigation handled as LINE. Direction = {}", direction);
            } else {
                // moves to position based on word skip position passing direction and shift
                this.setCursor(this.getWordSkipPosition(direction), shift);
                cmdDeleteClient.LOGGER.debug("Navigation handled as WORD. Direction = {}", direction);
            }

            cir.setReturnValue(true); // stops vanilla sense I handled
        }
        // let vanilla handle other stuff
    }
}
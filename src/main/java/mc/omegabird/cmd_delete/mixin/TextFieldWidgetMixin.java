package mc.omegabird.cmd_delete.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import mc.omegabird.cmd_delete.client.cmdDeleteClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class TextFieldWidgetMixin extends AbstractWidget {
    @Environment(EnvType.CLIENT)
    public TextFieldWidgetMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Shadow
    protected abstract void deleteText(int offset, boolean words);
    @Shadow
    public abstract void deleteCharsToPos(int position);
    @Shadow
    public abstract void moveCursorTo(int cursor, boolean select);
    @Shadow
    public abstract String getValue();
    @Shadow
    public abstract int getWordPosition(int wordOffset);

    static {
        cmdDeleteClient.LOGGER.info("Registering TextFieldWidgetMixin");
    }
    
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        int key = input.key();
        var window = Minecraft.getInstance().getWindow();
        boolean shift = input.hasShiftDown(); // gets is shift down

        // gets left/right word/line keys down
        boolean word = InputConstants.isKeyDown(window, cmdDeleteClient.WORD_MODIFIER_KEY) || InputConstants.isKeyDown(window, cmdDeleteClient.RIGHT_WORD_MODIFIER_KEY);
        boolean line = InputConstants.isKeyDown(window, cmdDeleteClient.LINE_MODIFIER_KEY) || InputConstants.isKeyDown(window, cmdDeleteClient.RIGHT_LINE_MODIFIER_KEY);

        // delete handling
        if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
            // Logs debug info
            cmdDeleteClient.LOGGER.debug("keyPressed from Delete handle triggered, key = {}, shift = {}", key, shift);
            cmdDeleteClient.LOGGER.debug("keyPressed trigger continued, word = {}. line = {}", word, line);

            // if no word or line, then let vanilla delete
            if (!word && !line) {
                cmdDeleteClient.LOGGER.debug("returned from deletion because word and line are false");
                return;
            }

            // If backspace delete before, if delete key, delete after
            int direction = (key == GLFW.GLFW_KEY_BACKSPACE) ? -1 : 1;

            if (line) {
                // Deletes begining/end based off direction. If backspace before, delete to 0, else delete after delete to end length.
                this.deleteCharsToPos(direction < 0 ? 0 : this.getValue().length());
                cmdDeleteClient.LOGGER.debug("Deletion handled as LINE. Direction = {}", direction);
            } else {
                // This handles the word deletion
                this.deleteText(direction, true);
                cmdDeleteClient.LOGGER.debug("Deletion handled as WORD. Direction = {}", direction);
            }

            cir.setReturnValue(true); // stops vanilla sense in this case, I handled
            return;
        }

        // Handle arrow key navigation
        if (key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT) {
            // Logs debug info
            cmdDeleteClient.LOGGER.debug("keyPressed from navigation handle triggered, key = {}, shift = {}", key, shift);
            cmdDeleteClient.LOGGER.debug("keyPressed trigger continued, word = {}. line = {}", word, line);

            if (!word && !line) {
                cmdDeleteClient.LOGGER.debug("returned from navigation because word and line are false");
                return; // sends to vanilla if no modifiers
            }

            int direction = (key == GLFW.GLFW_KEY_LEFT) ? -1 : 1; // left -1, right 1

            if (line) {
                // moves to begining/end based on direction (if -1 left, then move to first, else move to right based opn length) & passes shift
                this.moveCursorTo(direction < 0 ? 0 : this.getValue().length(), shift);
                cmdDeleteClient.LOGGER.debug("Navigation handled as LINE. Direction = {}", direction);
            } else {
                // moves to position based on word skip position passing direction and shift
                this.moveCursorTo(this.getWordPosition(direction), shift);
                cmdDeleteClient.LOGGER.debug("Navigation handled as WORD. Direction = {}", direction);
            }

            cir.setReturnValue(true); // stops vanilla sense I handled
        }
        // let vanilla handle other stuff
    }
}
package mc.omegabird.cmd_delete.mixin;

import mc.omegabird.cmd_delete.client.cmd_delete_client;
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

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin {
    @Shadow
    protected abstract void erase(int offset, boolean words);
    @Shadow
    public abstract void moveCursor(int offset, boolean select);
    @Shadow
    public abstract void setCursor(int cursor, boolean select);
    @Shadow
    public abstract String getText();
    @Shadow
    protected abstract int getWordSkipPosition(int wordOffset, int cursorPosition, boolean skipOverSpaces);
    @Shadow
    protected abstract int getCursorPosWithOffset(int offset);

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;erase(IZ)V"
            ),
            cancellable = true
    )
    private void cmd_delete$overrideDelete(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        int key = input.key();
        boolean backspace = key == GLFW.GLFW_KEY_BACKSPACE;
        boolean delete    = key == GLFW.GLFW_KEY_DELETE;
        if (!backspace && !delete) {
            return;
        }

        var window = MinecraftClient.getInstance().getWindow();

        boolean wordPressed = InputUtil.isKeyPressed(window, cmd_delete_client.WORD_MODIFIER_KEY) || InputUtil.isKeyPressed(window, cmd_delete_client.RIGHT_WORD_MODIFIER_KEY);
        boolean linePressed = (cmd_delete_client.LINE_MODIFIER_KEY != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, cmd_delete_client.LINE_MODIFIER_KEY)) || (cmd_delete_client.RIGHT_LINE_MODIFIER_KEY != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, cmd_delete_client.RIGHT_LINE_MODIFIER_KEY));

        int direction = backspace ? -1 : 1;

        if (linePressed) {
            this.erase(direction > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE, false);
            cir.setReturnValue(true);
            return;
        }
        if (wordPressed) {
            this.erase(direction, true);
            cir.setReturnValue(true);
            return;
        }

        this.erase(direction, false);
        cir.setReturnValue(true);
    }

    @Inject(
            method = "keyPressed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cmd_delete$arrowNavigation(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        int key = input.key();

        if (key != GLFW.GLFW_KEY_LEFT && key != GLFW.GLFW_KEY_RIGHT) {
            return;
        }

        var window = MinecraftClient.getInstance().getWindow();
        boolean shift = InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
        boolean wordModifierPressed = InputUtil.isKeyPressed(window, cmd_delete_client.WORD_MODIFIER_KEY) || InputUtil.isKeyPressed(window, cmd_delete_client.RIGHT_WORD_MODIFIER_KEY) ;
        boolean lineModifierPressed = (cmd_delete_client.LINE_MODIFIER_KEY != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, cmd_delete_client.LINE_MODIFIER_KEY)) || (cmd_delete_client.RIGHT_LINE_MODIFIER_KEY != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, cmd_delete_client.RIGHT_LINE_MODIFIER_KEY));

        int direction = (key == GLFW.GLFW_KEY_LEFT) ? -1 : 1;

        if (lineModifierPressed) {
            int target = (direction < 0) ? 0 : this.getText().length();
            this.setCursor(target, shift);
            cir.setReturnValue(true);
            return;
        }
        if (wordModifierPressed) {
            this.setCursor(this.getWordSkipPosition(direction, this.getCursorPosWithOffset(0), true), shift);
            cir.setReturnValue(true);
            return;
        }

        this.moveCursor(direction, shift);
        cir.setReturnValue(true);
    }
}
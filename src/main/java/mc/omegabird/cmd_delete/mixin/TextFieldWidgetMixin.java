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
        int wordKey = cmd_delete_client.WORD_MODIFIER_KEY;
        int lineKey = cmd_delete_client.LINE_MODIFIER_KEY;

        boolean wordPressed = wordKey != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, wordKey);
        boolean linePressed = lineKey != GLFW.GLFW_KEY_UNKNOWN && InputUtil.isKeyPressed(window, lineKey);

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
}
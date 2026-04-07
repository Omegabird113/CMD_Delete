package io.github.omegabird113.cmddelete.mixin;

import io.github.omegabird113.cmddelete.client.KeyConstants;
import io.github.omegabird113.cmddelete.client.CmdDeleteClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class TextFieldWidgetMixin {
    @Shadow
    protected abstract void deleteText(int dir, boolean wholeWord);
    @Shadow
    public abstract void deleteCharsToPos(int pos);
    @Shadow
    public abstract void moveCursorTo(int dir, boolean extendSelection);
    @Shadow
    public abstract String getValue();
    @Shadow
    public abstract int getWordPosition(int dir);

    static {
        CmdDeleteClient.LOGGER.info("Registering TextFieldWidgetMixin");
    }
    
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        int key = event.key();
        var window = Minecraft.getInstance().getWindow();
        boolean shift = event.hasShiftDown();

        boolean word = KeyConstants.wordKeyDown(window);
        boolean line = KeyConstants.lineKeyDown(window);

        if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
            if (!word && !line)
                return;

            int direction = (key == GLFW.GLFW_KEY_BACKSPACE) ? -1 : 1;

            if (line) {
                this.deleteCharsToPos(direction < 0 ? 0 : this.getValue().length());
            } else {
                this.deleteText(direction, true);
            }

            cir.setReturnValue(true);
            return;
        }

        if (key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT) {
            if (!word && !line)
                return;

            int direction = (key == GLFW.GLFW_KEY_LEFT) ? -1 : 1; // left -1, right 1

            if (line) {
                this.moveCursorTo(direction < 0 ? 0 : this.getValue().length(), shift);
            } else {
                this.moveCursorTo(this.getWordPosition(direction), shift);
            }

            cir.setReturnValue(true);
        }
    }
}
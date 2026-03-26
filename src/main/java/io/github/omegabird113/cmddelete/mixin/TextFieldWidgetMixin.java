package io.github.omegabird113.cmddelete.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.omegabird113.cmddelete.client.cmdDeleteClient;
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
        boolean shift = input.hasShiftDown();

        boolean word = InputConstants.isKeyDown(window, cmdDeleteClient.WORD_MODIFIER_KEY) || InputConstants.isKeyDown(window, cmdDeleteClient.RIGHT_WORD_MODIFIER_KEY);
        boolean line = InputConstants.isKeyDown(window, cmdDeleteClient.LINE_MODIFIER_KEY) || InputConstants.isKeyDown(window, cmdDeleteClient.RIGHT_LINE_MODIFIER_KEY);

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
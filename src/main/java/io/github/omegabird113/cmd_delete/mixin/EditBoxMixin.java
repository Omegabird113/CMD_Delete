package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class EditBoxMixin {
    @Shadow
    private boolean shiftPressed;

    @Shadow
    public abstract void deleteWords(int i);

    @Shadow
    public abstract void moveCursorTo(int pos);

    @Shadow
    public abstract String getValue();

    @Shadow
    public abstract int getWordPosition(int dir);

    @Shadow
    public abstract int getCursorPosition();

    @Shadow
    public abstract void deleteChars(int dir);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    private int cursorPos;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings()
                .getAction(keyCode, Minecraft.getInstance().getWindow());
        int direction = ActionOffsetUtils.getOffset(action);
        switch (action) {
            case DEL_LINE_LEFT -> {
                int cursor = this.getCursorPosition();
                this.deleteChars(-cursor);
            }
            case DEL_LINE_RIGHT -> {
                int cursor = this.getCursorPosition();
                int end = this.getValue().length();
                this.deleteChars(end - cursor);
            }
            case DEL_WORD_LEFT, DEL_WORD_RIGHT -> this.deleteWords(direction);
            case NAV_LINE_LEFT, NAV_TEXT_START -> this.moveCursorTo(0);
            case NAV_LINE_RIGHT, NAV_TEXT_END -> this.moveCursorTo(this.getValue().length());
            case SEL_LINE_LEFT, SEL_TEXT_START -> {
                boolean old = this.shiftPressed;
                this.shiftPressed = true;
                this.moveCursorTo(0);
                this.shiftPressed = old;
            }
            case SEL_LINE_RIGHT, SEL_TEXT_END -> {
                boolean old = this.shiftPressed;
                this.shiftPressed = true;
                this.moveCursorTo(this.getValue().length());
                this.shiftPressed = old;
            }
            case NAV_WORD_LEFT, NAV_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(direction));
            case SEL_WORD_LEFT, SEL_WORD_RIGHT -> {
                boolean old = this.shiftPressed;
                this.shiftPressed = true;
                this.moveCursorTo(this.getWordPosition(direction));
                this.shiftPressed = old;
            }
            case OVR_NAV_CHAR_LEFT, OVR_NAV_CHAR_RIGHT -> this.moveCursorTo(this.cursorPos + direction);
            case OVR_SEL_CHAR_LEFT, OVR_SEL_CHAR_RIGHT -> {
                boolean old = this.shiftPressed;
                this.shiftPressed = true;
                this.moveCursorTo(this.cursorPos + direction);
                this.shiftPressed = old;
            }
            case OVR_DEL_CHAR_LEFT -> {
                if (this.isEditable())
                    this.deleteChars(-1);
            }
            case OVR_DEL_CHAR_RIGHT -> {
                if (this.isEditable())
                    this.deleteChars(1);
            }
            case SEL_TEXT_UP, SEL_TEXT_DOWN, OVR_NAV_TEXT_UP, OVR_NAV_TEXT_DOWN -> {
                return;
            }
            case NONE -> {
                if (!NavMappingsManager.getCurrentFeatureFlags().overrideVanillaNavigation() || keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)
                    return;
            }
        }

        cir.setReturnValue(true);
    }
}
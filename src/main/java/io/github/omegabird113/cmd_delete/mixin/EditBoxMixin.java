package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class EditBoxMixin {
    @Shadow
    public abstract void deleteWords(int i);

    @Shadow
    public abstract void deleteCharsToPos(int pos);

    @Shadow
    public abstract void moveCursorTo(int dir, boolean extendSelection);

    @Shadow
    public abstract String getValue();

    @Shadow
    public abstract int getWordPosition(int dir);

    @Shadow
    public abstract void moveCursor(int dir, boolean extendSelection);

    @Shadow
    public abstract void deleteChars(int dir);

    @Shadow
    protected abstract boolean isEditable();

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings()
                .getAction(keyCode, Minecraft.getInstance().getWindow());
        switch (action) {
            case DEL_LINE_LEFT -> this.deleteCharsToPos(0);
            case DEL_LINE_RIGHT -> this.deleteCharsToPos(this.getValue().length());
            case DEL_WORD_LEFT -> this.deleteText(-1, true);
            case DEL_WORD_RIGHT -> this.deleteText(1, true);
            case NAV_LINE_LEFT, NAV_TEXT_START -> this.moveCursorTo(0, false);
            case NAV_LINE_RIGHT, NAV_TEXT_END -> this.moveCursorTo(this.getValue().length(), false);
            case SEL_LINE_LEFT, SEL_TEXT_START -> this.moveCursorTo(0, true);
            case SEL_LINE_RIGHT, SEL_TEXT_END -> this.moveCursorTo(this.getValue().length(), true);
            case NAV_WORD_LEFT -> this.moveCursorTo(this.getWordPosition(-1), false);
            case NAV_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(1), false);
            case SEL_WORD_LEFT -> this.moveCursorTo(this.getWordPosition(-1), true);
            case SEL_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(1), true);
            case OVR_NAV_CHAR_LEFT -> this.moveCursor(-1, false);
            case OVR_NAV_CHAR_RIGHT -> this.moveCursor(1, false);
            case OVR_SEL_CHAR_LEFT -> this.moveCursor(-1, true);
            case OVR_SEL_CHAR_RIGHT -> this.moveCursor(1, true);
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
                if (!NavMappingsManager.getCurrentFeatureFlags().overrideVanillaNavigation() || event.isEscape() || event.key() == GLFW.GLFW_KEY_ENTER)
                    return;
            }
        }

        cir.setReturnValue(true);
    }
}

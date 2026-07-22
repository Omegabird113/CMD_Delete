package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.CrashUtil;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class EditBoxMixin {
    @Unique
    private static final Logger LOGGER = LoggingManager.getLogger(EditBoxMixin.class);

    static {
        LOGGER.debug("EditBoxMixin loaded");
    }

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

    @Shadow
    public abstract void moveCursor(int dir, boolean hasShiftDown);

    @Shadow
    public abstract void deleteChars(int dir);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    public abstract String getHighlighted();

    @Shadow
    public abstract void insertText(String input);

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        final NavAction action = CrashUtil.crashMinecraftOnFailure(() -> NavMappingsManager.getCurrentMappings().getAction(event, Minecraft.getInstance().getWindow()));

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
            case OVR_COPY -> Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            case OVR_CUT -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                if (this.isEditable())
                    this.deleteCharsToPos(this.getHighlighted().length());
            }
            case OVR_PASTE -> {
                if (this.isEditable())
                    this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            }
            case OVR_SELECT_ALL -> {
                this.moveCursorTo(0, false);
                this.moveCursorTo(this.getValue().length(), true);
            }
            case SEL_TEXT_UP, SEL_TEXT_DOWN, OVR_NAV_TEXT_UP, OVR_NAV_TEXT_DOWN -> {
                return;
            }
            case NONE -> {
                if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().overrideVanillaNavigation()) || event.isEscape() || event.key() == GLFW.GLFW_KEY_ENTER || event.key() == GLFW.GLFW_KEY_KP_ENTER)
                    return;
            }
        }

        cir.setReturnValue(true);
    }
}

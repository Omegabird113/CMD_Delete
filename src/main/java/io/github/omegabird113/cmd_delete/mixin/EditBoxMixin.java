package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
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
    public abstract void deleteChars(int i);

    @Shadow
    public abstract void moveCursorTo(int pos, boolean extendSelection);

    @Shadow
    public abstract String getValue();

    @Shadow
    public abstract int getWordPosition(int dir);

    @Shadow
    public abstract int getCursorPosition();

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings()
                .getAction(keyCode, Minecraft.getInstance().getWindow());
        int direction = NavActionManager.getDirection(action);

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
            case NAV_LINE_LEFT, NAV_TEXT_START -> this.moveCursorTo(0, false);
            case NAV_LINE_RIGHT, NAV_TEXT_END -> this.moveCursorTo(this.getValue().length(), false);
            case SEL_LINE_LEFT, SEL_TEXT_START -> this.moveCursorTo(0, true);
            case SEL_LINE_RIGHT, SEL_TEXT_END -> this.moveCursorTo(this.getValue().length(), true);
            case NAV_WORD_LEFT, NAV_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(direction), false);
            case SEL_WORD_LEFT, SEL_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(direction), true);
            default -> {
                return;
            }
        }

        cir.setReturnValue(true);
    }
}
package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionUtils;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EditBox.class, priority = 2000)
public abstract class EditBoxMixin {
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

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideDelete(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings().getAction(event, Minecraft.getInstance().getWindow());
        int direction = NavActionUtils.getOffset(action);

        switch (action) {
            case DEL_LINE_LEFT, DEL_LINE_RIGHT -> this.deleteCharsToPos(direction < 0 ? 0 : this.getValue().length());
            case DEL_WORD_LEFT, DEL_WORD_RIGHT -> this.deleteText(direction, true);
            case NAV_LINE_LEFT, NAV_LINE_RIGHT, NAV_TEXT_START, NAV_TEXT_END ->
                    this.moveCursorTo(direction < 0 ? 0 : this.getValue().length(), false);
            case SEL_LINE_LEFT, SEL_LINE_RIGHT, SEL_TEXT_START, SEL_TEXT_END ->
                    this.moveCursorTo(direction < 0 ? 0 : this.getValue().length(), true);
            case NAV_WORD_LEFT, NAV_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(direction), false);
            case SEL_WORD_LEFT, SEL_WORD_RIGHT -> this.moveCursorTo(this.getWordPosition(direction), true);
            default -> {
                return;
            }
        }

        cir.setReturnValue(true);
    }
}

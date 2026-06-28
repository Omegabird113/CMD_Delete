package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.input.KeyEvent;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MultilineTextField.class, priority = 2000)
public abstract class MultilineTextFieldMixin {
    @Unique
    private static final Logger LOGGER = LoggingManager.getLogger(MultilineTextFieldMixin.class);

    static {
        LOGGER.debug("MultilineTextFieldMixin loaded");
    }

    @Shadow
    private String value;

    @Shadow
    private int cursor;

    @Final
    @Shadow
    private List<?> displayLines;

    @Shadow
    public abstract void setSelecting(boolean selecting);

    @Shadow
    public abstract void insertText(String input);

    @Shadow
    public abstract void deleteText(int dir);

    @Shadow
    public abstract void seekCursor(Whence whence, int cursor);

    @Shadow
    public abstract void seekCursorLine(int lineOffset);

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideMultilineNavigation(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings().getAction(event, Minecraft.getInstance().getWindow());
        int direction = ActionOffsetUtils.getOffset(action);

        switch (action) {
            case DEL_LINE_LEFT, DEL_LINE_RIGHT -> {
                this.setSelecting(true);
                this.cmd_delete$moveToLineEdge(direction);
                this.insertText("");
                this.setSelecting(false);
            }
            case DEL_WORD_LEFT -> {
                int previousWord = this.cmd_delete$getPreviousWordStart();
                this.deleteText(previousWord - this.cursor);
            }
            case DEL_WORD_RIGHT -> {
                int nextWord = this.cmd_delete$getNextWordStart();
                this.deleteText(nextWord - this.cursor);
            }
            case NAV_LINE_LEFT, NAV_LINE_RIGHT -> {
                this.setSelecting(false);
                this.cmd_delete$moveToLineEdge(direction);
            }
            case SEL_LINE_LEFT, SEL_LINE_RIGHT -> {
                this.setSelecting(true);
                this.cmd_delete$moveToLineEdge(direction);
            }
            case NAV_WORD_LEFT -> {
                this.setSelecting(false);
                this.seekCursor(Whence.ABSOLUTE, this.cmd_delete$getPreviousWordStart());
            }
            case NAV_WORD_RIGHT -> {
                this.setSelecting(false);
                this.seekCursor(Whence.ABSOLUTE, this.cmd_delete$getNextWordStart());
            }
            case SEL_WORD_LEFT -> {
                this.setSelecting(true);
                this.seekCursor(Whence.ABSOLUTE, this.cmd_delete$getPreviousWordStart());
            }
            case SEL_WORD_RIGHT -> {
                this.setSelecting(true);
                this.seekCursor(Whence.ABSOLUTE, this.cmd_delete$getNextWordStart());
            }
            case NAV_TEXT_START -> {
                this.setSelecting(false);
                this.seekCursor(Whence.ABSOLUTE, 0);
            }
            case NAV_TEXT_END -> {
                this.setSelecting(false);
                this.seekCursor(Whence.END, 0);
            }
            case SEL_TEXT_START -> {
                this.setSelecting(true);
                this.seekCursor(Whence.ABSOLUTE, 0);
            }
            case SEL_TEXT_END -> {
                this.setSelecting(true);
                this.seekCursor(Whence.END, 0);
            }
            case SEL_TEXT_UP, SEL_TEXT_DOWN -> {
                this.setSelecting(true);
                this.seekCursorLine(direction);
            }
            case NONE -> {
                return;
            }
        }

        cir.setReturnValue(true);
    }

    @Unique
    private void cmd_delete$moveToLineEdge(int direction) {
        this.seekCursor(Whence.ABSOLUTE, direction < 0 ? this.cmd_delete$getLineStart() : this.cmd_delete$getLineEnd());
    }

    @Unique
    private int cmd_delete$getLineStart() {
        MultilineTextFieldStringViewAccessor lineView = this.cmd_delete$getCursorLineView();
        return lineView == null ? 0 : lineView.cmd_delete$getBeginIndex();
    }

    @Unique
    private int cmd_delete$getLineEnd() {
        MultilineTextFieldStringViewAccessor lineView = this.cmd_delete$getCursorLineView();
        return lineView == null ? this.value.length() : lineView.cmd_delete$getEndIndex();
    }

    @Unique
    private MultilineTextFieldStringViewAccessor cmd_delete$getCursorLineView() {
        for (Object lineView : this.displayLines) {
            MultilineTextFieldStringViewAccessor accessor = (MultilineTextFieldStringViewAccessor) lineView;
            if (this.cursor >= accessor.cmd_delete$getBeginIndex() && this.cursor <= accessor.cmd_delete$getEndIndex())
                return accessor;
        }
        return this.displayLines.isEmpty() ? null : (MultilineTextFieldStringViewAccessor) this.displayLines.getLast();
    }

    @Unique
    private int cmd_delete$getPreviousWordStart() {
        int pos = Math.clamp(this.cursor, 0, this.value.length());
        while (pos > 0 && Character.isWhitespace(this.value.charAt(pos - 1)))
            pos--;
        while (pos > 0 && !Character.isWhitespace(this.value.charAt(pos - 1)))
            pos--;
        return pos;
    }

    @Unique
    private int cmd_delete$getNextWordStart() {
        int pos = Math.clamp(this.cursor, 0, this.value.length());
        while (pos < this.value.length() && !Character.isWhitespace(this.value.charAt(pos)))
            pos++;
        while (pos < this.value.length() && Character.isWhitespace(this.value.charAt(pos)))
            pos++;
        return pos;
    }
}

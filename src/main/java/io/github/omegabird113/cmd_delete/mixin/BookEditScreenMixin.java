package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Unique
    private static final int CMD_DELETE_TEXT_WIDTH = 114;

    @Shadow
    private boolean isSigning;

    @Shadow
    private boolean isModified;

    @Shadow
    private int currentPage;

    @Final
    @Shadow
    private List<String> pages;

    @Final
    @Shadow
    private TextFieldHelper pageEdit;

    @Final
    @Shadow
    private TextFieldHelper titleEdit;

    @Shadow
    private void clearDisplayCache() {
    }

    @Shadow
    private void updateButtonVisibility() {
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$onKeyPressed(int keyCode, int scanCode, int modifiers,
                                         CallbackInfoReturnable<Boolean> cir) {

        NavAction action = NavMappingsManager.getCurrentMappings().getAction(keyCode, Minecraft.getInstance().getWindow());
        TextFieldHelper edit = this.isSigning ? this.titleEdit : this.pageEdit;
        int direction = NavActionManager.getDirection(action);
        boolean changedText = false;

        switch (action) {
            case DEL_LINE_LEFT, DEL_LINE_RIGHT -> {
                this.cmd_delete$deleteToLineEdge(edit, direction);
                changedText = true;
            }
            case DEL_WORD_LEFT -> {
                edit.removeWordsFromCursor(NavActionManager.DIRECTION_LEFT);
                changedText = true;
            }
            case DEL_WORD_RIGHT -> {
                edit.removeWordsFromCursor(NavActionManager.DIRECTION_RIGHT);
                changedText = true;
            }
            case NAV_LINE_LEFT, NAV_LINE_RIGHT -> this.cmd_delete$moveToLineEdge(edit, direction, false);
            case SEL_LINE_LEFT, SEL_LINE_RIGHT -> this.cmd_delete$moveToLineEdge(edit, direction, true);
            case NAV_WORD_LEFT -> edit.moveByWords(NavActionManager.DIRECTION_LEFT);
            case NAV_WORD_RIGHT -> edit.moveByWords(NavActionManager.DIRECTION_RIGHT);
            case SEL_WORD_LEFT -> edit.moveByWords(NavActionManager.DIRECTION_LEFT, true);
            case SEL_WORD_RIGHT -> edit.moveByWords(NavActionManager.DIRECTION_RIGHT, true);
            case SEL_TEXT_START, NAV_TEXT_START -> edit.setCursorToStart(action == NavAction.SEL_TEXT_START);
            case SEL_TEXT_END, NAV_TEXT_END -> edit.setCursorToEnd(action == NavAction.SEL_TEXT_END);
            default -> {
                return;
            }
        }

        this.cmd_delete$afterEditAction(changedText);
        cir.setReturnValue(true);
    }

    @Unique
    private void cmd_delete$moveToLineEdge(TextFieldHelper edit, int direction, boolean extendSelection) {
        if (this.isSigning) {
            if (direction == NavActionManager.DIRECTION_LEFT) {
                edit.setCursorToStart(extendSelection);
            } else {
                edit.setCursorToEnd(extendSelection);
            }
            return;
        }

        if (extendSelection) {
            int selectionStart = edit.getSelectionPos();
            this.cmd_delete$movePageEditToVisualLineEdge(direction, false);
            edit.setSelectionRange(edit.getCursorPos(), selectionStart);
        } else {
            this.cmd_delete$movePageEditToVisualLineEdge(direction, false);
        }
    }

    @Unique
    private void cmd_delete$deleteToLineEdge(TextFieldHelper edit, int direction) {
        int cursor = edit.getCursorPos();
        this.cmd_delete$moveToLineEdge(edit, direction, false);
        edit.setSelectionRange(edit.getCursorPos(), cursor);
        edit.insertText("");
    }

    @Unique
    private void cmd_delete$movePageEditToVisualLineEdge(int direction, boolean extendSelection) {
        int cursor = this.pageEdit.getCursorPos();
        int[] lineStarts = this.cmd_delete$getVisualLineStarts();
        String[] lineContents = this.cmd_delete$getVisualLineContents();
        int line = this.cmd_delete$findLineFromPos(lineStarts, cursor);
        int lineEdge = direction == NavActionManager.DIRECTION_LEFT
                ? lineStarts[line]
                : lineStarts[line] + lineContents[line].length();
        this.pageEdit.setCursorPos(lineEdge, extendSelection);
    }

    @Unique
    private int[] cmd_delete$getVisualLineStarts() {
        List<Integer> starts = new ArrayList<>();
        String text = this.cmd_delete$getCurrentPageText();
        Minecraft.getInstance().font.getSplitter().splitLines(text, CMD_DELETE_TEXT_WIDTH, Style.EMPTY, true,
                (style, start, end) -> starts.add(start));

        if (starts.isEmpty()) {
            return new int[]{0};
        }

        return starts.stream().mapToInt(Integer::intValue).toArray();
    }

    @Unique
    private String[] cmd_delete$getVisualLineContents() {
        List<String> contents = new ArrayList<>();
        String text = this.cmd_delete$getCurrentPageText();
        Minecraft.getInstance().font.getSplitter().splitLines(text, CMD_DELETE_TEXT_WIDTH, Style.EMPTY, true,
                (style, start, end) -> contents.add(this.cmd_delete$stripVisualLineEnd(text.substring(start, end))));

        if (contents.isEmpty()) {
            return new String[]{""};
        }

        return contents.toArray(new String[0]);
    }

    @Unique
    private String cmd_delete$getCurrentPageText() {
        if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
            return this.pages.get(this.currentPage);
        }

        return "";
    }

    @Unique
    private int cmd_delete$findLineFromPos(int[] lineStarts, int cursor) {
        int line = Arrays.binarySearch(lineStarts, cursor);
        if (line < 0) {
            line = -(line + 2);
        }

        return Math.max(0, Math.min(line, lineStarts.length - 1));
    }

    @Unique
    private String cmd_delete$stripVisualLineEnd(String text) {
        int end = text.length();
        while (end > 0) {
            char c = text.charAt(end - 1);
            if (c != ' ' && c != '\n') {
                break;
            }
            end--;
        }

        return text.substring(0, end);
    }

    @Unique
    private void cmd_delete$afterEditAction(boolean changedText) {
        if (this.isSigning) {
            if (changedText) {
                this.updateButtonVisibility();
                this.isModified = true;
            }
        } else {
            this.clearDisplayCache();
        }
    }
}

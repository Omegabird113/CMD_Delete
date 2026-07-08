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

    @Shadow
    private String title;

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
            case DEL_LINE_LEFT:
            case DEL_LINE_RIGHT: {
                this.cmd_delete$deleteToLineEdge(edit, direction);
                changedText = true;
                break;
            }
            case DEL_WORD_LEFT: {
                this.cmd_delete$removeWordsFromCursor(edit, NavActionManager.DIRECTION_LEFT);
                changedText = true;
                break;
            }
            case DEL_WORD_RIGHT: {
                this.cmd_delete$removeWordsFromCursor(edit, NavActionManager.DIRECTION_RIGHT);
                changedText = true;
                break;
            }
            case NAV_LINE_LEFT:
            case NAV_LINE_RIGHT: {
                this.cmd_delete$moveToLineEdge(edit, direction, false);
                break;
            }
            case SEL_LINE_LEFT:
            case SEL_LINE_RIGHT: {
                this.cmd_delete$moveToLineEdge(edit, direction, true);
                break;
            }
            case NAV_WORD_LEFT: {
                edit.moveByWords(NavActionManager.DIRECTION_LEFT, false);
                break;
            }
            case NAV_WORD_RIGHT: {
                edit.moveByWords(NavActionManager.DIRECTION_RIGHT, false);
                break;
            }
            case SEL_WORD_LEFT: {
                edit.moveByWords(NavActionManager.DIRECTION_LEFT, true);
                break;
            }
            case SEL_WORD_RIGHT: {
                edit.moveByWords(NavActionManager.DIRECTION_RIGHT, true);
                break;
            }
            case SEL_TEXT_START:
            case NAV_TEXT_START: {
                edit.setCursorPos(0, action == NavAction.SEL_TEXT_START);
                break;
            }
            case SEL_TEXT_END:
            case NAV_TEXT_END: {
                edit.setCursorPos(this.cmd_delete$getEditLength(), action == NavAction.SEL_TEXT_END);
                break;
            }
            default: {
                return;
            }
        }

        this.cmd_delete$afterEditAction(changedText);
        cir.setReturnValue(true);
    }

    @Unique
    private int cmd_delete$getEditLength() {
        return this.cmd_delete$getCurrentEditText().length();
    }

    @Unique
    private String cmd_delete$getCurrentEditText() {
        if (this.isSigning) {
            return this.title;
        }
        return this.cmd_delete$getCurrentPageText();
    }

    @Unique
    private void cmd_delete$moveToLineEdge(TextFieldHelper edit, int direction, boolean extendSelection) {
        if (this.isSigning) {
            if (direction == NavActionManager.DIRECTION_LEFT) {
                edit.setCursorPos(0, extendSelection);
            } else {
                edit.setCursorPos(this.cmd_delete$getEditLength(), extendSelection);
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
    private void cmd_delete$removeWordsFromCursor(TextFieldHelper edit, int direction) {
        int selectionStart = edit.getSelectionPos();
        int cursor = edit.getCursorPos();

        if (selectionStart != cursor) {
            edit.insertText("");
            return;
        }

        edit.moveByWords(direction, false);
        edit.setSelectionRange(edit.getCursorPos(), cursor);
        edit.insertText("");
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

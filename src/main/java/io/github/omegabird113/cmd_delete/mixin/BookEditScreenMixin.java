package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
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
    
    @Shadow
    private int cursorPos;

    @Shadow
    private int selectionPos;

    @Shadow
    private void deleteSelection() {}

    @Shadow
    private void updateButtonVisibility() {}

    @Shadow
    protected abstract int strIndexAtWidth(String string, int i);

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$onKeyPressed(int i, int j, int k,
                                         CallbackInfoReturnable<Boolean> cir) {

        NavAction action = NavMappingsManager.getCurrentMappings().getAction(i, Minecraft.getInstance().window);
        int direction = NavActionManager.getDirection(action);
        boolean changedText = false;

        switch (action) {
            case DEL_LINE_LEFT:
            case DEL_LINE_RIGHT: {
                this.cmd_delete$deleteToLineEdge(direction);
                changedText = true;
                break;
            }
            case DEL_WORD_LEFT: {
                this.cmd_delete$removeWordsFromCursor(NavActionManager.DIRECTION_LEFT);
                changedText = true;
                break;
            }
            case DEL_WORD_RIGHT: {
                this.cmd_delete$removeWordsFromCursor(NavActionManager.DIRECTION_RIGHT);
                changedText = true;
                break;
            }
            case NAV_LINE_LEFT:
            case NAV_LINE_RIGHT: {
                this.cmd_delete$moveToLineEdge(direction, false);
                break;
            }
            case SEL_LINE_LEFT:
            case SEL_LINE_RIGHT: {
                this.cmd_delete$moveToLineEdge(direction, true);
                break;
            }
            case NAV_WORD_LEFT: {
                cmd_delete$moveByWords(NavActionManager.DIRECTION_LEFT, false);
                break;
            }
            case NAV_WORD_RIGHT: {
                cmd_delete$moveByWords(NavActionManager.DIRECTION_RIGHT, false);
                break;
            }
            case SEL_WORD_LEFT: {
                cmd_delete$moveByWords(NavActionManager.DIRECTION_LEFT, true);
                break;
            }
            case SEL_WORD_RIGHT: {
                cmd_delete$moveByWords(NavActionManager.DIRECTION_RIGHT, true);
                break;
            }
            case SEL_TEXT_START:
            case NAV_TEXT_START: {
                cmd_delete$setCursorPos(0, action == NavAction.SEL_TEXT_START);
                break;
            }
            case SEL_TEXT_END:
            case NAV_TEXT_END: {
                cmd_delete$setCursorPos(this.cmd_delete$getEditLength(), action == NavAction.SEL_TEXT_END);
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
    private void cmd_delete$setCursorPos(int pos, boolean selecting) {
        if (!selecting)
            selectionPos = pos;
        cursorPos = pos;        
    }

    @Unique
    private void cmd_delete$moveByWords(int direction, boolean selecting) {
        String text = cmd_delete$getCurrentEditText();

        cursorPos = Minecraft.getInstance()
                .font
                .getWordPosition(text, direction, cursorPos, true);

        if (!selecting) {
            selectionPos = cursorPos;
        }
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
    private void cmd_delete$moveToLineEdge(int direction, boolean extendSelection) {
        if (this.isSigning) {
            if (direction == NavActionManager.DIRECTION_LEFT) {
                cmd_delete$setCursorPos(0, extendSelection);
            } else {
                cmd_delete$setCursorPos(this.cmd_delete$getEditLength(), extendSelection);
            }
            return;
        }

        this.cmd_delete$movePageEditToVisualLineEdge(direction, extendSelection);
    }

    @Unique
    private void cmd_delete$deleteToLineEdge(int direction) {
        int originalCursor = cursorPos;
        cmd_delete$moveToLineEdge(direction, false);
        selectionPos = originalCursor;
        deleteSelection();
    }

    @Unique
    private void cmd_delete$movePageEditToVisualLineEdge(int direction, boolean extendSelection) {
        int cursor = cursorPos;
        int[] lineStarts = this.cmd_delete$getVisualLineStarts();
        String[] lineContents = this.cmd_delete$getVisualLineContents();
        int line = this.cmd_delete$findLineFromPos(lineStarts, cursor);
        cursorPos = direction == NavActionManager.DIRECTION_LEFT
                ? lineStarts[line]
                : lineStarts[line] + lineContents[line].length();
        if (!extendSelection)
            selectionPos = cursorPos;    
    }

    @Unique
    private void cmd_delete$removeWordsFromCursor(int direction) {
        int selectionStart = selectionPos;
        int cursor = cursorPos;

        if (selectionStart != cursor) {
            deleteSelection();
            return;
        }

        cmd_delete$moveByWords(direction, false);
        selectionPos = cursor;
        deleteSelection();
    }

    @Unique
    private int[] cmd_delete$getVisualLineStarts() {
        List<Integer> starts = new ArrayList<>();

        String remaining = this.cmd_delete$getCurrentPageText();
        int index = 0;

        starts.add(0);

        while (!remaining.isEmpty()) {
            int end = this.strIndexAtWidth(remaining, CMD_DELETE_TEXT_WIDTH);

            if (end >= remaining.length()) {
                break;
            }

            String line = remaining.substring(0, end);

            char c = remaining.charAt(end);
            boolean consume = c == ' ' || c == '\n';

            index += line.length() + (consume ? 1 : 0);

            starts.add(index);

            remaining = remaining.substring(end + (consume ? 1 : 0));
        }

        return starts.stream().mapToInt(Integer::intValue).toArray();
    }

    @Unique
    private String[] cmd_delete$getVisualLineContents() {
        List<String> lines = new ArrayList<>();

        String remaining = this.cmd_delete$getCurrentPageText();

        while (!remaining.isEmpty()) {
            int end = this.strIndexAtWidth(remaining, CMD_DELETE_TEXT_WIDTH);

            if (end >= remaining.length()) {
                lines.add(cmd_delete$stripVisualLineEnd(remaining));
                break;
            }

            String line = remaining.substring(0, end);

            lines.add(cmd_delete$stripVisualLineEnd(line));

            char c = remaining.charAt(end);
            boolean consume = c == ' ' || c == '\n';

            remaining = remaining.substring(end + (consume ? 1 : 0));
        }

        if (lines.isEmpty()) {
            lines.add("");
        }

        return lines.toArray(new String[0]);
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
            if (changedText) {
                isModified = true;
            }
        }
    }
}

package io.github.omegabird113.cmddelete.mixin;

import io.github.omegabird113.cmddelete.client.KeyConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractSignEditScreen.class, priority = 2000)
public abstract class SignEditScreenMixin {
    @Shadow
    @Final
    protected SignBlockEntity sign;

    @Shadow
    private TextFieldHelper signField;

    @Shadow
    @Final
    private String[] messages;

    @Shadow
    private int line;

    @Unique
    private int cmd_delete$selectionStartLine = -1;

    @Unique
    private int cmd_delete$selectionEndLine = -1;

    @Unique
    private int cmd_delete$selectionStartPos;


    @Unique
    private int cmd_delete$selectionEndPos;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$overrideSignEditNavigation(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        int key = event.key();
        var window = Minecraft.getInstance().getWindow();

        boolean shift = event.hasShiftDown();
        boolean delete = KeyConstants.isDeleteKey(key);
        boolean move = KeyConstants.isMoveKey(key);
        boolean wordModifier = KeyConstants.wordKeyDown(window);
        boolean lineModifier = KeyConstants.lineKeyDown(window);
        int direction = KeyConstants.getDirection(key);

        // Reset selection if player moves w/o shift
        if (!shift && (event.isUp() || event.isDown() || move)) {
            this.cmd_delete$clearMultilineSelection();
        }

        // Keep old line selected if shift move up/down
        if (shift && (event.isUp() || event.isDown())) {
            this.cmd_delete$updateSelectionStart();
            this.line = (this.line + (event.isUp() ? -1 : 1)) & 3;
            this.signField.setCursorPos(Math.min(this.cmd_delete$selectionEndPos, this.cmd_delete$currentLineMessage().length()), false);
            this.cmd_delete$updateSelectionEnd();
            this.cmd_delete$syncCurrentLineSelection();
            cir.setReturnValue(true);
            return;
        }

        if (delete) {
            if (!wordModifier && !lineModifier) return;

            this.cmd_delete$clearMultilineSelection();

            if (lineModifier) {
                if (direction < 0) {
                    this.signField.setCursorToStart(true);
                } else {
                    this.signField.setCursorToEnd(true);
                }
                this.signField.insertText("");
            } else {
                this.signField.removeWordsFromCursor(direction);
            }

            cir.setReturnValue(true);
            return;
        }

        if (move) {
            if (!wordModifier && !lineModifier) return;

            if (shift) {
                this.cmd_delete$updateSelectionStart();
            } else {
                this.cmd_delete$clearMultilineSelection();
            }

            if (lineModifier) {
                if (direction < 0) {
                    this.signField.setCursorToStart(shift);
                } else {
                    this.signField.setCursorToEnd(shift);
                }
            } else {
                this.cmd_delete$moveByWords(direction, shift);
            }

            if (shift) {
                this.cmd_delete$updateSelectionEnd();
                this.cmd_delete$syncCurrentLineSelection();
            }

            cir.setReturnValue(true);
        }
    }

    // Resets local selection after typing because typing changes it
    @Inject(method = "charTyped", at = @At("HEAD"))
    private void cmd_delete$onCharTyped(CharacterEvent event, CallbackInfoReturnable<Boolean> cir) {
        this.cmd_delete$selectionStartLine = -1;
        this.cmd_delete$selectionEndLine = -1;
    }

    // Draw selected lines other than the cursor's line
    @Inject(method = "extractSignText", at = @At("TAIL"))
    private void cmd_delete$extractMultilineSelection(GuiGraphicsExtractor graphics, Vector2f cursorPosOutput, CallbackInfo ci) {
        if (this.cmd_delete$hasNoMultilineSelection()) {
            return;
        }

        int textLineHeight = this.sign.getTextLineHeight();
        int yOffset = this.messages.length * textLineHeight / 2;

        for (int workingLine = 0; workingLine < this.messages.length; workingLine++) {
            if (workingLine == this.line || !this.cmd_delete$lineHasSelection(workingLine)) {
                continue;
            }

            String message = this.messages[workingLine];
            int start = this.cmd_delete$getSelectionStart(workingLine);
            int end = this.cmd_delete$getSelectionEnd(workingLine);
            int x1 = this.cmd_delete$getTextAtX(message, start);
            int x2 = this.cmd_delete$getTextAtX(message, end);
            int y = workingLine * textLineHeight - yOffset;

            graphics.textHighlight(Math.min(x1, x2), y, Math.max(x1, x2), y + textLineHeight, true);
        }
    }

    @Unique
    private void cmd_delete$moveByWords(int direction, boolean extendSelection) {
        // At line edges, move to next line if needed
        int nextLine = this.cmd_delete$getNextWordLine(direction);

        if (direction < 0 && this.signField.getCursorPos() == 0 && nextLine != this.line) {
            this.line = nextLine;
            this.signField.setCursorToEnd(false);
        } else if (direction > 0 && this.signField.getCursorPos() == this.cmd_delete$currentLineMessage().length() && nextLine != this.line) {
            this.line = nextLine;
            this.signField.setCursorToStart(false);
        }

        this.signField.moveByWords(direction, extendSelection);
    }

    @Unique
    private int cmd_delete$getNextWordLine(int direction) {
        for (int nextLine = this.line + direction; nextLine >= 0 && nextLine < this.messages.length; nextLine += direction) {
            if (!this.messages[nextLine].isEmpty()) {
                return nextLine;
            }
        }

        return this.line;
    }

    @Unique
    private String cmd_delete$currentLineMessage() {
        return this.messages[this.line];
    }

    @Unique
    private void cmd_delete$updateSelectionStart() {
        if (this.cmd_delete$selectionStartLine == -1) {
            this.cmd_delete$selectionStartLine = this.line;
            this.cmd_delete$selectionStartPos = this.signField.getSelectionPos();
        }

        this.cmd_delete$selectionEndLine = this.line;
        this.cmd_delete$selectionEndPos = this.signField.getCursorPos();
    }

    @Unique
    private void cmd_delete$updateSelectionEnd() {
        this.cmd_delete$selectionEndLine = this.line;
        this.cmd_delete$selectionEndPos = this.signField.getCursorPos();
    }

    @Unique
    private void cmd_delete$clearMultilineSelection() {
        this.cmd_delete$selectionStartLine = -1;
        this.cmd_delete$selectionEndLine = -1;
    }

    @Unique
    private boolean cmd_delete$hasNoMultilineSelection() {
        return this.cmd_delete$selectionStartLine == -1
                || this.cmd_delete$selectionEndLine == -1
                || (this.cmd_delete$selectionStartLine == this.cmd_delete$selectionEndLine
                && this.cmd_delete$selectionStartPos == this.cmd_delete$selectionEndPos);
    }

    @Unique
    private void cmd_delete$syncCurrentLineSelection() {
        if (this.cmd_delete$hasNoMultilineSelection()) {
            return;
        }

        if (this.cmd_delete$lineHasSelection(this.line)) {
            this.signField.setSelectionRange(this.cmd_delete$selectionEndPos, this.cmd_delete$getCurrentLineSelectionOppositeEnd());
        }
    }

    @Unique
    private int cmd_delete$getCurrentLineSelectionOppositeEnd() {
        if (this.line == this.cmd_delete$selectionStartLine) {
            return this.cmd_delete$selectionStartPos;
        }

        return this.cmd_delete$selectionStartLine < this.cmd_delete$selectionEndLine ? 0 : this.cmd_delete$currentLineMessage().length();
    }

    @Unique
    private boolean cmd_delete$lineHasSelection(int workingLine) {
        return this.cmd_delete$getSelectionStart(workingLine) != this.cmd_delete$getSelectionEnd(workingLine);
    }

    @Unique
    private int cmd_delete$getSelectionStart(int workingLine) {
        if (this.cmd_delete$compareSelectionPoints() <= 0) {
            return this.cmd_delete$getSelectionStart(workingLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionStartPos, this.cmd_delete$selectionEndLine);
        }

        return this.cmd_delete$getSelectionStart(workingLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionEndPos, this.cmd_delete$selectionStartLine);
    }

    @Unique
    private int cmd_delete$getSelectionStart(int workingLine, int startLine, int startPos, int endLine) {
        if (workingLine < startLine || workingLine > endLine) {
            return 0;
        }

        return workingLine == startLine ? startPos : 0;
    }

    @Unique
    private int cmd_delete$getSelectionEnd(int workingLine) {
        if (this.cmd_delete$compareSelectionPoints() <= 0) {
            return this.cmd_delete$getSelectionEnd(workingLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionEndPos);
        }

        return this.cmd_delete$getSelectionEnd(workingLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionStartPos);
    }

    @Unique
    private int cmd_delete$getSelectionEnd(int workingLine, int startLine, int endLine, int endPos) {
        if (workingLine < startLine || workingLine > endLine) {
            return 0;
        }

        return workingLine == endLine ? endPos : this.messages[workingLine].length();
    }

    @Unique
    private int cmd_delete$compareSelectionPoints() {
        if (this.cmd_delete$selectionStartLine != this.cmd_delete$selectionEndLine) {
            return Integer.compare(this.cmd_delete$selectionStartLine, this.cmd_delete$selectionEndLine);
        }

        return Integer.compare(this.cmd_delete$selectionStartPos, this.cmd_delete$selectionEndPos);
    }

    @Unique
    private int cmd_delete$getTextAtX(String message, int position) {
        var font = Minecraft.getInstance().font;
        String shapedMessage = font.isBidirectional() ? font.bidirectionalShaping(message) : message;
        int clampedPosition = Math.min(position, shapedMessage.length());
        return font.width(shapedMessage.substring(0, clampedPosition)) - font.width(shapedMessage) / 2;
    }
}

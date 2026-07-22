package io.github.omegabird113.cmd_delete.mixin;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.actions.NavActionOffset;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
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
    @Unique
    private static final Logger LOGGER = LoggingManager.getLogger(SignEditScreenMixin.class);

    static {
        LOGGER.debug("SignEditScreenMixin loaded");
    }

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
        final NavAction action = NavMappingsManager.getCurrentMappings().getAction(event, Minecraft.getInstance().getWindow());
        final boolean shift = event.hasShiftDown();

        // Reset selection if player moves w/o shift
        if (!shift && (event.isUp() || event.isDown() || event.isLeft() || event.isRight() || action.isMove()))
            this.cmd_delete$clearMultilineSelection();

        if (action == NavAction.NONE && !shift && (event.isLeft() || event.isRight())) {
            // If line changed, we handled it, else continue
            final int sideDirection = event.isLeft() ? NavActionOffset.LEFT.value : NavActionOffset.RIGHT.value;
            if (this.cmd_delete$tryMoveToNextLineByCharacter(sideDirection)) {
                cir.setReturnValue(true);
                return;
            }
        }

        final int direction = NavActionOffset.get(action);

        switch (action) {
            case SEL_TEXT_UP, SEL_TEXT_DOWN -> {
                if (Boolean.TRUE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
                    this.cmd_delete$selectVertical(direction);
                else
                    return;
            }
            case DEL_LINE_LEFT, DEL_LINE_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$deleteToLineEdge(direction);
            }
            case DEL_WORD_LEFT, DEL_WORD_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$deleteByWords(direction);
            }
            case NAV_LINE_LEFT, NAV_LINE_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveToLineEdge(direction, false);
            }
            case SEL_LINE_LEFT, SEL_LINE_RIGHT -> this.cmd_delete$selectToLineEdge(direction);
            case NAV_WORD_LEFT, NAV_WORD_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveByWords(direction, false);
            }
            case SEL_WORD_LEFT, SEL_WORD_RIGHT -> this.cmd_delete$selectByWords(direction);
            case NAV_TEXT_START, NAV_TEXT_END -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveToTextEdge(direction, false);
            }
            case SEL_TEXT_START, SEL_TEXT_END -> this.cmd_delete$selectToTextEdge(direction);
            case OVR_NAV_CHAR_LEFT, OVR_NAV_CHAR_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveByChars(direction, false);
            }
            case OVR_SEL_CHAR_LEFT, OVR_SEL_CHAR_RIGHT -> this.cmd_delete$selectByChars(direction);
            case OVR_DEL_CHAR_LEFT, OVR_DEL_CHAR_RIGHT -> {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$deleteByChars(direction);
            }
            case OVR_NAV_TEXT_UP, OVR_NAV_TEXT_DOWN -> {
                this.cmd_delete$clearMultilineSelection();
                if (Boolean.TRUE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
                    this.line = (this.line + direction) & 3;
                else
                    this.line = Math.clamp(this.messages.length - 1, 0, this.line + direction);
                this.signField.setCursorToEnd(false);
            }
            case OVR_COPY -> this.signField.copy();
            case OVR_CUT -> this.signField.cut();
            case OVR_PASTE -> this.signField.paste();
            case OVR_SELECT_ALL -> this.signField.selectAll();
            case NONE -> {
                if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().overrideVanillaNavigation()) || event.isEscape() || event.key() == GLFW.GLFW_KEY_ENTER || event.key() == GLFW.GLFW_KEY_KP_ENTER)
                    return;
            }
        }

        cir.setReturnValue(true);
    }

    @Unique
    private void cmd_delete$deleteByChars(int direction) {
        this.cmd_delete$moveToNextCharacterLineIfNeeded(direction);
        this.signField.removeCharsFromCursor(direction);
    }

    @Unique
    private void cmd_delete$moveByChars(int direction, boolean extendSelection) {
        this.cmd_delete$moveToNextCharacterLineIfNeeded(direction);
        this.signField.moveByChars(direction, extendSelection);
    }

    @Unique
    private void cmd_delete$selectByChars(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.cmd_delete$moveByChars(direction, true);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private void cmd_delete$moveToNextCharacterLineIfNeeded(int direction) {
        if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
            return;
        if (direction == NavActionOffset.LEFT.value
                && this.signField.getCursorPos() == 0
                && this.line > 0) {
            this.line--;
            this.signField.setCursorToEnd(false);
        } else if (direction == NavActionOffset.RIGHT.value
                && this.signField.getCursorPos() == this.cmd_delete$currentLineMessage().length()
                && this.line < this.messages.length - 1) {
            this.line++;
            this.signField.setCursorToStart(false);
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
        if (this.cmd_delete$hasNoMultilineSelection())
            return;

        final int textLineHeight = this.sign.getTextLineHeight();
        final int yOffset = this.messages.length * textLineHeight / 2;

        for (int workingLine = 0; workingLine < this.messages.length; workingLine++) {
            if (workingLine == this.line || !this.cmd_delete$lineHasSelection(workingLine))
                continue;

            final String message = this.messages[workingLine];
            final int start = this.cmd_delete$getSelectionStart(workingLine);
            final int end = this.cmd_delete$getSelectionEnd(workingLine);
            final int x1 = this.cmd_delete$getTextAtX(message, start);
            final int x2 = this.cmd_delete$getTextAtX(message, end);
            final int y = workingLine * textLineHeight - yOffset;

            graphics.textHighlight(Math.min(x1, x2), y, Math.max(x1, x2), y + textLineHeight, true);
        }
    }

    @Unique
    private boolean cmd_delete$tryMoveToNextLineByCharacter(int direction) {
        if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
            return false;
        final int oldLine = this.line;
        this.cmd_delete$moveToNextCharacterLineIfNeeded(direction);
        return oldLine != this.line;
    }

    @Unique
    private void cmd_delete$deleteByWords(int direction) {
        this.cmd_delete$moveToNextWordLineIfNeeded(direction);
        this.signField.removeWordsFromCursor(direction);
    }

    @Unique
    private void cmd_delete$moveByWords(int direction, boolean extendSelection) {
        this.cmd_delete$moveToNextWordLineIfNeeded(direction);
        this.signField.moveByWords(direction, extendSelection);
    }

    @Unique
    private void cmd_delete$moveToNextWordLineIfNeeded(int direction) {
        if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
            return;
        final int nextLine = this.cmd_delete$getNextWordLine(direction);
        if (direction == NavActionOffset.LEFT.value && this.signField.getCursorPos() == 0 && nextLine != this.line) {
            this.line = nextLine;
            this.signField.setCursorToEnd(false);
        } else if (direction == NavActionOffset.RIGHT.value && this.signField.getCursorPos() == this.cmd_delete$currentLineMessage().length() && nextLine != this.line) {
            this.line = nextLine;
            this.signField.setCursorToStart(false);
        }
    }

    @Unique
    private void cmd_delete$deleteToLineEdge(int direction) {
        this.cmd_delete$moveToLineEdge(direction, true);
        this.signField.insertText("");
    }

    @Unique
    private void cmd_delete$moveToLineEdge(int direction, boolean extendSelection) {
        if (direction == NavActionOffset.LEFT.value)
            this.signField.setCursorToStart(extendSelection);
        else
            this.signField.setCursorToEnd(extendSelection);
    }

    @Unique
    private void cmd_delete$selectToLineEdge(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.cmd_delete$moveToLineEdge(direction, true);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private void cmd_delete$selectByWords(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.cmd_delete$moveByWords(direction, true);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private void cmd_delete$moveToTextEdge(int direction, boolean extendSelection) {
        if (direction == NavActionOffset.UP.value) {
            this.line = 0;
            this.signField.setCursorToStart(extendSelection);
        } else {
            this.line = this.messages.length - 1;
            this.signField.setCursorToEnd(extendSelection);
        }
    }

    @Unique
    private void cmd_delete$selectToTextEdge(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.cmd_delete$moveToTextEdge(direction, true);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private void cmd_delete$selectVertical(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.line = (this.line + direction) & 3;
        this.signField.setCursorPos(Math.min(this.cmd_delete$selectionEndPos, this.cmd_delete$currentLineMessage().length()), false);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private int cmd_delete$getNextWordLine(int direction) {
        if (Boolean.FALSE.equals(NavMappingsManager.getCurrentFeatureFlags().crossLineSignMovement()))
            return this.line;
        for (int nextLine = this.line + direction; nextLine >= 0 && nextLine < this.messages.length; nextLine += direction)
            if (!this.messages[nextLine].isEmpty())
                return nextLine;
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
        if (this.cmd_delete$hasNoMultilineSelection())
            return;
        if (this.cmd_delete$lineHasSelection(this.line))
            this.signField.setSelectionRange(this.cmd_delete$selectionEndPos, this.cmd_delete$getCurrentLineSelectionOppositeEnd());
    }

    @Unique
    private int cmd_delete$getCurrentLineSelectionOppositeEnd() {
        if (this.line == this.cmd_delete$selectionStartLine)
            return this.cmd_delete$selectionStartPos;
        return this.cmd_delete$selectionStartLine < this.cmd_delete$selectionEndLine ? 0 : this.cmd_delete$currentLineMessage().length();
    }

    @Unique
    private boolean cmd_delete$lineHasSelection(int workingLine) {
        return this.cmd_delete$getSelectionStart(workingLine) != this.cmd_delete$getSelectionEnd(workingLine);
    }

    @Unique
    private int cmd_delete$getSelectionStart(int workingLine) {
        if (this.cmd_delete$compareSelectionPoints() <= 0)
            return this.cmd_delete$getSelectionStart(workingLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionStartPos, this.cmd_delete$selectionEndLine);
        return this.cmd_delete$getSelectionStart(workingLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionEndPos, this.cmd_delete$selectionStartLine);
    }

    @Unique
    private int cmd_delete$getSelectionStart(int workingLine, int startLine, int startPos, int endLine) {
        if (workingLine < startLine || workingLine > endLine)
            return 0;
        return workingLine == startLine ? startPos : 0;
    }

    @Unique
    private int cmd_delete$getSelectionEnd(int workingLine) {
        if (this.cmd_delete$compareSelectionPoints() <= 0)
            return this.cmd_delete$getSelectionEnd(workingLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionEndPos);
        return this.cmd_delete$getSelectionEnd(workingLine, this.cmd_delete$selectionEndLine, this.cmd_delete$selectionStartLine, this.cmd_delete$selectionStartPos);
    }

    @Unique
    private int cmd_delete$getSelectionEnd(int workingLine, int startLine, int endLine, int endPos) {
        if (workingLine < startLine || workingLine > endLine)
            return 0;
        return workingLine == endLine ? endPos : this.messages[workingLine].length();
    }

    @Unique
    private int cmd_delete$compareSelectionPoints() {
        if (this.cmd_delete$selectionStartLine != this.cmd_delete$selectionEndLine)
            return Integer.compare(this.cmd_delete$selectionStartLine, this.cmd_delete$selectionEndLine);
        return Integer.compare(this.cmd_delete$selectionStartPos, this.cmd_delete$selectionEndPos);
    }

    @Unique
    private int cmd_delete$getTextAtX(String message, int position) {
        final Font font = Minecraft.getInstance().font;
        final String shapedMessage = font.isBidirectional() ? font.bidirectionalShaping(message) : message;
        final int clampedPosition = Math.min(position, shapedMessage.length());
        return font.width(shapedMessage.substring(0, clampedPosition)) - font.width(shapedMessage) / 2;
    }
}

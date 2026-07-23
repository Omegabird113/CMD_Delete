package io.github.omegabird113.cmd_delete.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SignEditScreen.class, priority = 2000)
public abstract class SignEditScreenMixin {
    @Shadow
    private TextFieldHelper signField;

    @Shadow
    @Final
    private SignBlockEntity sign;

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
    private void cmd_delete$overrideSignEditNavigation(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        NavAction action = NavMappingsManager.getCurrentMappings().getAction(i, Minecraft.getInstance().window);

        boolean shift = Screen.hasShiftDown();

        boolean left = i == GLFW.GLFW_KEY_LEFT;
        boolean right = i == GLFW.GLFW_KEY_RIGHT;
        boolean up = i == GLFW.GLFW_KEY_UP;
        boolean down = i == GLFW.GLFW_KEY_DOWN;

        // Reset selection if player moves w/o shift
        if (!shift && (up || down || left || right || NavActionManager.isMoveAction(action))) {
            this.cmd_delete$clearMultilineSelection();
        }

        if (action == NavAction.NONE && !shift && (left || right)) {
            int sideDirection = left ? NavActionManager.DIRECTION_LEFT : NavActionManager.DIRECTION_RIGHT;
            if (this.cmd_delete$tryMoveToNextLineByCharacter(sideDirection)) {
                cir.setReturnValue(true);
                return;
            }
        }

        int direction = NavActionManager.getDirection(action);

        switch (action) {
            case SEL_TEXT_UP:
            case SEL_TEXT_DOWN: {
                this.cmd_delete$selectVertical(direction);
                break;
            }
            case DEL_LINE_LEFT:
            case DEL_LINE_RIGHT: {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$deleteToLineEdge(direction);
                break;
            }
            case DEL_WORD_LEFT:
            case DEL_WORD_RIGHT: {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$deleteByWords(direction);
                break;
            }
            case NAV_LINE_LEFT:
            case NAV_LINE_RIGHT: {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveToLineEdge(direction, false);
                break;
            }
            case SEL_LINE_LEFT:
            case SEL_LINE_RIGHT: {
                this.cmd_delete$selectToLineEdge(direction);
                break;
            }
            case NAV_WORD_LEFT:
            case NAV_WORD_RIGHT: {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveByWords(direction, false);
                break;
            }
            case SEL_WORD_LEFT:
            case SEL_WORD_RIGHT: {
                this.cmd_delete$selectByWords(direction);
                break;
            }
            case NAV_TEXT_START:
            case NAV_TEXT_END: {
                this.cmd_delete$clearMultilineSelection();
                this.cmd_delete$moveToTextEdge(direction, false);
                break;
            }
            case SEL_TEXT_START:
            case SEL_TEXT_END: {
                    this.cmd_delete$selectToTextEdge(direction);
            }
            default: {
                return;
            }
        }

        cir.setReturnValue(true);
    }


    // Resets local selection after typing because typing changes it
    @Inject(method = "charTyped", at = @At("HEAD"))
    private void cmd_delete$onCharTyped(char c, int i, CallbackInfoReturnable<Boolean> cir) {
        this.cmd_delete$selectionStartLine = -1;
        this.cmd_delete$selectionEndLine = -1;
    }

    @Unique
    private void cmd_delete$setCursorPos(int pos, boolean selecting) {
        TextFieldHelperAccessor helper = (TextFieldHelperAccessor) this.signField;

        pos = Mth.clamp(pos, 0, this.cmd_delete$currentLineMessage().length());

        helper.cmd_delete$setCursorPosRaw(pos);

        if (!selecting) {
            helper.cmd_delete$setSelectionPosRaw(pos);
        }
    }


    // Draw selected lines other than the cursor's line
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;render(Lnet/minecraft/world/level/block/entity/BlockEntity;DDDF)V", shift = At.Shift.AFTER))
    private void cmd_delete$renderMultilineSelection(int i, int j, float f, CallbackInfo ci) {
        if (this.cmd_delete$hasNoMultilineSelection()) {
            return;
        }

        int textLineHeight = 10;
        int yOffset = this.sign.messages.length * textLineHeight / 2;

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        for (int workingLine = 0; workingLine < this.sign.messages.length; workingLine++) {
            if (workingLine == this.line || !this.cmd_delete$lineHasSelection(workingLine)) {
                continue;
            }

            String message = this.sign.messages[workingLine].toString();

            int start = this.cmd_delete$getSelectionStart(workingLine);
            int end = this.cmd_delete$getSelectionEnd(workingLine);

            int x1 = this.cmd_delete$getTextAtX(message, start);
            int x2 = this.cmd_delete$getTextAtX(message, end);

            int y = workingLine * textLineHeight - yOffset;

            Screen.fill(Math.min(x1, x2), y, Math.max(x1, x2), y + textLineHeight, -16776961);
        }

        GlStateManager.disableColorLogicOp();
    }

    @Unique
    private boolean cmd_delete$tryMoveToNextLineByCharacter(int direction) {
        if (direction == NavActionManager.DIRECTION_LEFT && this.signField.getCursorPos() == 0 && this.line > 0) {
            this.line--;
            cmd_delete$setCursorPos(this.cmd_delete$currentLineMessage().length(), false);
            return true;
        } else if (direction == NavActionManager.DIRECTION_RIGHT && this.signField.getCursorPos() == this.cmd_delete$currentLineMessage().length() && this.line < this.sign.messages.length - 1) {
            this.line++;
            cmd_delete$setCursorPos(0, false);
            return true;
        }
        return false;
    }

    @Unique
    private void cmd_delete$deleteByWords(int direction) {
        this.cmd_delete$moveToNextWordLineIfNeeded(direction);

        int start = this.signField.getCursorPos();

        cmd_delete$vanilliaMoveByWords(direction, false);

        int end = this.signField.getCursorPos();

        String text = this.cmd_delete$currentLineMessage();

        int from = Math.min(start, end);
        int to = Math.max(start, end);

        String newText =
                text.substring(0, from) + text.substring(to);

        this.sign.setMessage(this.line, new TextComponent(newText));
        cmd_delete$setCursorPos(from, false);
    }

    @Unique
    private void cmd_delete$moveByWords(int direction, boolean extendSelection) {
        this.cmd_delete$moveToNextWordLineIfNeeded(direction);
        cmd_delete$vanilliaMoveByWords(direction, extendSelection);
    }

    @Unique
    private void cmd_delete$moveToNextWordLineIfNeeded(int direction) {
        // At line edges, move to next line if needed
        int nextLine = this.cmd_delete$getNextWordLine(direction);

        if (direction == NavActionManager.DIRECTION_LEFT && this.signField.getCursorPos() == 0 && nextLine != this.line) {
            this.line = nextLine;
            cmd_delete$setCursorPos(this.cmd_delete$currentLineMessage().length(), false);
        } else if (direction == NavActionManager.DIRECTION_RIGHT && this.signField.getCursorPos() == this.cmd_delete$currentLineMessage().length() && nextLine != this.line) {
            this.line = nextLine;
            cmd_delete$setCursorPos(0, false);
        }
    }

    @Unique
    private void cmd_delete$deleteToLineEdge(int direction) {
        String text = this.cmd_delete$currentLineMessage();

        int cursor = this.signField.getCursorPos();

        String newText;

        if (direction == NavActionManager.DIRECTION_LEFT) {
            newText = text.substring(cursor);
            this.sign.setMessage(this.line, new TextComponent(newText));
            this.cmd_delete$setCursorPos(0, false);
        } else {
            newText = text.substring(0, cursor);
            this.sign.setMessage(this.line, new TextComponent(newText));
            this.cmd_delete$setCursorPos(newText.length(), false);
        }
    }


    @Unique
    private void cmd_delete$moveToLineEdge(int direction, boolean extendSelection) {
        if (direction == NavActionManager.DIRECTION_LEFT) {
            cmd_delete$setCursorPos(0, extendSelection);
        } else {
            cmd_delete$setCursorPos(this.cmd_delete$currentLineMessage().length(), extendSelection);
        }
    }

    @Unique
    private void cmd_delete$selectToLineEdge(int direction) {
        this.cmd_delete$updateSelectionStart();
        this.cmd_delete$moveToLineEdge(direction, true);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private void cmd_delete$vanilliaMoveByWords(int direction, boolean selecting) {
        this.cmd_delete$moveToNextWordLineIfNeeded(direction);

        String text = this.cmd_delete$currentLineMessage();

        TextFieldHelperAccessor helper = (TextFieldHelperAccessor) this.signField;

        int pos = Minecraft.getInstance().font.getWordPosition(
                text,
                direction,
                helper.cmd_delete$getCursorPosRaw(),
                true
        );

        helper.cmd_delete$setCursorPosRaw(pos);

        if (!selecting) {
            helper.cmd_delete$setSelectionPosRaw(pos);
        }
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
        if (direction == NavActionManager.DIRECTION_UP) {
            this.line = 0;
            cmd_delete$setCursorPos(0, extendSelection);
        } else {
            this.line = this.sign.messages.length - 1;
            cmd_delete$setCursorPos(this.cmd_delete$currentLineMessage().length(), extendSelection);
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
        cmd_delete$setCursorPos(Math.min(this.cmd_delete$selectionEndPos, this.cmd_delete$currentLineMessage().length()), false);
        this.cmd_delete$updateSelectionEnd();
        this.cmd_delete$syncCurrentLineSelection();
    }

    @Unique
    private int cmd_delete$getNextWordLine(int direction) {
        for (int nextLine = this.line + direction; nextLine >= 0 && nextLine < this.sign.messages.length; nextLine += direction) {
            if (!this.sign.getMessage(nextLine).getString().isEmpty()) {
                return nextLine;
            }
        }

        return this.line;
    }

    @Unique
    private String cmd_delete$currentLineMessage() {
        return this.sign.getMessage(this.line).getString();
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
            cmd_delete$setSelectionRange(this.cmd_delete$selectionEndPos, this.cmd_delete$getCurrentLineSelectionOppositeEnd());
        }
    }

    @Unique
    private void cmd_delete$setSelectionRange(int cursor, int selection) {
        TextFieldHelperAccessor helper = (TextFieldHelperAccessor) this.signField;

        helper.cmd_delete$setCursorPosRaw(cursor);
        helper.cmd_delete$setSelectionPosRaw(selection);
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

        return workingLine == endLine ? endPos : this.sign.getMessage(workingLine).getString().length();
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
        Font font = Minecraft.getInstance().font;
        String shapedMessage = font.isBidirectional() ? font.bidirectionalShaping(message) : message;
        int clampedPosition = Math.min(position, shapedMessage.length());
        return font.width(shapedMessage.substring(0, clampedPosition)) - font.width(shapedMessage) / 2;
    }
}

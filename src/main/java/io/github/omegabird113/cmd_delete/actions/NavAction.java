package io.github.omegabird113.cmd_delete.actions;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public enum NavAction {
    NAV_LINE_LEFT(NavActionOffset.LEFT, Type.MOVE, Scope.LINE, false),
    NAV_LINE_RIGHT(NavActionOffset.RIGHT, Type.MOVE, Scope.LINE, false),
    NAV_WORD_LEFT(NavActionOffset.LEFT, Type.MOVE, Scope.WORD, false),
    NAV_WORD_RIGHT(NavActionOffset.RIGHT, Type.MOVE, Scope.WORD, false),
    SEL_LINE_LEFT(NavActionOffset.LEFT, Type.SELECT, Scope.LINE, false),
    SEL_LINE_RIGHT(NavActionOffset.RIGHT, Type.SELECT, Scope.LINE, false),
    SEL_WORD_LEFT(NavActionOffset.LEFT, Type.SELECT, Scope.WORD, false),
    SEL_WORD_RIGHT(NavActionOffset.RIGHT, Type.SELECT, Scope.WORD, false),
    DEL_LINE_LEFT(NavActionOffset.LEFT, Type.DELETE, Scope.LINE, false),
    DEL_LINE_RIGHT(NavActionOffset.RIGHT, Type.DELETE, Scope.LINE, false),
    DEL_WORD_LEFT(NavActionOffset.LEFT, Type.DELETE, Scope.WORD, false),
    DEL_WORD_RIGHT(NavActionOffset.RIGHT, Type.DELETE, Scope.WORD, false),
    NAV_TEXT_START(NavActionOffset.UP, Type.MOVE, Scope.TEXT, false),
    NAV_TEXT_END(NavActionOffset.DOWN, Type.MOVE, Scope.TEXT, false),
    SEL_TEXT_START(NavActionOffset.UP, Type.SELECT, Scope.TEXT, false),
    SEL_TEXT_END(NavActionOffset.DOWN, Type.SELECT, Scope.TEXT, false),
    SEL_TEXT_UP(NavActionOffset.UP, Type.SELECT, Scope.LINE, false),
    SEL_TEXT_DOWN(NavActionOffset.DOWN, Type.SELECT, Scope.LINE, false),
    OVR_NAV_CHAR_LEFT(NavActionOffset.LEFT, Type.MOVE, Scope.CHAR, true),
    OVR_NAV_CHAR_RIGHT(NavActionOffset.RIGHT, Type.MOVE, Scope.CHAR, true),
    OVR_SEL_CHAR_LEFT(NavActionOffset.LEFT, Type.SELECT, Scope.CHAR, true),
    OVR_SEL_CHAR_RIGHT(NavActionOffset.RIGHT, Type.SELECT, Scope.CHAR, true),
    OVR_DEL_CHAR_LEFT(NavActionOffset.LEFT, Type.DELETE, Scope.CHAR, true),
    OVR_DEL_CHAR_RIGHT(NavActionOffset.RIGHT, Type.DELETE, Scope.CHAR, true),
    OVR_NAV_TEXT_UP(NavActionOffset.UP, Type.MOVE, Scope.LINE, true),
    OVR_NAV_TEXT_DOWN(NavActionOffset.DOWN, Type.MOVE, Scope.LINE, true),
    OVR_COPY(NavActionOffset.INVALID, Type.EDIT, Scope.TEXT, true),
    OVR_CUT(NavActionOffset.INVALID, Type.EDIT, Scope.TEXT, true),
    OVR_PASTE(NavActionOffset.INVALID, Type.EDIT, Scope.TEXT, true),
    OVR_SELECT_ALL(NavActionOffset.INVALID, Type.EDIT, Scope.TEXT, true),
    NONE(NavActionOffset.INVALID, Type.NONE, Scope.NONE, false);
    //NOTE: SEL_TEXT_UP, SEL_TEXT_DOWN, OVR_NAV_TEXT_UP, and OVR_NAV_TEXT_DOWN were misnamed at the time of their creation due to them working on lines, but compatibility doesn't allow for renaming until FV5.

    private final @NonNull NavActionOffset offset;
    private final @NonNull Type type;
    private final @NonNull Scope scope;
    private final boolean overrideMode;

    NavAction(@NonNull NavActionOffset offset, @NonNull Type type, @NonNull Scope scope, boolean overrideMode) {
        this.offset = offset;
        this.overrideMode = overrideMode;
        this.type = type;
        this.scope = scope;
    }

    @Contract(pure = true)
    public boolean isMove() {
        return this.type == Type.MOVE;
    }

    @Contract(pure = true)
    public boolean isSelect() {
        return this.type == Type.SELECT;
    }

    @Contract(pure = true)
    public boolean isDelete() {
        return this.type == Type.DELETE;
    }

    @Contract(pure = true)
    public boolean isEdit() {
        return this.type == Type.EDIT;
    }

    @Contract(pure = true)
    public boolean isOverrideEdit() {
        return this.overrideMode && this.isEdit();
    }

    @Contract(pure = true)
    public boolean isChar() {
        return this.scope == Scope.CHAR;
    }

    @Contract(pure = true)
    public boolean isWord() {
        return this.scope == Scope.WORD;
    }

    @Contract(pure = true)
    public boolean isLine() {
        return this.scope == Scope.LINE;
    }

    @Contract(pure = true)
    public boolean isText() {
        return this.scope == Scope.TEXT;
    }

    public @NonNull NavActionOffset offset() {
        return offset;
    }

    public @NonNull Type type() {
        return type;
    }

    public @NonNull Scope scope() {
        return scope;
    }

    public boolean overrideMode() {
        return overrideMode;
    }

    public enum Type {
        MOVE, SELECT, DELETE, EDIT, NONE
    }

    public enum Scope {
        NONE, CHAR, WORD, LINE, TEXT
    }
}

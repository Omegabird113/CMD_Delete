package io.github.omegabird113.cmd_delete.actions;

import io.github.omegabird113.cmd_delete.LoggingManager;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.Locale;

@SuppressWarnings("unused")
public enum NavAction {
    NAV_LINE_LEFT(NavActionOffset.LEFT, Type.MOVE, Scope.WITHIN_LINE, false),
    NAV_LINE_RIGHT(NavActionOffset.RIGHT, Type.MOVE, Scope.WITHIN_LINE, false),
    NAV_WORD_LEFT(NavActionOffset.LEFT, Type.MOVE, Scope.WORD, false),
    NAV_WORD_RIGHT(NavActionOffset.RIGHT, Type.MOVE, Scope.WORD, false),
    SEL_LINE_LEFT(NavActionOffset.LEFT, Type.SELECT, Scope.WITHIN_LINE, false),
    SEL_LINE_RIGHT(NavActionOffset.RIGHT, Type.SELECT, Scope.WITHIN_LINE, false),
    SEL_WORD_LEFT(NavActionOffset.LEFT, Type.SELECT, Scope.WORD, false),
    SEL_WORD_RIGHT(NavActionOffset.RIGHT, Type.SELECT, Scope.WORD, false),
    DEL_LINE_LEFT(NavActionOffset.LEFT, Type.DELETE, Scope.WITHIN_LINE, false),
    DEL_LINE_RIGHT(NavActionOffset.RIGHT, Type.DELETE, Scope.WITHIN_LINE, false),
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

    private static final Logger LOGGER = LoggingManager.getLogger(NavAction.class);

    static {
        LOGGER.debug("NavAction enum loaded. Detailed dump:\n{}", NavAction.getDetailedActionDump());
    }

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

    public static String getDetailedActionDump() {
        final NavAction[] actions = values();

        final String[][] table = new String[actions.length + 1][5];
        table[0] = new String[]{"Action", "Type", "Scope", "Offset", "Override"};

        for (int i = 0; i < actions.length; i++) {
            final String actionStr = actions[i].name();
            final String typeStr = actions[i].type().name();
            final String scopeStr = actions[i].scope().name();
            final String offsetStr = actions[i].offset().name();
            final String overrideStr = actions[i].overrideMode() ? "yes" : "no";

            final String[] entry = new String[]{actionStr, typeStr, scopeStr, offsetStr, overrideStr};
            table[i + 1] = entry;
        }

        StringBuilder dump = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            final String[] row = table[i];
            dump.append(
                    String.format(Locale.ROOT, "%-18s %-7s %-11s %-7s %-3s", row[0], row[1], row[2], row[3], row[4])
            );
            if (i != table.length - 1)
                dump.append("\n");
        }
        return dump.toString();
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
    public boolean isWithinLine() {
        return this.scope == Scope.WITHIN_LINE;
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
        NONE, CHAR, WORD, WITHIN_LINE, LINE, TEXT
    }
}

package io.github.omegabird113.cmd_delete.actions;

import io.github.omegabird113.cmd_delete.LoggingManager;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.Locale;

import static io.github.omegabird113.cmd_delete.actions.NavActionOffset.*;
import static io.github.omegabird113.cmd_delete.actions.NavActionScope.*;
import static io.github.omegabird113.cmd_delete.actions.NavActionType.*;

@SuppressWarnings("unused")
public enum NavAction {
    NAV_LINE_LEFT(LEFT, MOVE, WITHIN_LINE, false),
    NAV_LINE_RIGHT(RIGHT, MOVE, WITHIN_LINE, false),
    NAV_WORD_LEFT(LEFT, MOVE, WORD, false),
    NAV_WORD_RIGHT(RIGHT, MOVE, WORD, false),
    SEL_LINE_LEFT(LEFT, SELECT, WITHIN_LINE, false),
    SEL_LINE_RIGHT(RIGHT, SELECT, WITHIN_LINE, false),
    SEL_WORD_LEFT(LEFT, SELECT, WORD, false),
    SEL_WORD_RIGHT(RIGHT, SELECT, WORD, false),
    DEL_LINE_LEFT(LEFT, DELETE, WITHIN_LINE, false),
    DEL_LINE_RIGHT(RIGHT, DELETE, WITHIN_LINE, false),
    DEL_WORD_LEFT(LEFT, DELETE, WORD, false),
    DEL_WORD_RIGHT(RIGHT, DELETE, WORD, false),
    NAV_TEXT_START(UP, MOVE, TEXT, false),
    NAV_TEXT_END(DOWN, MOVE, TEXT, false),
    SEL_TEXT_START(UP, SELECT, TEXT, false),
    SEL_TEXT_END(DOWN, SELECT, TEXT, false),
    SEL_TEXT_UP(UP, SELECT, LINE, false),
    SEL_TEXT_DOWN(DOWN, SELECT, LINE, false),
    OVR_NAV_CHAR_LEFT(LEFT, MOVE, CHAR, true),
    OVR_NAV_CHAR_RIGHT(RIGHT, MOVE, CHAR, true),
    OVR_SEL_CHAR_LEFT(LEFT, SELECT, CHAR, true),
    OVR_SEL_CHAR_RIGHT(RIGHT, SELECT, CHAR, true),
    OVR_DEL_CHAR_LEFT(LEFT, DELETE, CHAR, true),
    OVR_DEL_CHAR_RIGHT(RIGHT, DELETE, CHAR, true),
    OVR_NAV_TEXT_UP(UP, MOVE, LINE, true),
    OVR_NAV_TEXT_DOWN(DOWN, MOVE, LINE, true),
    OVR_COPY(INVALID, EDIT, TEXT, true),
    OVR_CUT(INVALID, EDIT, TEXT, true),
    OVR_PASTE(INVALID, EDIT, TEXT, true),
    OVR_SELECT_ALL(INVALID, EDIT, TEXT, true),
    NONE(INVALID, NO_TYPE, NO_SCOPE, false);

    private static final Logger LOGGER = LoggingManager.getLogger(NavAction.class);

    static {
        LOGGER.debug("NavAction enum loaded. Detailed dump:\n{}", NavAction.getDetailedActionDump());
    }

    private final @NonNull NavActionOffset offset;
    private final @NonNull NavActionType type;
    private final @NonNull NavActionScope scope;
    private final boolean overrideMode;

    NavAction(@NonNull NavActionOffset offset, @NonNull NavActionType type, @NonNull NavActionScope scope, boolean overrideMode) {
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
        return this.type == MOVE;
    }

    @Contract(pure = true)
    public boolean isSelect() {
        return this.type == SELECT;
    }

    @Contract(pure = true)
    public boolean isDelete() {
        return this.type == DELETE;
    }

    @Contract(pure = true)
    public boolean isEdit() {
        return this.type == EDIT;
    }

    @Contract(pure = true)
    public boolean isOverrideEdit() {
        return this.overrideMode && this.isEdit();
    }

    @Contract(pure = true)
    public boolean isChar() {
        return this.scope == CHAR;
    }

    @Contract(pure = true)
    public boolean isWord() {
        return this.scope == WORD;
    }

    @Contract(pure = true)
    public boolean isWithinLine() {
        return this.scope == WITHIN_LINE;
    }

    @Contract(pure = true)
    public boolean isLine() {
        return this.scope == LINE;
    }

    @Contract(pure = true)
    public boolean isText() {
        return this.scope == TEXT;
    }

    public @NonNull NavActionOffset offset() {
        return offset;
    }

    public @NonNull NavActionType type() {
        return type;
    }

    public @NonNull NavActionScope scope() {
        return scope;
    }

    public boolean overrideMode() {
        return overrideMode;
    }
}

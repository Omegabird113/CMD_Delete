package io.github.omegabird113.cmd_delete.actions;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public final class ActionOffsetUtils {
    public static final int OFFSET_LEFT = -1;
    public static final int OFFSET_RIGHT = 1;
    public static final int OFFSET_DOWN = 1;
    public static final int OFFSET_UP = -1;

    private ActionOffsetUtils() {
    }

    @Contract(pure = true)
    public static int getOffset(@NonNull NavAction action) {
        return switch (action) {
            case NAV_LINE_LEFT, SEL_LINE_LEFT, DEL_LINE_LEFT, NAV_WORD_LEFT, SEL_WORD_LEFT, DEL_WORD_LEFT,
                 OVR_NAV_CHAR_LEFT, OVR_DEL_CHAR_LEFT, OVR_SEL_CHAR_LEFT -> OFFSET_LEFT;
            case NAV_LINE_RIGHT, SEL_LINE_RIGHT, DEL_LINE_RIGHT, NAV_WORD_RIGHT, SEL_WORD_RIGHT, DEL_WORD_RIGHT,
                 OVR_DEL_CHAR_RIGHT, OVR_NAV_CHAR_RIGHT, OVR_SEL_CHAR_RIGHT -> OFFSET_RIGHT;
            case NAV_TEXT_START, SEL_TEXT_START, SEL_TEXT_UP, OVR_NAV_TEXT_UP -> OFFSET_UP;
            case NAV_TEXT_END, SEL_TEXT_END, SEL_TEXT_DOWN, OVR_NAV_TEXT_DOWN -> OFFSET_DOWN;
            case NONE, OVR_COPY, OVR_CUT, OVR_PASTE, OVR_SELECT_ALL -> 0;
        };
    }

    @Contract(pure = true)
    public static boolean isMoveAction(@NonNull NavAction action) {
        return switch (action) {
            case NAV_LINE_LEFT, NAV_LINE_RIGHT,
                 NAV_WORD_LEFT, NAV_WORD_RIGHT,
                 NAV_TEXT_START, NAV_TEXT_END,
                 OVR_NAV_CHAR_LEFT, OVR_NAV_CHAR_RIGHT,
                 OVR_NAV_TEXT_UP, OVR_NAV_TEXT_DOWN -> true;
            default -> false;
        };
    }

    @Contract(pure = true)
    public static boolean isOverrideAction(@NonNull NavAction action) {
        return switch (action) {
            case OVR_NAV_CHAR_LEFT, OVR_DEL_CHAR_LEFT,
                 OVR_DEL_CHAR_RIGHT, OVR_NAV_CHAR_RIGHT,
                 OVR_NAV_TEXT_DOWN, OVR_NAV_TEXT_UP,
                 OVR_SEL_CHAR_LEFT, OVR_SEL_CHAR_RIGHT,
                 OVR_COPY, OVR_CUT, OVR_PASTE, OVR_SELECT_ALL -> true;
            default -> false;
        };
    }

    @Contract(pure = true)
    public static boolean isOverrideEditAction(@NonNull NavAction action) {
        return switch (action) {
            case OVR_COPY, OVR_CUT, OVR_PASTE, OVR_SELECT_ALL -> true;
            default -> false;
        };
    }
}

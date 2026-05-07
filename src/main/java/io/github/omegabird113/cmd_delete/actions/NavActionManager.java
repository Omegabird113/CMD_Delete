package io.github.omegabird113.cmd_delete.actions;

import io.github.omegabird113.cmd_delete.mappings.INavMappings;

import java.util.Arrays;

public class NavActionManager {
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_UP = -1;

    public static int getDirection(NavAction action) {
        return switch (action) {
            case NAV_LINE_LEFT, SEL_LINE_LEFT, DEL_LINE_LEFT, NAV_WORD_LEFT, SEL_WORD_LEFT, DEL_WORD_LEFT ->
                    DIRECTION_LEFT;
            case NAV_LINE_RIGHT, SEL_LINE_RIGHT, DEL_LINE_RIGHT, NAV_WORD_RIGHT, SEL_WORD_RIGHT, DEL_WORD_RIGHT ->
                    DIRECTION_RIGHT;
            case NAV_TEXT_START, SEL_TEXT_START, SEL_TEXT_UP -> DIRECTION_UP;
            case NAV_TEXT_END, SEL_TEXT_END, SEL_TEXT_DOWN -> DIRECTION_DOWN;
            default -> DIRECTION_RIGHT;
        };
    }

    public static boolean isMoveAction(NavAction action) {
        return switch (action) {
            case NAV_LINE_LEFT, NAV_LINE_RIGHT,
                 NAV_WORD_LEFT, NAV_WORD_RIGHT,
                 NAV_TEXT_START, NAV_TEXT_END -> true;
            default -> false;
        };
    }

    public static float getCoverage(INavMappings mapping) {
        int total = Arrays.stream(NavAction.values())
                .filter(action -> action != NavAction.NONE)
                .toArray(NavAction[]::new)
                .length;
        int support = mapping.getPossibleActions().length;
        return ((float) support) / total;
    }
}

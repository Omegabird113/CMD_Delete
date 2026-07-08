package io.github.omegabird113.cmd_delete.actions;

import io.github.omegabird113.cmd_delete.mappings.INavMappings;

import java.util.Arrays;

public class NavActionManager {
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_UP = -1;

    public static int getDirection(NavAction action) {
         switch (action) {
             case NAV_LINE_LEFT:
             case SEL_LINE_LEFT:
             case DEL_LINE_LEFT:
             case NAV_WORD_LEFT:
             case SEL_WORD_LEFT:
             case DEL_WORD_LEFT: {
                 return DIRECTION_LEFT;
             }
             case NAV_LINE_RIGHT:
             case SEL_LINE_RIGHT:
             case DEL_LINE_RIGHT:
             case NAV_WORD_RIGHT:
             case SEL_WORD_RIGHT:
             case DEL_WORD_RIGHT:
             case NONE: {
                 return DIRECTION_RIGHT;
             }
             case NAV_TEXT_START:
             case SEL_TEXT_START:
             case SEL_TEXT_UP: {
                 return DIRECTION_UP;
             }
             case NAV_TEXT_END:
             case SEL_TEXT_END:
             case SEL_TEXT_DOWN: {
                 return DIRECTION_DOWN;
             }
        }
        return 0;
    }

    public static boolean isMoveAction(NavAction action) {
         switch (action) {
             case NAV_LINE_LEFT:
             case NAV_LINE_RIGHT:
             case NAV_WORD_LEFT:
             case NAV_WORD_RIGHT:
             case NAV_TEXT_START:
             case NAV_TEXT_END: {
                 return true;
             }
             default: {
                 return false;
             }
         }
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

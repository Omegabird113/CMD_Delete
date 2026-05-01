package io.github.omegabird113.cmddelete.actions;

import io.github.omegabird113.cmddelete.actions.mappings.INavMapping;
import io.github.omegabird113.cmddelete.actions.mappings.LinuxNavMapping;
import io.github.omegabird113.cmddelete.actions.mappings.MacNavMapping;
import io.github.omegabird113.cmddelete.actions.mappings.WindowsNavMapping;

public class NavActionManager {
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_UP = -1;

    public static INavMapping getMapping() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return new MacNavMapping();
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return new WindowsNavMapping();
        } else {
            return new LinuxNavMapping();
        }
    }

    public static int getDirection(ActionConstant action) {
        return switch(action) {
            case NAV_LINE_LEFT, SEL_LINE_LEFT, DEL_LINE_LEFT, NAV_WORD_LEFT, SEL_WORD_LEFT, DEL_WORD_LEFT -> DIRECTION_LEFT;
            case NAV_LINE_RIGHT, SEL_LINE_RIGHT, DEL_LINE_RIGHT, NAV_WORD_RIGHT, SEL_WORD_RIGHT,  DEL_WORD_RIGHT -> DIRECTION_RIGHT;
            case NAV_TEXT_START, SEL_TEXT_START, SEL_TEXT_UP -> DIRECTION_UP;
            case NAV_TEXT_END, SEL_TEXT_END, SEL_TEXT_DOWN ->  DIRECTION_DOWN;
            default -> DIRECTION_RIGHT;
        };
    }


}

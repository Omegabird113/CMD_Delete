package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmd_delete.actions.NavAction.*;

class WindowsLinuxNavMappings implements INavMappings {
    private static final int LEFT = GLFW.GLFW_KEY_LEFT;
    private static final int RIGHT = GLFW.GLFW_KEY_RIGHT;
    private static final int HOME = GLFW.GLFW_KEY_HOME;
    private static final int END = GLFW.GLFW_KEY_END;
    private static final int BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    private static final int DELETE = GLFW.GLFW_KEY_DELETE;

    @Override
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        if (control && key == HOME)
            return shift ? SEL_TEXT_START : NAV_TEXT_START;
        if (control && key == END)
            return shift ? SEL_TEXT_END : NAV_TEXT_END;

        if (key == HOME)
            return shift ? SEL_LINE_LEFT : NAV_LINE_LEFT;
        if (key == END)
            return shift ? SEL_LINE_RIGHT : NAV_LINE_RIGHT;

        if (control && key == LEFT)
            return shift ? SEL_WORD_LEFT : NAV_WORD_LEFT;
        if (control && key == RIGHT)
            return shift ? SEL_WORD_RIGHT : NAV_WORD_RIGHT;

        if (control && key == BACKSPACE)
            return DEL_WORD_LEFT;
        if (control && key == DELETE)
            return DEL_WORD_RIGHT;

        if (shift && key == GLFW.GLFW_KEY_UP)
            return SEL_TEXT_UP;
        if (shift && key == GLFW.GLFW_KEY_DOWN)
            return SEL_TEXT_DOWN;

        return NONE;
    }

    @Override
    public NavAction[] getPossibleActions() {
        return new NavAction[] {
                NAV_LINE_LEFT, NAV_LINE_RIGHT,
                SEL_LINE_LEFT, SEL_LINE_RIGHT,
                DEL_WORD_LEFT, DEL_WORD_RIGHT,
                NAV_WORD_LEFT, NAV_WORD_RIGHT,
                SEL_WORD_LEFT, SEL_WORD_RIGHT,
                NAV_TEXT_START, NAV_TEXT_END,
                SEL_TEXT_START, SEL_TEXT_END,
                SEL_TEXT_UP, SEL_TEXT_DOWN
        };
    }

    @Override
    public Os[] getMappingsSupportedSystems() {
        return new Os[] {
            Os.WINDOWS, Os.LINUX
        };
    }
}

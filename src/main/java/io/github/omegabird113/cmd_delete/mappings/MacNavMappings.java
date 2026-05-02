package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmd_delete.actions.NavAction.*;

class MacNavMappings implements INavMappings {
    private static final int UP = GLFW.GLFW_KEY_UP;
    private static final int DOWN = GLFW.GLFW_KEY_DOWN;
    private static final int LEFT = GLFW.GLFW_KEY_LEFT;
    private static final int RIGHT = GLFW.GLFW_KEY_RIGHT;
    private static final int BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    private static final int DELETE = GLFW.GLFW_KEY_DELETE;

    @Override
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        if (superCommand && key == UP)
            return shift ? SEL_TEXT_START : NAV_TEXT_START;
        if (superCommand && key == DOWN)
            return shift ? SEL_TEXT_END : NAV_TEXT_END;

        if (superCommand && key == LEFT)
            return shift ? SEL_LINE_LEFT : NAV_LINE_LEFT;
        if (superCommand && key == RIGHT)
            return shift ? SEL_LINE_RIGHT : NAV_LINE_RIGHT;

        if (altOption && key == LEFT)
            return shift ? SEL_WORD_LEFT : NAV_WORD_LEFT;
        if (altOption && key == RIGHT)
            return shift ? SEL_WORD_RIGHT : NAV_WORD_RIGHT;

        if (superCommand && key == BACKSPACE)
            return DEL_LINE_LEFT;
        if (superCommand && key == DELETE)
            return DEL_LINE_RIGHT;

        if (altOption && key == BACKSPACE)
            return DEL_WORD_LEFT;
        if (altOption && key == DELETE)
            return DEL_WORD_RIGHT;

        if (shift && key == UP)
            return SEL_TEXT_UP;
        if (shift && key == DOWN)
            return SEL_TEXT_DOWN;

        return NONE;
    }

    @Override
    public NavAction[] getPossibleActions() {
        return new NavAction[] {
                NAV_LINE_LEFT, NAV_LINE_RIGHT,
                SEL_LINE_LEFT, SEL_LINE_RIGHT,
                DEL_LINE_LEFT, DEL_LINE_RIGHT,
                NAV_WORD_LEFT, NAV_WORD_RIGHT,
                SEL_WORD_LEFT, SEL_WORD_RIGHT,
                DEL_WORD_LEFT, DEL_WORD_RIGHT,
                NAV_TEXT_START, NAV_TEXT_END,
                SEL_TEXT_START, SEL_TEXT_END,
                SEL_TEXT_UP, SEL_TEXT_DOWN,
                NONE
        };
    }

    @Override
    public Os getMappingsOs() {
        return Os.MAC;
    }
}

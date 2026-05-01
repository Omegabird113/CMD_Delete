package io.github.omegabird113.cmddelete.actions.mappings;

import io.github.omegabird113.cmddelete.actions.ActionConstant;
import io.github.omegabird113.cmddelete.actions.OsConstant;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmddelete.actions.ActionConstant.*;

public class WindowsNavMapping implements INavMapping {
    private static final int LEFT = GLFW.GLFW_KEY_LEFT;
    private static final int RIGHT = GLFW.GLFW_KEY_RIGHT;
    private static final int HOME = GLFW.GLFW_KEY_HOME;
    private static final int END = GLFW.GLFW_KEY_END;
    private static final int BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    private static final int DELETE = GLFW.GLFW_KEY_DELETE;

    @Override
    public ActionConstant getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        // ctrl + home/end -> text start/end
        if (control && key == HOME)
            return shift ? SEL_TEXT_START : NAV_TEXT_START;
        if (control && key == END)
            return shift ? SEL_TEXT_END : NAV_TEXT_END;

        // home/end -> line start/end
        if (key == HOME)
            return shift ? SEL_LINE_LEFT : NAV_LINE_LEFT;
        if (key == END)
            return shift ? SEL_LINE_RIGHT : NAV_LINE_RIGHT;

        // ctrl + left/right -> word last/next
        if (control && key == LEFT)
            return shift ? SEL_WORD_LEFT : NAV_WORD_LEFT;
        if (control && key == RIGHT)
            return shift ? SEL_WORD_RIGHT : NAV_WORD_RIGHT;

        // ctrl + backspace/delete -> delete last/next word
        if (control && key == BACKSPACE)
            return DEL_WORD_LEFT;
        if (control && key == DELETE)
            return DEL_WORD_RIGHT;

        return NONE;
    }

    @Override
    public ActionConstant getAction(KeyEvent event) {
        int key = event.key();
        boolean shift   = event.hasShiftDown();
        boolean control = event.hasControlDown();
        return getAction(key, shift, false, control, false);
    }

    @Override
    public ActionConstant[] getPossibleActions() {
        return new ActionConstant[] {
                NAV_LINE_LEFT,  NAV_LINE_RIGHT,
                SEL_LINE_LEFT,  SEL_LINE_RIGHT,
                DEL_WORD_LEFT,  DEL_WORD_RIGHT,
                NAV_WORD_LEFT,  NAV_WORD_RIGHT,
                SEL_WORD_LEFT,  SEL_WORD_RIGHT,
                NAV_TEXT_START, NAV_TEXT_END,
                SEL_TEXT_START, SEL_TEXT_END
        };
    }

    @Override
    public OsConstant getMappingOs() {
        return OsConstant.WINDOWS;
    }
}


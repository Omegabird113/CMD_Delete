package io.github.omegabird113.cmddelete.actions.mapping;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.omegabird113.cmddelete.actions.ActionConstant;
import io.github.omegabird113.cmddelete.actions.OsConstant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmddelete.actions.ActionConstant.*;

public class MacNavMapping implements INavMapping {
    private static final int LEFT_COMMAND = GLFW.GLFW_KEY_LEFT_SUPER;
    private static final int RIGHT_COMMAND = GLFW.GLFW_KEY_RIGHT_SUPER;

    private static final int UP = GLFW.GLFW_KEY_UP;
    private static final int DOWN = GLFW.GLFW_KEY_DOWN;
    private static final int LEFT = GLFW.GLFW_KEY_LEFT;
    private static final int RIGHT = GLFW.GLFW_KEY_RIGHT;
    private static final int BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    private static final int DELETE = GLFW.GLFW_KEY_DELETE;

    @Override
    public ActionConstant getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        // cmd + up/down -> text start/end
        if (superCommand && key == UP)
            return shift ? SEL_TEXT_START : NAV_TEXT_START;
        if (superCommand && key == DOWN)
            return shift ? SEL_TEXT_END : NAV_TEXT_END;

        // cmd + left/right -> line start/end
        if (superCommand && key == LEFT)
            return shift ? SEL_LINE_LEFT : NAV_LINE_LEFT;
        if (superCommand && key == RIGHT)
            return shift ? SEL_LINE_RIGHT : NAV_LINE_RIGHT;

        // option + left/right -> word last/next
        if (altOption && key == LEFT)
            return shift ? SEL_WORD_LEFT : NAV_WORD_LEFT;
        if (altOption && key == RIGHT)
            return shift ? SEL_WORD_RIGHT : NAV_WORD_RIGHT;

        // cmd + backspcae/delete -> delete to start/end line
        if (superCommand && key == BACKSPACE)
            return DEL_LINE_LEFT;
        if (superCommand && key == DELETE)
            return DEL_LINE_RIGHT;

        // option + backspace/delete -> delete last/next word
        if (altOption && key == BACKSPACE)
            return DEL_WORD_LEFT;
        if (altOption && key == DELETE)
            return DEL_WORD_RIGHT;

        // shift + up/down -> select previous/next text line
        if (shift && key == UP)
            return SEL_TEXT_UP;
        if (shift && key == DOWN)
            return SEL_TEXT_DOWN;

        return NONE;
    }

    @Override
    public ActionConstant getAction(KeyEvent event) {
        var window = Minecraft.getInstance().getWindow();

        int key = event.key();
        boolean shift = event.hasShiftDown();
        boolean option = event.hasAltDown();
        boolean control = event.hasControlDown();
        boolean command = InputConstants.isKeyDown(window, LEFT_COMMAND) || InputConstants.isKeyDown(window, RIGHT_COMMAND);

        return getAction(key, shift, option, control, command);
    }

    @Override
    public ActionConstant[] getPossibleActions() {
        return new ActionConstant[] {
                NAV_LINE_LEFT, NAV_LINE_RIGHT,
                SEL_LINE_LEFT, SEL_LINE_RIGHT,
                DEL_LINE_LEFT, DEL_LINE_RIGHT,
                NAV_WORD_LEFT, NAV_WORD_RIGHT,
                SEL_WORD_LEFT, SEL_WORD_RIGHT,
                DEL_WORD_LEFT, DEL_WORD_RIGHT,
                NAV_TEXT_START, NAV_TEXT_END,
                SEL_TEXT_START, SEL_TEXT_END,
                SEL_TEXT_UP, SEL_TEXT_DOWN
        };
    }

    @Override
    public OsConstant getMappingOs() {
        return OsConstant.MAC;
    }
}

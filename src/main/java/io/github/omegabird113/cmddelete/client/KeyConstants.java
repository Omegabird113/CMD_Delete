package io.github.omegabird113.cmddelete.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;

public class KeyConstants {
    public static final boolean USING_MACOS = isUserOnMac();

    private static final int leftControl = GLFW.GLFW_KEY_LEFT_CONTROL;
    private static final int leftAltOption = GLFW.GLFW_KEY_LEFT_ALT;
    private static final int leftSuperCommand = GLFW.GLFW_KEY_LEFT_SUPER;
    private static final int rightControl = GLFW.GLFW_KEY_RIGHT_CONTROL;
    private static final int rightAltOption = GLFW.GLFW_KEY_RIGHT_ALT;
    private static final int rightSuperCommand = GLFW.GLFW_KEY_RIGHT_SUPER;

    public static final int LEFT_WORD_MODIFIER_KEY = USING_MACOS ? leftAltOption : leftControl;
    public static final int LEFT_LINE_MODIFIER_KEY = USING_MACOS ? leftSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
    public static final int RIGHT_WORD_MODIFIER_KEY = USING_MACOS ? rightAltOption : rightControl;
    public static final int RIGHT_LINE_MODIFIER_KEY = USING_MACOS ? rightSuperCommand : GLFW.GLFW_KEY_UNKNOWN;

    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_UP = -1;

    private static boolean isUserOnMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static boolean wordKeyDown(Window window) {
        return InputConstants.isKeyDown(window, KeyConstants.LEFT_WORD_MODIFIER_KEY) || InputConstants.isKeyDown(window, KeyConstants.RIGHT_WORD_MODIFIER_KEY);
    }

    public static boolean lineKeyDown(Window window) {
        return InputConstants.isKeyDown(window, KeyConstants.LEFT_LINE_MODIFIER_KEY) || InputConstants.isKeyDown(window, KeyConstants.RIGHT_LINE_MODIFIER_KEY);
    }

    public static int getDeleteDirection(int key) {
        return key == GLFW.GLFW_KEY_BACKSPACE ? DIRECTION_LEFT : DIRECTION_RIGHT; // backspace left, delete right
    }

    public static int getSideDirection(int key) {
        return key == GLFW.GLFW_KEY_LEFT ? DIRECTION_LEFT : DIRECTION_RIGHT;
    }

    public static int getVerticalDirection(int key) {
        return key == GLFW.GLFW_KEY_DOWN ? DIRECTION_DOWN : DIRECTION_UP;
    }

    public static boolean isDeleteKey(int key) {
        return key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE;
    }

    public static boolean isMoveKey(int key) {
        return key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT;
    }
}

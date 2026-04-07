package io.github.omegabird113.cmddelete.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmddelete.client.cmdDeleteClient.USING_MACOS;

public class KeyConstants {
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

    public static boolean wordKeyDown(Window window) {
        return  InputConstants.isKeyDown(window, KeyConstants.LEFT_WORD_MODIFIER_KEY) || InputConstants.isKeyDown(window, KeyConstants.RIGHT_WORD_MODIFIER_KEY);
    }

    public static boolean lineKeyDown(Window window) {
        return  InputConstants.isKeyDown(window, KeyConstants.LEFT_LINE_MODIFIER_KEY) || InputConstants.isKeyDown(window, KeyConstants.RIGHT_LINE_MODIFIER_KEY);
    }

}

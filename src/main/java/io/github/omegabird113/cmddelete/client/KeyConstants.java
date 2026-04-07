package io.github.omegabird113.cmddelete.client;

import org.lwjgl.glfw.GLFW;

import static io.github.omegabird113.cmddelete.client.cmdDeleteClient.macOS;

public class KeyConstants {
    private static final int leftControl = GLFW.GLFW_KEY_LEFT_CONTROL;
    private static final int leftAltOption = GLFW.GLFW_KEY_LEFT_ALT;
    private static final int leftSuperCommand = GLFW.GLFW_KEY_LEFT_SUPER;
    private static final int rightControl = GLFW.GLFW_KEY_RIGHT_CONTROL;
    private static final int rightAltOption = GLFW.GLFW_KEY_RIGHT_ALT;
    private static final int rightSuperCommand = GLFW.GLFW_KEY_RIGHT_SUPER;

    public static final int WORD_MODIFIER_KEY = macOS ? leftAltOption : leftControl;
    public static final int LINE_MODIFIER_KEY = macOS ? leftSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
    public static final int RIGHT_WORD_MODIFIER_KEY = macOS ? rightAltOption : rightControl;
    public static final int RIGHT_LINE_MODIFIER_KEY = macOS ? rightSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
}

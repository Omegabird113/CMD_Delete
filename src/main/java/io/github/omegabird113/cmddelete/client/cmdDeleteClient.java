package io.github.omegabird113.cmddelete.client;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class cmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd-delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    private static final int leftControl = GLFW.GLFW_KEY_LEFT_CONTROL;
    private static final int leftAltOption = GLFW.GLFW_KEY_LEFT_ALT;
    private static final int leftSuperCommand = GLFW.GLFW_KEY_LEFT_SUPER;
    private static final int rightControl = GLFW.GLFW_KEY_RIGHT_CONTROL;
    private static final int rightAltOption = GLFW.GLFW_KEY_RIGHT_ALT;
    private static final int rightSuperCommand = GLFW.GLFW_KEY_RIGHT_SUPER;

    public static final int WORD_MODIFIER_KEY = isUserOnMac() ? leftAltOption : leftControl;
    public static final int LINE_MODIFIER_KEY = isUserOnMac() ? leftSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
    public static final int RIGHT_WORD_MODIFIER_KEY = isUserOnMac() ? rightAltOption : rightControl;
    public static final int RIGHT_LINE_MODIFIER_KEY = isUserOnMac() ? rightSuperCommand : GLFW.GLFW_KEY_UNKNOWN;

    public static boolean isUserOnMac() {
        String os = System.getProperty("os.name").toLowerCase(); // gets os name
        return os.contains("mac");
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("CMD Delete loaded. User {} using macOS.", isUserOnMac() ? "is" : "is not");
    }
}

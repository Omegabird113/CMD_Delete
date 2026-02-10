package mc.omegabird.cmd_delete.client;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class cmdDeleteClient implements ClientModInitializer {
    public static final String MOD_ID = "cmd_delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Ints w/ all the Keys
    private static final int leftControl = GLFW.GLFW_KEY_LEFT_CONTROL;
    private static final int leftAltOption = GLFW.GLFW_KEY_LEFT_ALT;
    private static final int leftSuperCommand = GLFW.GLFW_KEY_LEFT_SUPER;
    private static final int rightControl = GLFW.GLFW_KEY_RIGHT_CONTROL;
    private static final int rightAltOption = GLFW.GLFW_KEY_RIGHT_ALT;
    private static final int rightSuperCommand = GLFW.GLFW_KEY_RIGHT_SUPER;

    // Method to check if user's on macOS
    public static boolean ifUserOnMac() {
        String os = System.getProperty("os.name").toLowerCase(); // gets os name
        return os.contains("mac"); // returns if name has 'mac' in it
    }

    // Other word = control, macOS = option. other line = unknown, macOS = command. Sets left/right appropriately
    public static final int WORD_MODIFIER_KEY = ifUserOnMac() ? leftAltOption : leftControl;
    public static final int LINE_MODIFIER_KEY = ifUserOnMac() ? leftSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
    public static final int RIGHT_WORD_MODIFIER_KEY = ifUserOnMac() ? rightAltOption : rightControl;
    public static final int RIGHT_LINE_MODIFIER_KEY = ifUserOnMac() ? rightSuperCommand : GLFW.GLFW_KEY_UNKNOWN;

    @Override
    public void onInitializeClient() {
        LOGGER.info("CMD Delete loaded. User {} using macOS.", ifUserOnMac() ? "is" : "is not");
    }
}

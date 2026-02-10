package mc.omegabird.cmd_delete.client;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.glfw.GLFW;

public class cmdDeleteClient implements ClientModInitializer {
    // Ints w/ all the Keys
    static int leftControl = GLFW.GLFW_KEY_LEFT_CONTROL;
    static int leftAltOption = GLFW.GLFW_KEY_LEFT_ALT;
    static int leftSuperCommand = GLFW.GLFW_KEY_LEFT_SUPER;
    static int rightControl = GLFW.GLFW_KEY_RIGHT_CONTROL;
    static int rightAltOption = GLFW.GLFW_KEY_RIGHT_ALT;
    static int rightSuperCommand = GLFW.GLFW_KEY_RIGHT_SUPER;

    // Method to check if user's on macOS
    public static boolean ifUserOnMac() {
        String os = System.getProperty("os.name").toLowerCase(); // gets os name
        return os.contains("mac"); // returns if name has 'mac' in it
    }

    // Other word = control, macOS = option. other line = unknown, macOS = command. Sets left/right appropriately
    public static int WORD_MODIFIER_KEY = ifUserOnMac() ? leftAltOption : leftControl;
    public static int LINE_MODIFIER_KEY = ifUserOnMac() ? leftSuperCommand : GLFW.GLFW_KEY_UNKNOWN;
    public static int RIGHT_WORD_MODIFIER_KEY = ifUserOnMac() ? rightAltOption : rightControl;
    public static int RIGHT_LINE_MODIFIER_KEY = ifUserOnMac() ? rightSuperCommand : GLFW.GLFW_KEY_UNKNOWN;

    @Override
    public void onInitializeClient() {

    }
}

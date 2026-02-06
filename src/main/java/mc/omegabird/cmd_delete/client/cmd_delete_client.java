package mc.omegabird.cmd_delete.client;

import net.fabricmc.api.ClientModInitializer;

import org.lwjgl.glfw.GLFW;

public class cmd_delete_client implements ClientModInitializer {
    static int control = GLFW.GLFW_KEY_LEFT_CONTROL;
    static int altOption = GLFW.GLFW_KEY_LEFT_ALT;
    static int WindowsCommand = GLFW.GLFW_KEY_LEFT_SUPER;

    public static boolean ifUserOnMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static int WORD_MODIFIER_KEY = ifUserOnMac() ? altOption : control;
    public static int LINE_MODIFIER_KEY = ifUserOnMac() ? WindowsCommand : GLFW.GLFW_KEY_UNKNOWN;

    @Override
    public void onInitializeClient() {

    }
}

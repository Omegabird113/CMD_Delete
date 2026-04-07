package io.github.omegabird113.cmddelete.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class cmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd-delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static boolean isUserOnMac() {
        String os = System.getProperty("os.name").toLowerCase(); // gets os name
        return os.contains("mac");
    }

    public static final boolean USING_MACOS = isUserOnMac();

    @Override
    public void onInitializeClient() {
        LOGGER.info("CMD Delete loaded. User {} using USING_MACOS.", isUserOnMac() ? "is" : "is not");
    }
}

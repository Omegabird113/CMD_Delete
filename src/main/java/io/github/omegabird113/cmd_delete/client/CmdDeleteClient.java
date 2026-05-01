package io.github.omegabird113.cmd_delete.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("CMD Delete is loading. User {} using macOS.", KeyConstants.USING_MACOS ? "is" : "is not");
    }
}

package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.commands.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    public static final String VERSION = "1.0.0-beta4+mc26.1.x";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeClient() {
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();
    }
}

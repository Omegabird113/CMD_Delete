package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    public static final Logger LOGGER = LogManager.getLogger(CmdDeleteClient.class);
    public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Initializing client mod \"{}\" (version {})...", MODID, VERSION);
        CustomMappingsJSONManager.tryMakeConfigFiles();
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();
    }
}

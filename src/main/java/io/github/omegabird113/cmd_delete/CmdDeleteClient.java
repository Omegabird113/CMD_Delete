package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing client mod \"{}\"...", MODID);
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();
    }
}

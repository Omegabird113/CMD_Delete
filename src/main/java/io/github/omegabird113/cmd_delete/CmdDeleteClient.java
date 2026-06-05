package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.commands.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    private static final Optional<ModContainer> CONTAINER = FabricLoader.getInstance().getModContainer(MODID);
    public static final String VERSION =  CONTAINER.isEmpty() ? "<unknown>" : CONTAINER.get().getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeClient() {
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();
    }
}

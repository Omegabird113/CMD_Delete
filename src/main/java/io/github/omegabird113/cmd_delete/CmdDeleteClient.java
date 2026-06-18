package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");
    public static final int MAPPINGS_FORMAT_VERSION = 2;
    private static final Path GAME_PATH = FabricLoader.getInstance().getGameDir();
    public static final Path MAPPINGS_JSONS_PATH = GAME_PATH.resolve("config/cmd_delete/mappings/");
    public static final Path ACTIVE_MAPPINGS_FILE_PATH = GAME_PATH.resolve("config/cmd_delete/.active_mappings");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {})...", MODID, VERSION, MAPPINGS_FORMAT_VERSION);
        LOGGER.debug("Resolved MAPPINGS_JSONS_PATH=\"{}\" and ACTIVE_MAPPINGS_FILE_PATH=\"{}\"", MAPPINGS_JSONS_PATH, ACTIVE_MAPPINGS_FILE_PATH);
        MappingsJSONManager.tryMakeConfigFiles();
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();
    }
}

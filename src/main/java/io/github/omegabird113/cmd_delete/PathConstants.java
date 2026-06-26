package io.github.omegabird113.cmd_delete;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PathConstants {
    public static final Path MAPPINGS_RESOURCE_PATH = FabricLoader.getInstance().getModContainer(CmdDeleteClient.MODID)
            .orElseThrow().findPath("mappings/").orElseThrow();
    private static final Path GAME_PATH = FabricLoader.getInstance().getGameDir();
    public static final Path ACTIVE_MAPPINGS_FILE_PATH = GAME_PATH.resolve("config/cmd_delete/.active_mappings");
    public static final Path MAPPINGS_JSONS_PATH = GAME_PATH.resolve("config/cmd_delete/mappings/");
    private static final Logger LOGGER = LoggingManager.getInitializerLogger(PathConstants.class);

    static {
        LOGGER.debug("Resolved MAPPINGS_RESOURCE_PATH=\"{}\", MAPPINGS_JSONS_PATH=\"{}\", ACTIVE_MAPPINGS_FILE_PATH=\"{}\", GAME_PATH=\"{}\"", PathConstants.MAPPINGS_RESOURCE_PATH, PathConstants.MAPPINGS_JSONS_PATH, PathConstants.ACTIVE_MAPPINGS_FILE_PATH, PathConstants.GAME_PATH);
    }

    private PathConstants() {
    }
}

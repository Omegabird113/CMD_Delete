package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public final class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd_delete";
    private static final Logger LOGGER = getLogger();
    public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");
    public static final int MAPPINGS_FORMAT_VERSION = 2;
    public static final Path MAPPINGS_RESOURCE_PATH = FabricLoader.getInstance().getModContainer(CmdDeleteClient.MODID)
            .orElseThrow().findPath("mappings/").orElseThrow();
    private static final Path GAME_PATH = FabricLoader.getInstance().getGameDir();
    public static final Path MAPPINGS_JSONS_PATH = GAME_PATH.resolve("config/cmd_delete/mappings/");
    public static final Path ACTIVE_MAPPINGS_FILE_PATH = GAME_PATH.resolve("config/cmd_delete/.active_mappings");

    @Override
    public void onInitializeClient() {
        final long startTime = System.nanoTime();

        LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {})...", MODID, VERSION, MAPPINGS_FORMAT_VERSION);
        LOGGER.info("User appears to be running system: {}", Os.getCurrent());
        LOGGER.debug("Resolved MAPPINGS_RESOURCE_PATH=\"{}\", MAPPINGS_JSONS_PATH=\"{}\", and ACTIVE_MAPPINGS_FILE_PATH=\"{}\"", MAPPINGS_RESOURCE_PATH, MAPPINGS_JSONS_PATH, ACTIVE_MAPPINGS_FILE_PATH);

        MixinEnvironment mixinEnv = MixinEnvironment.getCurrentEnvironment();
        LOGGER.info("Mixin version {} with obfuscation \"{}\" and compatability level \"{}\" in phase \"{}\" on side \"{}\"", mixinEnv.getVersion(), mixinEnv.getObfuscationContext(), MixinEnvironment.getCompatibilityLevel(), mixinEnv.getPhase(), mixinEnv.getSide());

        MappingsJSONManager.tryMakeConfigFiles();
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();

        final long takenNanos = System.nanoTime() - startTime;
        final long takenMillis = TimeUnit.NANOSECONDS.toMillis(takenNanos);

        LOGGER.info("Finished initializing after a total of {} milliseconds", takenMillis);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(MODID + "/" + clazz.getSimpleName());
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(MODID);
    }
}

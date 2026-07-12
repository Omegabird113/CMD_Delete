package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public final class CmdDeleteClient implements ClientModInitializer {
    public static final @NonNull String MODID = "cmd_delete";
    public static final @NonNull String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");
    public static final int CURRENT_MAPPINGS_FORMAT_VERSION = 4;
    public static final int MINIMUM_MAPPINGS_FORMAT_VERSION = 2;
    public static final int SHARE_CODE_FORMAT_VERSION = 1;
    private static final Logger LOGGER = LoggingManager.getInitializerLogger();

    @Override
    public void onInitializeClient() {
        final long startTime = System.nanoTime();

        LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {}, minimum mappings compatible version: {})...", MODID, VERSION, CURRENT_MAPPINGS_FORMAT_VERSION, MINIMUM_MAPPINGS_FORMAT_VERSION);
        LOGGER.info("User appears to be running system: {}", Os.USING);

        final MixinEnvironment mixinEnv = MixinEnvironment.getCurrentEnvironment();
        LOGGER.debug("Mixin version {} with obfuscation \"{}\" and compatability level \"{}\" in phase \"{}\" on side \"{}\"", mixinEnv.getVersion(), mixinEnv.getObfuscationContext(), MixinEnvironment.getCompatibilityLevel(), mixinEnv.getPhase(), mixinEnv.getSide());
        final long startLogTime = System.nanoTime();

        final Path gameDir = FabricLoader.getInstance().getGameDir();
        final Path resourceMappingsDir = FabricLoader.getInstance().getModContainer(CmdDeleteClient.MODID)
                .orElseThrow().findPath("mappings/").orElseThrow();
        PathConstants.init(gameDir, resourceMappingsDir);

        final long fileInitTime = System.nanoTime();

        NavMappingsManager.loadMappings();
        final long loadMappingsTime = System.nanoTime();

        NavMappingsCommand.register();
        final long registerTime = System.nanoTime();

        final long startLogTakenNanos = startLogTime - startTime;
        final double startLogTakenMillis = startLogTakenNanos / 1000000.0;
        final long fileInitTakenNanos = fileInitTime - startLogTime;
        final double fileInitTakenMillis = fileInitTakenNanos / 1000000.0;
        final long loadMappingsTakenNanos = loadMappingsTime - fileInitTime;
        final double loadMappingsTakenMillis = loadMappingsTakenNanos / 1000000.0;
        final long registerTakenNanos = registerTime - loadMappingsTime;
        final double registerTakenMillis = registerTakenNanos / 1000000.0;
        final long totalTakenNanos = registerTime - startTime;
        final long totalTakenMillis = TimeUnit.NANOSECONDS.toMillis(totalTakenNanos);

        LOGGER.info("Finished initializing after a total of {} milliseconds", totalTakenMillis);
        LOGGER.debug("Startup/mixin: {}ms, File init: {}ms, Mappings load: {}ms, /navmappings register: {}ms, Total: {}ms", startLogTakenMillis, fileInitTakenMillis, loadMappingsTakenMillis, registerTakenMillis, totalTakenMillis);
    }
}

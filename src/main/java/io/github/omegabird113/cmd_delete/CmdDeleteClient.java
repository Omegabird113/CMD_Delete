package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.concurrent.TimeUnit;

public final class CmdDeleteClient implements ClientModInitializer {
    public static final @NonNull String MODID = "cmd_delete";
    public static final @NonNull String VERSION = FabricLoader.getInstance().getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");
    public static final int MAPPINGS_FORMAT_VERSION = 2;
    private static final Logger LOGGER = LoggingManager.getInitializerLogger();

    @Override
    public void onInitializeClient() {
        final long startTime = System.nanoTime();

        LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {})...", MODID, VERSION, MAPPINGS_FORMAT_VERSION);
        LOGGER.info("User appears to be running system: {}", Os.getCurrent());

        MixinEnvironment mixinEnv = MixinEnvironment.getCurrentEnvironment();
        LOGGER.debug("Mixin version {} with obfuscation \"{}\" and compatability level \"{}\" in phase \"{}\" on side \"{}\"", mixinEnv.getVersion(), mixinEnv.getObfuscationContext(), MixinEnvironment.getCompatibilityLevel(), mixinEnv.getPhase(), mixinEnv.getSide());

        MappingsJSONManager.tryMakeConfigFiles();
        NavMappingsManager.loadMappings();
        NavMappingsCommand.register();

        final long takenNanos = System.nanoTime() - startTime;
        final long takenMillis = TimeUnit.NANOSECONDS.toMillis(takenNanos);

        LOGGER.info("Finished initializing after a total of {} milliseconds", takenMillis);
    }
}

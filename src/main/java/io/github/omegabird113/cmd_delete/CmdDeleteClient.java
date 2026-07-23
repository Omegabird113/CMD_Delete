package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.nio.file.Path;

public final class CmdDeleteClient implements ClientModInitializer {
    public static final @NonNull String MODID = "cmd_delete";
    public static final @NonNull FabricLoader LOADER = FabricLoader.getInstance();
    public static final @NonNull String VERSION = LOADER.getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("<unknown>");
    public static final int CURRENT_MAPPINGS_FORMAT_VERSION = 4;
    public static final int MINIMUM_MAPPINGS_FORMAT_VERSION = 2;
    public static final int SHARECODE_FORMAT_VERSION = 1;
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(CmdDeleteClient.class);

    @Override
    public void onInitializeClient() {
        LoadTimer.time(() -> {
            LoadTimer.time(() -> {
                LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {}, minimum mappings compatible version: {}, sharecode encoding version: {})...", MODID, VERSION, CURRENT_MAPPINGS_FORMAT_VERSION, MINIMUM_MAPPINGS_FORMAT_VERSION, SHARECODE_FORMAT_VERSION);
                LOGGER.info("User appears to be running system: {}", Os.USING);

                final MixinEnvironment mixinEnv = MixinEnvironment.getCurrentEnvironment();
                LOGGER.debug("Mixin version {} with obfuscation \"{}\" and compatibility level \"{}\" in phase \"{}\" on side \"{}\"", mixinEnv.getVersion(), mixinEnv.getObfuscationContext(), MixinEnvironment.getCompatibilityLevel(), mixinEnv.getPhase(), mixinEnv.getSide());
            }, "initial logging", true);

            LoadTimer.time(() -> {
                final Path gameDir = LOADER.getGameDir();
                final Path resourceMappingsDir = LOADER.getModContainer(CmdDeleteClient.MODID)
                        .orElseThrow().findPath("mappings/").orElseThrow();
                PathConstants.init(gameDir, resourceMappingsDir);
            }, "path initialization", true);

            LoadTimer.time(NavMappingsManager::loadMappings, "loading mappings", true);
            LoadTimer.time(NavMappingsCommand::register, "registering /navmappings", true);
        }, "full load", false);

        if (Boolean.getBoolean("ci.stopMinecraftAfterLoad")) {
            LOGGER.info("Stopping Minecraft client due to set \"ci.stopMinecraftAfterLoad\" jvm property...");
            Minecraft.getInstance().stop();
            System.exit(0);
        }
    }
}

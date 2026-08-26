/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.utils.CrashUtils;
import io.github.omegabird113.cmd_delete.utils.LoadTimer;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.utils.Os;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.nio.file.Path;

public final class CmdDeleteClient {
    public static final @NonNull String MODID = "cmd_delete";
    public static final @NonNull String ISSUE_TRACKER_URL_STRING = "https://github.com/Omegabird113/CMD_Delete/issues";
    public static final int CURRENT_MAPPINGS_FORMAT_VERSION = 4;
    public static final int MINIMUM_MAPPINGS_FORMAT_VERSION = 2;
    public static final int SHARECODE_FORMAT_VERSION = 1;
    public static final boolean FORCE_PREVENT_OVERRIDE_MODE = Boolean.getBoolean("cmd_delete.forcePreventOverrideMode");
    public static final @NonNull Gson GSON = new GsonBuilder()
            .create();
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(CmdDeleteClient.class);
    public static @NonNull String VERSION = "unknown";
    private static @Nullable IPlatform platform;

    public static @NonNull IPlatform getPlatform() {
        if (platform == null)
            throw new IllegalStateException("No CmdDeleteClient class instance defined");
        return platform;
    }

    public static void start(IPlatform platform) {
        LoadTimer.time(() -> CrashUtils.crashMinecraftOnFailure(() -> {
            LoadTimer.time(() -> {
                CmdDeleteClient.platform = platform;
                VERSION = platform.getModVersion();
            }, "Registering platform information", true);

            LoadTimer.time(() -> {
                LOGGER.info("Initializing client mod \"{}\" (version: {}, mappings format version: {}, minimum mappings compatible version: {}, sharecode encoding version: {})... You can report any issues at {}.", MODID, platform.getModVersion(), CURRENT_MAPPINGS_FORMAT_VERSION, MINIMUM_MAPPINGS_FORMAT_VERSION, SHARECODE_FORMAT_VERSION, ISSUE_TRACKER_URL_STRING);
                LOGGER.info("User appears to be running system: {}", Os.USING);

                final MixinEnvironment mixinEnv = MixinEnvironment.getCurrentEnvironment();
                LoggingManager.traceLog(LOGGER, "Mixin version {} with obfuscation \"{}\" and compatibility level \"{}\" in phase \"{}\" on side \"{}\"", mixinEnv.getVersion(), mixinEnv.getObfuscationContext(), MixinEnvironment.getCompatibilityLevel(), mixinEnv.getPhase(), mixinEnv.getSide());

                CrashUtils.sendLoadInfo();

                if (FORCE_PREVENT_OVERRIDE_MODE)
                    LOGGER.warn("Override mode is not allowed for any mappings set due to the \"cmd_delete.forcePreventOverrideMode\" JVM property being set to true. This prevents any mappings set from using override actions even if it enables the Feature Flag. This mode is not recommended unless you are actively troubleshooting an issue with override mode.");
            }, "initial logging & CrashUtils info", true);

            LoadTimer.time(() -> {
                final Path gameDir = platform.getGamePath();
                final Path resourceMappingsDir = platform.getResourcePath().resolve("mappings/");
                PathConstants.init(gameDir, resourceMappingsDir);
            }, "path initialization", true);

            LoadTimer.time(NavMappingsManager::loadMappings, "loading mappings", true);
            LoadTimer.time(() -> platform.registerClientCommand(NavMappingsCommand::register), "registering /navmappings", true);
        }), "full load", false);

        if (Boolean.getBoolean("cmd_delete.ci.stopMinecraftAfterLoad")) {
            LOGGER.info("Stopping Minecraft client due to set \"cmd_delete.ci.stopMinecraftAfterLoad\" jvm property...");
            System.exit(0);
        }
    }
}

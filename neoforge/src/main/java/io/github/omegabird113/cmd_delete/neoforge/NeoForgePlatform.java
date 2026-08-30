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

package io.github.omegabird113.cmd_delete.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public final class NeoForgePlatform implements IPlatform {
    public static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(NeoForgePlatform.class);
    private CommandRegistration<?> commandRegistration;

    public NeoForgePlatform() {
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, this::registerClientCommand);
    }

    @SuppressWarnings("unchecked")
    private <S extends SharedSuggestionProvider> void registerClientCommand(final RegisterClientCommandsEvent event) {
        if (commandRegistration == null)
            throw new IllegalStateException("Client command registration was requested before the platform was initialized");
        ((CommandRegistration<S>) commandRegistration).register((CommandDispatcher<S>) event.getDispatcher());
    }

    @Override
    public <S extends SharedSuggestionProvider> void registerClientCommand(final @NonNull CommandRegistration<S> registration) {
        commandRegistration = registration;
    }

    @Contract(pure = true)
    @Override
    public @NonNull BiConsumer<SharedSuggestionProvider, Component> getFeedbackMethod() {
        return (source, component) -> ((CommandSourceStack) source).sendSuccess(() -> component, false);
    }

    @Override
    public @NotNull String getPlatformName() {
        return "NeoForge Platform";
    }

    @Override
    public @NonNull String getModVersion() {
        return ModList.get().getModContainerById(CmdDeleteClient.MODID)
                .orElseThrow(() -> new IllegalStateException("CMD + Delete is not present in NeoForge's mod list"))
                .getModInfo().getVersion().toString();
    }

    @Override
    public @NonNull Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public @NonNull Path getResourcePath() {
        final String devMappings = System.getProperty("cmd_delete.dev.mappings");
        if (devMappings != null) {
            final Path path = Paths.get(devMappings).resolve("mappings");
            if (Files.isDirectory(path))
                return path;
            throw new IllegalStateException("Configured development mappings directory does not exist: " + path);
        }
        try {
            final Path tempDir = Files.createTempDirectory("cmd-delete-mappings");
            for (String mappings : MappingsInfoCollectionUtils.getBuiltinMappingsNamespacedIdsList().stream().map(MappingsIdResolutionUtils::removeNamespaceFromId).toList())
                try (InputStream in = NeoForgePlatform.class.getClassLoader().getResourceAsStream("mappings/" + mappings + ".json")) {
                    if (in == null)
                        throw new IllegalStateException("Missing bundled mapping: mappings/" + mappings + ".json");
                    Files.copy(in, tempDir.resolve(mappings + ".json"));
                }
            return tempDir;
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract CMD + Delete mappings", e);
        }
    }

    @Override
    public @NotNull Logger getPlatformLogger() {
        return LOGGER;
    }
}

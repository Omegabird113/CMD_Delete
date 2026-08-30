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

package io.github.omegabird113.cmd_delete.fabric;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public final class FabricPlatform implements IPlatform {
    public static final @NonNull FabricLoader LOADER = FabricLoader.getInstance();
    public static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(FabricPlatform.class);

    @Override
    @SuppressWarnings("unchecked")
    public <S extends SharedSuggestionProvider> void registerClientCommand(final @NonNull CommandRegistration<S> registration) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> registration.register((CommandDispatcher<S>) dispatcher));
    }

    @Override
    public BiConsumer<SharedSuggestionProvider, Component> getFeedbackMethod() {
        return (source, component) -> ((FabricClientCommandSource) source).sendFeedback(component);
    }

    @Override
    public @NotNull String getPlatformName() {
        return "Fabric Platform";
    }

    @Override
    public @NonNull String getModVersion() {
        return LOADER.getModContainer(CmdDeleteClient.MODID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("<unknown>");
    }

    @Override
    public @NonNull Path getGamePath() {
        return LOADER.getGameDir();
    }

    @Override
    public @NonNull Path getResourcePath() {
        return LOADER.getModContainer(CmdDeleteClient.MODID).orElseThrow()
                .findPath("mappings/").orElseThrow();
    }

    @Override
    public @NotNull Logger getPlatformLogger() {
        return LOGGER;
    }
}

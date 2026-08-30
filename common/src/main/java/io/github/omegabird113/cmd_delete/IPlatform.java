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

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public interface IPlatform {
    @NonNull String getModVersion();

    @NonNull Path getGamePath();

    @NonNull Path getResourcePath();

    <S extends SharedSuggestionProvider> void registerClientCommand(@NonNull CommandRegistration<S> registration);

    @FunctionalInterface
    interface CommandRegistration<S extends SharedSuggestionProvider> {
        void register(@NonNull CommandDispatcher<S> dispatcher);
    }

    @Nullable BiConsumer<@NonNull SharedSuggestionProvider, @NonNull Component> getFeedbackMethod();

    @NotNull String getPlatformName();

    @NotNull Logger getPlatformLogger();
}

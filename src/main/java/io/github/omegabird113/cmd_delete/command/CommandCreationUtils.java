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

package io.github.omegabird113.cmd_delete.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsJSONManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class CommandCreationUtils {
    public static final @NonNull DynamicCommandExceptionType UNKNOWN_CUSTOM_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.cmd_delete.error.unknown_custom_mappings", id)
    );
    public static final @NonNull DynamicCommandExceptionType UNKNOWN_BUILTIN_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.cmd_delete.error.unknown_builtin_mappings", id)
    );
    public static final @NonNull DynamicCommandExceptionType FAILED_CUSTOM_MAPPINGS_IMPORT = new DynamicCommandExceptionType(
            location -> Component.translatable("commands.cmd_delete.error.failed_custom_mappings_import", location)
    );
    public static final @NonNull DynamicCommandExceptionType INVALID_SHARE_CODE = new DynamicCommandExceptionType(
            shareCode -> Component.translatable("commands.cmd_delete.error.invalid_share_code", shareCode)
    );

    public static final @NonNull SuggestionProvider<@NonNull FabricClientCommandSource> BUILTIN_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggest(List.of("windows_linux", "mac", "emacs_windows_linux", "emacs_mac", "readline"), builder);
    public static final @NonNull SuggestionProvider<@NonNull FabricClientCommandSource> CUSTOM_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggest(MappingsJSONManager.getAvailableOptions(false), builder);

    private CommandCreationUtils() {
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull LiteralArgumentBuilder<@NonNull FabricClientCommandSource> literal(final @NonNull String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <T> @NonNull RequiredArgumentBuilder<@NonNull FabricClientCommandSource, T> argument(final @NonNull String name, final @NonNull ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}

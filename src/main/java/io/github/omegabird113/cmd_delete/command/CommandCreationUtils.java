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

final class CommandCreationUtils {
    static final @NonNull DynamicCommandExceptionType UNKNOWN_CUSTOM_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load custom navmappings: " + id)
    );
    static final @NonNull DynamicCommandExceptionType UNKNOWN_BUILTIN_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load builtin navmappings: " + id)
    );
    static final @NonNull DynamicCommandExceptionType FAILED_CUSTOM_MAPPINGS_IMPORT = new DynamicCommandExceptionType(
            location -> Component.literal("Could not import custom navmappings from: " + location)
    );
    static final @NonNull DynamicCommandExceptionType INVALID_SHARE_CODE = new DynamicCommandExceptionType(
            shareCode -> Component.literal("Invalid share code: " + shareCode)
    );

    static final @NonNull SuggestionProvider<FabricClientCommandSource> BUILTIN_SUGGESTIONS =
            (_, builder) -> SharedSuggestionProvider.suggest(List.of("windows_linux", "mac", "emacs_windows_linux", "emacs_mac", "readline"), builder);
    static final @NonNull SuggestionProvider<FabricClientCommandSource> CUSTOM_SUGGESTIONS =
            (_, builder) -> SharedSuggestionProvider.suggest(MappingsJSONManager.getAvailableOptions(false), builder);

    private CommandCreationUtils() {
    }

    @Contract(value = "_ -> new", pure = true)
    static @NonNull LiteralArgumentBuilder<FabricClientCommandSource> literal(@NonNull String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static <T> @NonNull RequiredArgumentBuilder<FabricClientCommandSource, T> argument(@NonNull String name, @NonNull ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}

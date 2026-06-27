package io.github.omegabird113.cmd_delete.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.config.*;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

public final class NavMappingsCommand {
    private static final DynamicCommandExceptionType INVALID_OS = new DynamicCommandExceptionType(
            os -> Component.literal("Unknown builtin navmappings OS: " + os)
    );
    private static final DynamicCommandExceptionType UNKNOWN_CUSTOM_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load custom navmappings: " + id)
    );
    private static final DynamicCommandExceptionType UNKNOWN_BUILTIN_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load builtin navmappings: " + id)
    );
    private static final DynamicCommandExceptionType FAILED_CUSTOM_MAPPINGS_IMPORT = new DynamicCommandExceptionType(
            location -> Component.literal("Could not import custom navmappings from: " + location)
    );

    private static final SuggestionProvider<FabricClientCommandSource> BUILTIN_SUGGESTIONS =
            (source, builder) ->
                    SharedSuggestionProvider.suggest(List.of("windows_linux", "mac"), builder);

    private static final SuggestionProvider<FabricClientCommandSource> CUSTOM_SUGGESTIONS =
            (source, builder) ->
                    SharedSuggestionProvider.suggest(MappingsJSONManager.getAvailableOptions(false), builder);

    private static final Logger LOGGER = LoggingManager.getLogger(NavMappingsCommand.class);

    private NavMappingsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> register(dispatcher));
        LOGGER.info("Registered client command \"/navmappings\" through Fabric API");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> register(dispatcher));
        LOGGER.info("Registered client \"/navmappings\" command through Fabric API");
    }

    private static void register(@NonNull CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("navmappings")
                .then(literal("set")
                        .then(literal("builtin")
                                .then(argument("os", StringArgumentType.word())
                                        .suggests(BUILTIN_SUGGESTIONS)
                                        .executes(NavMappingsCommand::setBuiltIn)))
                        .then(literal("custom")
                                .then(argument("id", StringArgumentType.word())
                                        .suggests(CUSTOM_SUGGESTIONS)
                                        .executes(NavMappingsCommand::setCustom)))
                        .then(literal("default")
                                .executes(NavMappingsCommand::setDefault))
                )
                .then(literal("info").executes(NavMappingsCommand::printMappingsInfo))
                .then(literal("list").executes(NavMappingsCommand::printMappingsList))
                .then(literal("reload").executes(NavMappingsCommand::reloadMappings))
                .then(literal("debug")
                        .then(literal("aboutCmdDelete").executes(NavMappingsCommand::printCmdDeleteAbout))
                        .then(literal("dumpRegistry").executes(NavMappingsCommand::dumpRegistry))
                        .then(literal("dumpKeymap").executes(NavMappingsCommand::dumpKeyMap))
                )
                .then(literal("export")
                        .then(literal("builtin")
                                .then(argument("id", StringArgumentType.word())
                                        .suggests(BUILTIN_SUGGESTIONS)
                                        .then(argument("location", StringArgumentType.greedyString()).executes(NavMappingsCommand::exportBuiltin)))
                        )
                        .then(literal("custom")
                                .then(argument("id", StringArgumentType.word())
                                        .suggests(CUSTOM_SUGGESTIONS)
                                        .then(argument("location", StringArgumentType.greedyString()).executes(NavMappingsCommand::exportCustom)))
                        )
                )
                .then(literal("import")
                        .then(argument("location", StringArgumentType.greedyString()).executes(NavMappingsCommand::importCustom)))
        );
    }

    private static int dumpRegistry(@NonNull CommandContext<FabricClientCommandSource> context) {
        MappingsRegistry mr = NavMappingsManager.getCurrentMappings().getRegistry();
        context.getSource().sendFeedback(Component.literal("Registry dump:\n" + mr.toString().replace("\t", "    ")));
        return 1;
    }

    private static int dumpKeyMap(@NonNull CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("KeyMap dump:\n" + KeyCodeRegistry.getDumpString()));
        return 1;
    }

    private static int exportCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String idStr = StringArgumentType.getString(context, "id");
        String locationStr = StringArgumentType.getString(context, "location");

        Path configPath = PathConstants.MAPPINGS_JSONS_PATH;
        Path oldPath = configPath.resolve(idStr + ".json");

        Path newPath = Path.of(locationStr);
        if (!newPath.isAbsolute()) {
            LOGGER.error("New path \"{}\" for custom copy is not absolute", locationStr);
            throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
        }

        if (!oldPath.toFile().exists() || !oldPath.toFile().isFile()) {
            LOGGER.error("Error while reading custom mappings. File does not exist: {}", oldPath.toAbsolutePath());
            throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
        }

        try {
            Files.createDirectories(newPath.getParent());
            Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Error while copying custom mappings", e);
            throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
        }

        context.getSource().sendFeedback(Component.literal("Mappings \"custom:" + idStr + "\" copied to path: " + newPath.toAbsolutePath()));
        return 1;
    }

    private static int exportBuiltin(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String idStr = StringArgumentType.getString(context, "id");
        String locationStr = StringArgumentType.getString(context, "location");

        Path newPath = Path.of(locationStr);
        if (!newPath.isAbsolute()) {
            LOGGER.error("New path \"{}\" for builtin copy is not absolute", locationStr);
            throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        String resourceSubPathStr = "/mappings/" + idStr + ".json";

        try (InputStream resourceStream = NavMappingsCommand.class.getResourceAsStream(resourceSubPathStr)) {
            if (resourceStream == null) {
                LOGGER.error("Builtin mappings do not exist: {}", resourceSubPathStr);
                throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
            }
            Files.createDirectories(newPath.getParent());
            Files.copy(resourceStream, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Error while exporting builtin mappings", e);
            throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        context.getSource().sendFeedback(Component.literal("Mappings \"builtin:" + idStr + "\" copied to path: " + newPath.toAbsolutePath()));
        return 1;
    }

    private static int importCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String locationStr = StringArgumentType.getString(context, "location");

        Path configPath = PathConstants.MAPPINGS_JSONS_PATH;

        Path oldPath = Path.of(locationStr);
        Path newPath = configPath.resolve(FilenameUtils.getBaseName(locationStr) + ".json");

        if (!oldPath.isAbsolute()) {
            LOGGER.error("From path \"{}\" for custom import is not absolute", locationStr);
            throw FAILED_CUSTOM_MAPPINGS_IMPORT.create(locationStr);
        }

        if (!oldPath.toFile().exists() || !oldPath.toFile().isFile()) {
            LOGGER.error("Error while reading custom mappings to import them. File does not exist: {}", oldPath.toAbsolutePath());
            throw FAILED_CUSTOM_MAPPINGS_IMPORT.create(locationStr);
        }

        try {
            Files.createDirectories(newPath.getParent());
            Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Error while copying custom mappings", e);
            throw FAILED_CUSTOM_MAPPINGS_IMPORT.create(locationStr);
        }

        context.getSource().sendFeedback(Component.literal("Mappings from " + locationStr + " copied to path now available as \"custom:" + FilenameUtils.getBaseName(locationStr) + "\""));
        return 1;
    }

    private static int reloadMappings(@NonNull CommandContext<FabricClientCommandSource> context) {
        NavMappingsManager.loadMappings();
        context.getSource().sendFeedback(Component.literal("Reloaded mappings: \"" + MappingsIdResolutionUtils.resolveNamespacedId(NavMappingsManager.getMappingsState()) + "\""));
        return 1;
    }

    private static int setBuiltIn(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String osName = StringArgumentType.getString(context, "os");
        Os os = resolveOs(osName);
        NavMappingsManager.updateMappingsToBuiltIn(os);
        context.getSource().sendFeedback(Component.literal("Set nav mappings to builtin:" + resolveOsId(os)));
        return 1;
    }

    private static int setCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "id");
        if (!NavMappingsManager.updateMappingsToCustom(id))
            throw UNKNOWN_CUSTOM_MAPPINGS.create(id);
        context.getSource().sendFeedback(Component.literal("Set nav mappings to custom:" + id));
        return 1;
    }

    private static int setDefault(@NonNull CommandContext<FabricClientCommandSource> context) {
        NavMappingsManager.updateMappingsToDefault();
        context.getSource().sendFeedback(Component.literal("Set nav mappings to default"));
        return 1;
    }

    private static int printMappingsInfo(@NonNull CommandContext<FabricClientCommandSource> context) {
        MappingsState currentMappingState = NavMappingsManager.getMappingsState();
        String info = MappingsInfoCollectionUtils.getInfoFrom(currentMappingState, true);
        context.getSource().sendFeedback(Component.literal("The currently active mappings are:\n" + info));
        return 1;
    }

    private static int printMappingsList(@NonNull CommandContext<FabricClientCommandSource> context) {
        String[] options = MappingsInfoCollectionUtils.getMappingsList();
        context.getSource().sendFeedback(Component.literal("The currently available mappings options are:\n" + String.join("\n", options)));
        return 1;
    }

    private static int printCmdDeleteAbout(@NonNull CommandContext<FabricClientCommandSource> context) {
        String about = "CMD + Delete (modid: " + CmdDeleteClient.MODID
                + ") by Omegabird113 v" + CmdDeleteClient.VERSION
                + " using mappings format version " + CmdDeleteClient.MAPPINGS_FORMAT_VERSION;
        context.getSource().sendFeedback(Component.literal(about));
        return 1;
    }

    private static @NonNull Os resolveOs(@NonNull String osName) throws CommandSyntaxException {
        osName = osName.toLowerCase(Locale.ROOT);
        return switch (osName) {
            case "mac" -> Os.MAC;
            case "windows_linux" -> Os.WINDOWS;
            default -> throw INVALID_OS.create(osName);
        };
    }

    private static @NonNull String resolveOsId(@NonNull Os os) {
        return switch (os) {
            case MAC -> "mac";
            case WINDOWS, LINUX -> "windows_linux";
        };
    }

    @Contract(value = "_ -> new", pure = true)
    private static @NonNull LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @Contract(value = "_, _ -> new", pure = true)
    private static <T> @NonNull RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}

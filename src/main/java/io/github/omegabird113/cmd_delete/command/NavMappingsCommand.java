package io.github.omegabird113.cmd_delete.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.KeyCodeRegistry;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.JsonParsingUtils;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeDecoder;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public final class NavMappingsCommand {
    private static final DynamicCommandExceptionType UNKNOWN_CUSTOM_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load custom navmappings: " + id)
    );
    private static final DynamicCommandExceptionType UNKNOWN_BUILTIN_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load builtin navmappings: " + id)
    );
    private static final DynamicCommandExceptionType FAILED_CUSTOM_MAPPINGS_IMPORT = new DynamicCommandExceptionType(
            location -> Component.literal("Could not import custom navmappings from: " + location)
    );
    private static final DynamicCommandExceptionType INVALID_SHARE_CODE = new DynamicCommandExceptionType(
            shareCode -> Component.literal("Invalid share code: " + shareCode)
    );

    private static final SuggestionProvider<FabricClientCommandSource> BUILTIN_SUGGESTIONS =
            (_, builder) -> SharedSuggestionProvider.suggest(List.of("windows_linux", "mac", "emacs_windows_linux", "emacs_mac"), builder);

    private static final SuggestionProvider<FabricClientCommandSource> CUSTOM_SUGGESTIONS =
            (_, builder) -> SharedSuggestionProvider.suggest(MappingsJSONManager.getAvailableOptions(false), builder);

    private static final Logger LOGGER = LoggingManager.getLogger(NavMappingsCommand.class);

    private NavMappingsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> register(dispatcher));
        LOGGER.info("Registered client command \"/navmappings\" through Fabric API");
    }

    private static void register(@NonNull CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("navmappings")
                .then(literal("set")
                        .then(literal("builtin")
                                .then(argument("id", StringArgumentType.word())
                                        .suggests(BUILTIN_SUGGESTIONS)
                                        .executes(NavMappingsCommand::setBuiltIn))
                        )
                        .then(literal("custom")
                                .then(argument("id", StringArgumentType.word())
                                        .suggests(CUSTOM_SUGGESTIONS)
                                        .executes(NavMappingsCommand::setCustom))
                        )
                        .then(literal("default")
                                .executes(NavMappingsCommand::setDefault)
                        )
                )
                .then(literal("info").executes(NavMappingsCommand::printMappingsInfo))
                .then(literal("list").executes(NavMappingsCommand::printMappingsList))
                .then(literal("reload").executes(NavMappingsCommand::reloadMappings))
                .then(literal("debug")
                        .then(literal("aboutCmdDelete").executes(NavMappingsCommand::printCmdDeleteAbout))
                        .then(literal("dumpActions").executes(NavMappingsCommand::dumpActions))
                        .then(literal("dumpFeatureFlags").executes(NavMappingsCommand::dumpFeatureFlags))
                        .then(literal("dumpRegistry").executes(NavMappingsCommand::dumpRegistry))
                        .then(literal("dumpKeymap").executes(NavMappingsCommand::dumpKeyMap))
                )
                .then(literal("export")
                        .then(literal("builtin")
                                .then(literal("file")
                                        .then(argument("id", StringArgumentType.word())
                                                .suggests(BUILTIN_SUGGESTIONS)
                                                .then(argument("location", StringArgumentType.greedyString()).executes(NavMappingsCommand::exportBuiltin)))
                                )
                                .then(literal("sharecode")
                                        .then(argument("id", StringArgumentType.word())
                                                .suggests(BUILTIN_SUGGESTIONS)
                                                .executes(NavMappingsCommand::exportBuiltinShareCode))
                                )
                        )
                        .then(literal("custom")
                                .then(literal("file")
                                        .then(argument("id", StringArgumentType.word())
                                                .suggests(CUSTOM_SUGGESTIONS)
                                                .then(argument("location", StringArgumentType.greedyString()).executes(NavMappingsCommand::exportCustom))
                                        )
                                )
                                .then(literal("sharecode")
                                        .then(argument("id", StringArgumentType.word())
                                                .suggests(CUSTOM_SUGGESTIONS)
                                                .executes(NavMappingsCommand::exportCustomShareCode)
                                        )
                                )
                        )
                )
                .then(literal("import")
                        .then(literal("file")
                                .then(argument("location", StringArgumentType.greedyString())
                                        .executes(NavMappingsCommand::importCustom)
                                )
                        )
                        .then(literal("sharecode")
                                .then(literal("clipboard")
                                        .executes(NavMappingsCommand::importCustomShareCode)
                                )
                                .then(literal("chat")
                                        .then(argument("sharecode", StringArgumentType.greedyString()).executes(NavMappingsCommand::importCustomShareCodeFromChat))
                                )
                        )
                )
        );
    }

    private static int dumpRegistry(@NonNull CommandContext<FabricClientCommandSource> context) {
        final MappingsRegistry mr = NavMappingsManager.getCurrentMappingsRegistry();
        context.getSource().sendFeedback(Component.literal("Registry dump:\n" + mr.toString().replace("\t", "    ")));
        return 1;
    }

    private static int dumpActions(@NonNull CommandContext<FabricClientCommandSource> context) {
        final String actionsDump = String.join(", ", Arrays.stream(NavAction.values()).map(NavAction::name).toArray(String[]::new));
        context.getSource().sendFeedback(Component.literal("Actions dump:\n" + actionsDump));
        return 1;
    }

    private static int dumpFeatureFlags(@NonNull CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("Feature flags dump:\noverrideVanillaNavigation - default false\ncrossLineSignMovement - default true"));
        return 1;
    }

    private static int dumpKeyMap(@NonNull CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("KeyMap dump:\n" + KeyCodeRegistry.getDumpString()));
        return 1;
    }

    private static void exportShareCode(@NonNull CommandContext<FabricClientCommandSource> context, boolean custom) {
        final String idStr = StringArgumentType.getString(context, "id");

        final String namespacedId = MappingsIdResolutionUtils.resolveNamespacedId(custom ? MappingsState.Type.CUSTOM : MappingsState.Type.BUILTIN, idStr);
        final String shareCode = ShareCodeGenerator.generate(namespacedId);

        Minecraft.getInstance().keyboardHandler.setClipboard(shareCode);
        context.getSource().sendFeedback(Component.literal("Mappings \"" + (custom ? "custom:" : "builtin:") + idStr + "\" can be shared as: " + shareCode));
    }

    private static int exportCustomShareCode(@NonNull CommandContext<FabricClientCommandSource> context) {
        exportShareCode(context, true);
        return 1;
    }

    private static int exportBuiltinShareCode(@NonNull CommandContext<FabricClientCommandSource> context) {
        exportShareCode(context, false);
        return 1;
    }

    private static int exportBuiltin(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        exportMappings(context, false);
        return 1;
    }

    private static int exportCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        exportMappings(context, true);
        return 1;
    }

    private static void exportMappings(@NonNull CommandContext<FabricClientCommandSource> context, boolean custom) throws CommandSyntaxException {
        final String idStr = StringArgumentType.getString(context, "id");
        final String locationStr = StringArgumentType.getString(context, "location");

        final Path newPath = Path.of(locationStr);
        if (!newPath.isAbsolute()) {
            LOGGER.error("New path \"{}\" for {} copy is not absolute", locationStr, custom ? "custom" : "builtin");
            if (custom)
                throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
            else
                throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        try {
            Files.createDirectories(newPath.getParent());
            if (custom) {
                final Path oldPath = PathConstants.getMappingsJSONPath().resolve(idStr + ".json");
                if (!Files.isRegularFile(oldPath)) {
                    LOGGER.error("Error while reading custom mappings. File does not exist: {}", oldPath.toAbsolutePath());
                    throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
                }
                Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                final String resourceSubPathStr = "/mappings/" + idStr + ".json";
                try (InputStream resourceStream = NavMappingsCommand.class.getResourceAsStream(resourceSubPathStr)) {
                    if (resourceStream == null) {
                        LOGGER.error("Builtin mappings do not exist: {}", resourceSubPathStr);
                        throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
                    }
                    Files.copy(resourceStream, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while {} mappings", custom ? "copying custom" : "exporting builtin", e);
            if (custom)
                throw UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
            else
                throw UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        context.getSource().sendFeedback(Component.literal("Mappings \"" + (custom ? "custom:" : "builtin:") + idStr + "\" copied to path: " + newPath.toAbsolutePath()));
    }

    private static void importShareCode(@NonNull CommandContext<FabricClientCommandSource> context, @NonNull String shareCode) throws CommandSyntaxException {
        String decoded;
        try {
            decoded = ShareCodeDecoder.decode(shareCode.trim());

            JsonObject jsonObject = new Gson().fromJson(decoded, JsonObject.class);
            JsonObject meta = JsonParsingUtils.requireObject(jsonObject, "meta");
            String idStr = JsonParsingUtils.requireString(meta, "id");

            Path toCopyTo = PathConstants.getMappingsJSONPath().resolve(idStr + ".json");
            try (FileWriter writer = new FileWriter(toCopyTo.toFile())) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            } catch (IOException e) {
                LOGGER.error("Error while importing custom mappings", e);
                throw FAILED_CUSTOM_MAPPINGS_IMPORT.create(idStr);
            }

            context.getSource().sendFeedback(Component.literal("Custom mappings sharecode imported successfully: " + idStr));
        } catch (IllegalArgumentException | JsonParseException e) {
            LOGGER.error("Invalid share code: {}", shareCode, e);
            throw INVALID_SHARE_CODE.create(shareCode);
        }
    }

    private static int importCustomShareCode(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String shareCode = Minecraft.getInstance().keyboardHandler.getClipboard();
        importShareCode(context, shareCode);
        return 1;
    }

    private static int importCustomShareCodeFromChat(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String shareCode = StringArgumentType.getString(context, "sharecode");
        importShareCode(context, shareCode);
        return 1;
    }

    private static int importCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        final String locationStr = StringArgumentType.getString(context, "location");

        final Path configPath = PathConstants.getMappingsJSONPath();

        final Path oldPath = Path.of(locationStr);
        final Path newPath = configPath.resolve(FilenameUtils.getBaseName(locationStr) + ".json");

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

        context.getSource().sendFeedback(Component.literal("Custom mappings from " + locationStr + " copied to path now available as \"custom:" + FilenameUtils.getBaseName(locationStr) + "\""));
        return 1;
    }

    private static int reloadMappings(@NonNull CommandContext<FabricClientCommandSource> context) {
        NavMappingsManager.loadMappings();
        context.getSource().sendFeedback(Component.literal("Reloaded mappings: \"" + MappingsIdResolutionUtils.resolveNamespacedId(NavMappingsManager.getMappingsState()) + "\""));
        return 1;
    }

    private static int setBuiltIn(@NonNull CommandContext<FabricClientCommandSource> context) {
        final String id = StringArgumentType.getString(context, "id");
        NavMappingsManager.updateMappingsToBuiltIn(id);
        context.getSource().sendFeedback(Component.literal("Set nav mappings to builtin: " + id));
        return 1;
    }

    private static int setCustom(@NonNull CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        final String id = StringArgumentType.getString(context, "id");
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
        final MappingsState currentMappingState = NavMappingsManager.getMappingsState();
        final String info = MappingsInfoCollectionUtils.getInfoFrom(currentMappingState, true);
        context.getSource().sendFeedback(Component.literal("The currently active mappings are:\n" + info));
        return 1;
    }

    private static int printMappingsList(@NonNull CommandContext<FabricClientCommandSource> context) {
        final String[] options = MappingsInfoCollectionUtils.getMappingsList();
        context.getSource().sendFeedback(Component.literal("The currently available mappings options are:\n" + String.join("\n", options)));
        return 1;
    }

    private static int printCmdDeleteAbout(@NonNull CommandContext<FabricClientCommandSource> context) {
        final String about = "CMD + Delete (modid: " + CmdDeleteClient.MODID
                + ") by Omegabird113 v" + CmdDeleteClient.VERSION
                + " using mappings format version " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION;
        context.getSource().sendFeedback(Component.literal(about));
        return 1;
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

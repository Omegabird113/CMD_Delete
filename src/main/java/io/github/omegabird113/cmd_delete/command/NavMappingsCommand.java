package io.github.omegabird113.cmd_delete.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.KeyNameRegistry;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.ActiveMappingsManager;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.io.FilenameUtils;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.command.CommandCreationUtils.*;

public final class NavMappingsCommand {
    public static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(NavMappingsCommand.class);

    private NavMappingsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> register(dispatcher));
        LOGGER.info("Registered client command \"/navmappings\" through Fabric API");
    }

    private static void register(final @NonNull CommandDispatcher<@NonNull FabricClientCommandSource> dispatcher) {
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
                        .then(literal("dumpDetailedActions").executes(NavMappingsCommand::dumpDetailedActions))
                        .then(literal("dumpFeatureFlags").executes(NavMappingsCommand::dumpFeatureFlags))
                        .then(literal("dumpRegistry").executes(NavMappingsCommand::dumpRegistry))
                        .then(literal("dumpKeymap").executes(NavMappingsCommand::dumpKeyMap))
                        .then(literal("dumpMappingsState").executes(NavMappingsCommand::dumpMappingsState))
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

    private static int dumpRegistry(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final MappingsRegistry mr = NavMappingsManager.getCurrentMappingsRegistry();
        context.getSource().sendFeedback(Component.literal("Registry dump:\n" + mr.toString().replace("\t", "    ")));
        return 1;
    }

    private static int dumpMappingsState(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final MappingsState ms = NavMappingsManager.getOptionalMappingsState().orElse(null);
        if (ms == null)
            context.getSource().sendFeedback(Component.literal("State dump:\nnull"));
        else
            context.getSource().sendFeedback(Component.literal("State dump:\n" + ms.toString().replace("\t", "    ")));
        return 1;
    }

    private static int dumpActions(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final String actionsDump = String.join(", ", Arrays.stream(NavAction.values()).map(NavAction::name).toArray(String[]::new));
        context.getSource().sendFeedback(Component.literal("Actions dump:\n" + actionsDump));
        return 1;
    }

    private static int dumpDetailedActions(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final String actionsDump = NavAction.getDetailedActionDump();
        context.getSource().sendFeedback(Component.literal("Detailed actions dump:\n" + actionsDump));
        return 1;
    }

    private static int dumpFeatureFlags(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("Feature flags dump:\noverrideVanillaNavigation - default false\ncrossLineSignMovement - default true"));
        return 1;
    }

    private static int dumpKeyMap(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("KeyMap dump:\n" + KeyNameRegistry.getDumpString()));
        return 1;
    }

    private static int exportCustomShareCode(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        NavMappingsCommandExecutionUtils.exportShareCode(context, true);
        return 1;
    }

    private static int exportBuiltinShareCode(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        NavMappingsCommandExecutionUtils.exportShareCode(context, false);
        return 1;
    }

    private static int exportBuiltin(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        NavMappingsCommandExecutionUtils.exportMappings(context, false);
        return 1;
    }

    private static int exportCustom(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        NavMappingsCommandExecutionUtils.exportMappings(context, true);
        return 1;
    }

    private static int importCustomShareCode(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        final String shareCode = Minecraft.getInstance().keyboardHandler.getClipboard();
        NavMappingsCommandExecutionUtils.importShareCode(context, shareCode);
        return 1;
    }

    private static int importCustomShareCodeFromChat(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        final String shareCode = StringArgumentType.getString(context, "sharecode");
        NavMappingsCommandExecutionUtils.importShareCode(context, shareCode);
        return 1;
    }

    private static int importCustom(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        final String locationStr = StringArgumentType.getString(context, "location");

        final Path oldPath = Path.of(locationStr);
        final Path newPath = PathConstants.getPathOf(
                MappingsType.CUSTOM,
                FilenameUtils.getBaseName(locationStr));

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

    private static int reloadMappings(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        NavMappingsManager.loadMappings();
        context.getSource().sendFeedback(Component.literal("Reloaded mappings: \"" + MappingsIdResolutionUtils.resolveNamespacedId(NavMappingsManager.getMappingsState()) + "\""));
        return 1;
    }

    private static int setBuiltIn(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        final String id = StringArgumentType.getString(context, "id");
        if (!NavMappingsManager.updateMappingsTo(MappingsType.BUILTIN, id))
            throw UNKNOWN_BUILTIN_MAPPINGS.create(id);
        context.getSource().sendFeedback(Component.literal("Set navmappings to builtin:" + id));
        if (id.equals("emacs_windows_linux") || id.equals("emacs_mac") || id.equals("readline"))
            context.getSource().sendFeedback(Component.literal("WARNING: These mappings are not completely accurate to the conventions of the software they emulate. They do their best to provide similar behaviour to cause less issues with muscle memory, but they do not fully re-work Minecraft to provide the full experience of the control scheme."));
        return 1;
    }

    private static int setCustom(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        final String id = StringArgumentType.getString(context, "id");
        if (!NavMappingsManager.updateMappingsTo(MappingsType.CUSTOM, id))
            throw UNKNOWN_CUSTOM_MAPPINGS.create(id);
        context.getSource().sendFeedback(Component.literal("Set navmappings to custom:" + id));
        return 1;
    }

    private static int setDefault(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) throws CommandSyntaxException {
        if (!NavMappingsManager.updateMappingsTo(MappingsType.DEFAULT, ""))
            throw UNKNOWN_BUILTIN_MAPPINGS.create(ActiveMappingsManager.resolveDefaultMappingsNonNamespacedId());
        context.getSource().sendFeedback(Component.literal("Set navmappings to default"));
        return 1;
    }

    private static int printMappingsInfo(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final MappingsState currentMappingState = NavMappingsManager.getMappingsState();
        final String info = MappingsInfoCollectionUtils.getInfoFrom(currentMappingState, true);
        context.getSource().sendFeedback(Component.literal("The currently active mappings are:\n" + info));
        return 1;
    }

    private static int printMappingsList(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final String[] options = MappingsInfoCollectionUtils.getMappingsList();
        context.getSource().sendFeedback(Component.literal("The currently available mappings options are:\n" + String.join("\n", options)));
        return 1;
    }

    private static int printCmdDeleteAbout(final @NonNull CommandContext<@NonNull FabricClientCommandSource> context) {
        final String about = String.format(
                "CMD + Delete (modid:%s) by Omegabird113 v%s using mappings format version %s (minimum of %s) and sharecode encoding version %s. You can report issues at %s.",
                CmdDeleteClient.MODID,
                CmdDeleteClient.VERSION,
                CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION,
                CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION,
                CmdDeleteClient.SHARECODE_FORMAT_VERSION,
                CmdDeleteClient.ISSUE_TRACKER_URL_STRING
        );
        context.getSource().sendFeedback(Component.literal(about));
        return 1;
    }
}

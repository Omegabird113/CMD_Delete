package io.github.omegabird113.cmd_delete.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class NavMappingsCommand {
    private static final DynamicCommandExceptionType INVALID_OS = new DynamicCommandExceptionType(
            os -> Component.literal("Unknown builtin nav mappings OS: " + os)
    );
    private static final DynamicCommandExceptionType UNKNOWN_CUSTOM_MAPPINGS = new DynamicCommandExceptionType(
            id -> Component.literal("Could not load custom nav mappings: " + id)
    );

    private NavMappingsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("navmappings")
                .then(literal("set")
                        .then(literal("builtin")
                                .then(argument("os", StringArgumentType.word())
                                        .suggests((_, builder) -> {
                                            builder.suggest("mac");
                                            builder.suggest("windows_linux");
                                            return builder.buildFuture();
                                        })
                                        .executes(NavMappingsCommand::setBuiltIn)))
                        .then(literal("custom")
                                .then(argument("id", StringArgumentType.word())
                                        .executes(NavMappingsCommand::setCustom)))
                        .then(literal("default")
                                .executes(NavMappingsCommand::setDefault))));
    }

    private static int setBuiltIn(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String osName = StringArgumentType.getString(context, "os");
        Os os = resolveOs(osName);
        NavMappingsManager.updateMappingsToBuiltIn(os);
        context.getSource().sendFeedback(Component.literal("Set nav mappings to builtin:" + resolveOsId(os)));
        return 1;
    }

    private static int setCustom(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "id");
        if (!NavMappingsManager.updateMappingsToCustom(id)) {
            throw UNKNOWN_CUSTOM_MAPPINGS.create(id);
        }
        context.getSource().sendFeedback(Component.literal("Set nav mappings to custom:" + id));
        return 1;
    }

    private static int setDefault(CommandContext<FabricClientCommandSource> context) {
        NavMappingsManager.updateMappingsToDefault();
        context.getSource().sendFeedback(Component.literal("Set nav mappings to default"));
        return 1;
    }

    private static Os resolveOs(String osName) throws CommandSyntaxException {
        osName = osName.toLowerCase();
        return switch (osName) {
            case "mac" -> Os.MAC;
            case "windows", "windows_linux" -> Os.WINDOWS;
            case "linux" -> Os.LINUX;
            default -> throw INVALID_OS.create(osName);
        };
    }

    private static String resolveOsId(Os os) {
        return switch (os) {
            case MAC -> "mac";
            case WINDOWS, LINUX -> "windows_linux";
        };
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}

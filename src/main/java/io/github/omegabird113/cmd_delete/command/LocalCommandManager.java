package io.github.omegabird113.cmd_delete.command;

import com.mojang.brigadier.CommandDispatcher;

public final class LocalCommandManager {
    public static final CommandDispatcher<LocalCommandSource> DISPATCHER = new CommandDispatcher<>();
    private LocalCommandManager() {}
}
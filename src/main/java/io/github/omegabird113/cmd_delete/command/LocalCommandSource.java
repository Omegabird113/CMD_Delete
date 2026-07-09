package io.github.omegabird113.cmd_delete.command;

import net.minecraft.network.chat.TextComponent;

public interface LocalCommandSource {
    void sendFeedback(TextComponent message);
}

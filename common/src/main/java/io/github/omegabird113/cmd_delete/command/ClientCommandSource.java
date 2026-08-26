package io.github.omegabird113.cmd_delete.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

public final class ClientCommandSource {
    private static BiConsumer<SharedSuggestionProvider, Component> feedback;

    private ClientCommandSource() {
    }

    public static void setFeedback(final @NonNull BiConsumer<SharedSuggestionProvider, Component> feedback) {
        ClientCommandSource.feedback = feedback;
    }

    public static void sendFeedback(final @NonNull SharedSuggestionProvider source,
                                    final @NonNull Component component) {
        final BiConsumer<SharedSuggestionProvider, Component> currentFeedback = feedback;
        if (currentFeedback == null)
            throw new IllegalStateException("Client command feedback was requested before platform initialization");
        currentFeedback.accept(source, component);
    }
}

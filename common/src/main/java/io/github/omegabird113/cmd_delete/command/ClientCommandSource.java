package io.github.omegabird113.cmd_delete.command;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class ClientCommandSource {
    private static BiConsumer<SharedSuggestionProvider, Component> feedback;

    private ClientCommandSource() {
    }

    public static void setFeedback(final @NonNull BiConsumer<SharedSuggestionProvider, Component> feedback) {
        ClientCommandSource.feedback = feedback;
    }

    public static void sendFeedback(final @NonNull SharedSuggestionProvider source,
                                    final @NonNull Component component) {
        if (feedback == null)
            throw new IllegalStateException("Client command feedback was requested before platform initialization");
        feedback.accept(source, component);
    }
}

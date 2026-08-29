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

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class ClientCommandSource {
    private ClientCommandSource() {
    }

    public static void sendFeedback(final @NonNull SharedSuggestionProvider source,
                                    final @NonNull Component component) {
        final BiConsumer<SharedSuggestionProvider, Component> feedback = CmdDeleteClient.getPlatform().getFeedbackMethod();
        if (feedback == null)
            throw new IllegalStateException("Client command feedback was requested before platform initialization or in tests");
        feedback.accept(source, component);
    }
}

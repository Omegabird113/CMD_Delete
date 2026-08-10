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

import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.utils.Os;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    @Contract(pure = true)
    public static @NonNull String getInfoFrom(final @NonNull MappingsState mappingsState, final boolean includeDescription) {
        final double coverage = mappingsState.mappings().getCoverage();

        String displayName = "";
        String description = "";

        final String namespacedId = MappingsIdResolutionUtils.resolveNamespacedId(mappingsState);
        final String version = mappingsState.mappings().registry().version();
        final String author = mappingsState.mappings().registry().author();
        final String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                .map(Os::name)
                .toArray(String[]::new);

        switch (mappingsState.type()) {
            case CUSTOM -> {
                displayName = "\"" + mappingsState.mappings().registry().name() + "\"";
                description = mappingsState.mappings().registry().description();
            }
            case BUILTIN -> {
                displayName = mappingsState.mappings().registry().name();
                description = mappingsState.mappings().registry().description();
            }
            case DEFAULT -> {
                displayName = "Default Mappings (Resolved to " + String.join(" and ", systemStrings) + ")";
                description = "The hard-coded default behaviour to set the mappings to the pre-bundled mappings for the OS of the system when the client is loaded.";
            }
        }

        final MutableComponent baseComponent = Component.translatable(
                "commands.cmd_delete.mappings_info.base",
                Component.literal(displayName),
                Component.literal(namespacedId),
                Component.literal(version),
                Component.literal(author)
        );

        final Component coverageComponent = Component.translatable(
                "commands.cmd_delete.mappings_info.coverage",
                String.format(Locale.ROOT, "%.2f", coverage * 100),
                String.valueOf(mappingsState.mappings().registry().getSize()),
                String.join(" and ", systemStrings)
        );

        final MutableComponent result = baseComponent
                .append("\n")
                .append(coverageComponent);

        if (includeDescription) {
            final Component descriptionComponent = Component.translatable(
                    "commands.cmd_delete.mappings_info.description",
                    description
            );
            result.append("\n").append(descriptionComponent);
        }

        return result.getString();
    }

    @Contract(pure = true)
    public static @NonNull String @NonNull [] getMappingsList() {
        final List<String> internal = new ArrayList<>(
                List.of(
                        "default",
                        "builtin:windows_linux",
                        "builtin:mac",
                        "builtin:emacs_windows_linux",
                        "builtin:emacs_mac",
                        "builtin:readline"
                )
        );
        internal.addAll(MappingsJSONManager.getAvailableOptions(true));
        return internal.toArray(String[]::new);
    }
}

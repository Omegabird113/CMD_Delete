package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.Os;
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
    public static @NonNull String getInfoFrom(@NonNull MappingsState mappingsState, boolean includeDescription) {
        float coverage = mappingsState.mappings().getCoverage();

        String displayName = "";
        String description = "";

        String namespacedId = "\"" + MappingsIdResolutionUtils.resolveNamespacedId(mappingsState) + "\"";
        String version = mappingsState.mappings().registry().getVersion();
        String author = mappingsState.mappings().registry().getAuthor();
        String keyCombinationsString = " with " + mappingsState.mappings().registry().getSize() + " key combinations registered";
        String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                .map(Os::name)
                .toArray(String[]::new);

        switch (mappingsState.type()) {
            case CUSTOM -> {
                displayName = "\"" + mappingsState.mappings().registry().getName() + "\"";
                description = mappingsState.mappings().registry().getDescription();
            }
            case BUILTIN -> {
                displayName = mappingsState.mappings().registry().getName();
                description = mappingsState.mappings().registry().getDescription();
            }
            case DEFAULT -> {
                displayName = "Default Mappings (Resolved to " + String.join(" and ", systemStrings) + ")";
                description = "The hard-coded default behaviour to set the mappings to the pre-bundled mappings for the OS of the system when the client is loaded.";
            }
        }

        String baseString = displayName + " (id: " + namespacedId + ") v" + version + " by " + author;
        String descriptionString = "\nDescription:\n" + description;
        String coverageString = "\nThese mappings have " + String.format(Locale.ROOT, "%.2f", coverage * 100) + "% action coverage" + keyCombinationsString + ".";

        return includeDescription ? baseString + coverageString + descriptionString : baseString + coverageString;
    }

    @Contract(pure = true)
    public static String[] getMappingsList() {
        List<String> internal = new ArrayList<>(
                List.of(
                        "default",
                        "builtin:windows_linux",
                        "builtin:mac"
                )
        );
        internal.addAll(MappingsJSONManager.getAvailableOptions(true));
        return internal.toArray(String[]::new);
    }
}

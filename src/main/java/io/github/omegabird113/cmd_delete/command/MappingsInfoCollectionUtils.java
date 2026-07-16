package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsJSONManager;
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
        final float coverage = mappingsState.mappings().getCoverage();

        String displayName = "";
        String description = "";

        final String namespacedId = "\"" + MappingsIdResolutionUtils.resolveNamespacedId(mappingsState) + "\"";
        final String version = mappingsState.mappings().registry().version();
        final String author = mappingsState.mappings().registry().author();
        final String keyCombinationsString = " with " + mappingsState.mappings().registry().getSize() + " key combinations registered";
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

        final String baseString = displayName + " (id: " + namespacedId + ") v" + version + " by " + author;
        final String descriptionString = "\nDescription:\n" + description;
        final String coverageString = "\nThese mappings have " + String.format(Locale.ROOT, "%.2f", coverage * 100) + "% action coverage" + keyCombinationsString + ".";

        return includeDescription ? baseString + coverageString + descriptionString : baseString + coverageString;
    }

    @Contract(pure = true)
    public static String[] getMappingsList() {
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

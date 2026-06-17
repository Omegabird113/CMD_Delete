package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionUtils;
import io.github.omegabird113.cmd_delete.config.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    public static String getInfoFrom(MappingsState mappingsState, boolean includeDescription) {
        float coverage = NavActionUtils.getCoverage(mappingsState.mappings());

        String namespacedId = "";
        String displayName = "";
        String description = "";
        String version = "";
        String author = "";

        String keyCombinationsString = "";

        switch (mappingsState.type()) {
            case CUSTOM -> {
                namespacedId = "custom:" + mappingsState.mappings().getRegistry().getId();
                displayName = "\"" + mappingsState.mappings().getRegistry().getName() + "\"";
                description = mappingsState.mappings().getRegistry().getDescription();
                version = mappingsState.mappings().getRegistry().getVersion();
                author = mappingsState.mappings().getRegistry().getAuthor();

                keyCombinationsString = " with " + mappingsState.mappings().getRegistry().getSize() + " key combinations registered";
            }
            case BUILTIN -> {
                String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                        .map(Os::name)
                        .toArray(String[]::new);

                namespacedId = "builtin:" + String.join("_", systemStrings).toLowerCase(Locale.ROOT);
                displayName = mappingsState.mappings().getRegistry().getName();
                description = mappingsState.mappings().getRegistry().getDescription();
                version = CmdDeleteClient.VERSION;
                author = "Omegabird113";

                keyCombinationsString = " with " + mappingsState.mappings().getRegistry().getSize() + " key combinations registered";
            }
            case DEFAULT -> {
                String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                        .map(Os::name)
                        .toArray(String[]::new);

                namespacedId = "\"\"";
                displayName = "Default Mappings (Resolved to " + String.join(" and ", systemStrings) + ")";
                description = "The default behaviour to set the mappings to the hard-coded mappings for the OS you're currently using.";
                version = CmdDeleteClient.VERSION;
                author = "Omegabird113";

                keyCombinationsString = " with " + mappingsState.mappings().getRegistry().getSize() + " key combinations registered";
            }
        }

        String baseString = displayName + " (id: " + namespacedId + ") v" + version + " by " + author;
        String descriptionString = "\nDescription:\n" + description;
        String coverageString = "\nThese mappings have " + String.format(Locale.ROOT, "%.2f", coverage * 100) + "% action coverage" + keyCombinationsString + ".";

        return includeDescription ? baseString + coverageString + descriptionString : baseString + coverageString;
    }

    public static String[] getMappingsList() {
        List<String> internal = new ArrayList<>(
                List.of(
                        "default",
                        "builtin:windows_linux",
                        "builtin:mac"
                )
        );
        internal.addAll(MappingsJSONManager.getAvailableOptions());
        return internal.toArray(String[]::new);
    }
}

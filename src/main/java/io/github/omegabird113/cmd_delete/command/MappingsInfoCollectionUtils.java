package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.MappingsState;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    public static String getInfoFrom(MappingsState mappingsState, boolean includeDescription) {
        float coverage = NavActionManager.getCoverage(mappingsState.mappings());

        String namespacedId;
        String displayName;
        String description;
        String version;
        String author;

        String keyCombinationsString = "";

        if (mappingsState.type() == MappingsState.Type.CUSTOM) {
            CustomNavMappings custom = (CustomNavMappings) mappingsState.mappings();

            namespacedId = "custom:" + custom.getRegistry().getFilename();
            displayName = "\"" + custom.getRegistry().getName() + "\"";
            description = custom.getRegistry().getDescription();
            version = custom.getRegistry().getVersion();
            author = custom.getRegistry().getAuthor();

            keyCombinationsString = " with " + custom.getRegistry().getSize() + " key combinations registered";
        } else if (mappingsState.type() == MappingsState.Type.BUILTIN) {
            String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                    .map(Os::name)
                    .toArray(String[]::new);

            namespacedId = "builtin:" + String.join("_", systemStrings).toLowerCase(Locale.ROOT);
            displayName = String.join(" and ", systemStrings) + " mappings";
            description = "Hard-coded mappings for the specified operating system(s).";
            version = CmdDeleteClient.VERSION;
            author = "Omegabird113";
        } else if (mappingsState.type() == MappingsState.Type.DEFAULT) {
            String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                    .map(Os::name)
                    .toArray(String[]::new);

            namespacedId = "\"\"";
            displayName = "Default Mappings (resolved to " + String.join(" and ", systemStrings) + ")";
            description = "The default behaviour to set the mappings to the hard-coded mappings for the OS you're currently using.";
            version = CmdDeleteClient.VERSION;
            author = "Omegabird113";
        } else {
            CmdDeleteClient.LOGGER.error("Unknown mappings object type provided to MappingsInfoCollectionUtils.getInfoFrom(): {}", mappingsState);

            namespacedId = "unknown";
            displayName = "unknown";
            description = "unknown";
            version = "<unknown>";
            author = "unknown";
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
        internal.addAll(CustomMappingsJSONManager.getAvailableOptions());
        return internal.toArray(String[]::new);
    }
}

package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
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
        float coverage = NavActionManager.getCoverage(mappingsState.mappings());

        String namespacedId = "";
        String displayName = "";
        String description = "";
        String version = "";
        String author = "";

        String keyCombinationsString = "";

        switch (mappingsState.type()) {
            case CUSTOM: {
                CustomNavMappings custom = (CustomNavMappings) mappingsState.mappings();

                namespacedId = "custom:" + custom.getRegistry().getFilename();
                displayName = "\"" + custom.getRegistry().getName() + "\"";
                description = custom.getRegistry().getDescription();
                version = custom.getRegistry().getVersion();
                author = custom.getRegistry().getAuthor();

                keyCombinationsString = " with " + custom.getRegistry().getSize() + " key combinations registered";
                break;
            }
            case BUILTIN: {
                String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                        .map(Os::name)
                        .toArray(String[]::new);

                namespacedId = "builtin:" + String.join("_", systemStrings).toLowerCase(Locale.ROOT);
                displayName = String.join(" and ", systemStrings) + " mappings";
                description = "Hard-coded mappings for the specified operating system(s).";
                version = CmdDeleteClient.VERSION;
                author = "Omegabird113";
                break;
            }
            case DEFAULT: {
                String[] systemStrings = Arrays.stream(mappingsState.mappings().getMappingsSupportedSystems())
                        .map(Os::name)
                        .toArray(String[]::new);

                namespacedId = "\"\"";
                displayName = "Default Mappings (Resolved to " + String.join(" and ", systemStrings) + ")";
                description = "The default behaviour to set the mappings to the hard-coded mappings for the OS you're currently using.";
                version = CmdDeleteClient.VERSION;
                author = "Omegabird113";
                break;
            }
        }

        String baseString = displayName + " (id: " + namespacedId + ") v" + version + " by " + author;
        String descriptionString = "\nDescription:\n" + description;
        String coverageString = "\nThese mappings have " + String.format(Locale.ROOT, "%.2f", coverage * 100) + "% action coverage" + keyCombinationsString + ".";

        return includeDescription ? baseString + coverageString + descriptionString : baseString + coverageString;
    }

    public static String[] getMappingsList() {
        List<String> internal = new ArrayList<>();
        internal.add("default");
        internal.add("builtin:windows_linux");
        internal.add("builtin:mac");
        internal.addAll(CustomMappingsJSONManager.getAvailableOptions());
        return internal.toArray(new String[0]);
    }
}

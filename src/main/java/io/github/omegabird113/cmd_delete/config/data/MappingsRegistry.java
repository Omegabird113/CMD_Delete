package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MappingsRegistry(
        @NonNull Map<@NonNull KeyCombo, @NonNull NavAction> internalRegistry,
        @Nullable Map<@NonNull KeyCombo, @NonNull NavAction> internalDisabledRegistry,
        @NonNull List<@NonNull Os> systems,
        @NonNull FeatureFlags featureFlags,
        @NonNull String inherits,
        @NonNull String name,
        @NonNull String author,
        @NonNull String description,
        @NonNull String version,
        @NonNull String id
) {
    public MappingsRegistry {
        internalRegistry = Map.copyOf(internalRegistry);
        internalDisabledRegistry = internalDisabledRegistry == null ? null : Map.copyOf(internalDisabledRegistry);
        systems = List.copyOf(systems);
    }

    public @Nullable NavAction get(@NonNull KeyCombo key) {
        return internalRegistry.get(key);
    }

    public NavAction[] getValues() {
        return internalRegistry.values().toArray(NavAction[]::new);
    }

    public int getSize() {
        return internalRegistry.size();
    }

    @Contract(pure = true)
    private @NonNull String registryStringUtil(@Nullable Map<@NonNull KeyCombo, @NonNull NavAction> registry) {
        if (registry == null)
            return "null";
        if (registry.isEmpty())
            return "{\n\t<empty>\n}";

        final Map<NavAction, ArrayList<KeyCombo>> local = new HashMap<>();
        for (Map.Entry<KeyCombo, NavAction> entry : registry.entrySet())
            local.computeIfAbsent(entry.getValue(), _ -> new ArrayList<>()).add(entry.getKey());

        final ArrayList<String> stringEntries = new ArrayList<>();
        for (Map.Entry<NavAction, ArrayList<KeyCombo>> entry : local.entrySet())
            stringEntries.add("("
                    + String.join(", ", entry.getValue().stream().map(KeyCombo::toString).toList())
                    + " -> "
                    + entry.getKey()
                    + ")"
            );

        return "{\n\t\t" + String.join(",\n\t\t", stringEntries) + "\n\t}";
    }

    @Override
    public @NonNull String toString() {
        return String.format("""
                        MappingsRegistry(
                            name="%s",
                            author="%s",
                            description="%s",
                            version="%s",
                            id="%s",
                            inherits="%s",
                            hashCode=%d,
                            registry=%s,
                            disabledRegistry=%s,
                            featureFlags=%s
                        )""",
                name,
                author,
                description,
                version,
                id,
                inherits,
                hashCode(),
                registryStringUtil(internalRegistry),
                registryStringUtil(internalDisabledRegistry),
                featureFlags);
    }
}
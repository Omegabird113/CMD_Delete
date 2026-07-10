package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MappingsInheritanceManager {
    private MappingsInheritanceManager() {
    }

    @Contract("_ -> new")
    public static @NonNull MappingsRegistry merge(@NonNull List<MappingsRegistry> toMerge) {
        final MappingsRegistry first = toMerge.get(0);
        final Map<KeyCombo, NavAction> firstMap = first.getInternalRegistry();
        final Map<KeyCombo, NavAction> localRegistry = new HashMap<>(firstMap);
        FeatureFlags currentFeatureFlags = first.featureFlags();

        for (int i = 1; i < toMerge.size(); i++) {
            final MappingsRegistry currentRegistry = toMerge.get(i);
            final Optional<Map<KeyCombo, NavAction>> disabledMap = currentRegistry.getInternalDisabledRegistry();
            if (disabledMap.isPresent())
                for (Map.Entry<KeyCombo, NavAction> entry : disabledMap.get().entrySet())
                    localRegistry.remove(entry.getKey(), entry.getValue());
            final Map<KeyCombo, NavAction> enabledMap = currentRegistry.getInternalRegistry();
            localRegistry.putAll(enabledMap);
            currentFeatureFlags = FeatureFlags.merge(currentFeatureFlags, currentRegistry.featureFlags());
        }

        final MappingsRegistry last = toMerge.get(toMerge.size() - 1);

        return new MappingsRegistry(localRegistry, null, last.systems(), currentFeatureFlags, "", last.name(), last.author(), last.description(), last.version(), last.id());
    }
}

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
        MappingsRegistry first = toMerge.getFirst();
        Map<KeyCombo, NavAction> firstMap = first.getInternalRegistry();
        Map<KeyCombo, NavAction> localRegistry = new HashMap<>(firstMap);
        FeatureFlags featureFlags = first.featureFlags();

        for (int i = 1; i < toMerge.size(); i++) {
            MappingsRegistry currentRegistry = toMerge.get(i);
            Optional<Map<KeyCombo, NavAction>> disabledMap = currentRegistry.getInternalDisabledRegistry();
            if (disabledMap.isPresent())
                for (Map.Entry<KeyCombo, NavAction> entry : disabledMap.get().entrySet())
                    localRegistry.remove(entry.getKey(), entry.getValue());
            Map<KeyCombo, NavAction> enabledMap = currentRegistry.getInternalRegistry();
            localRegistry.putAll(enabledMap);
            featureFlags = FeatureFlags.merge(featureFlags, currentRegistry.featureFlags());
        }

        MappingsRegistry last = toMerge.getLast();

        return new MappingsRegistry(localRegistry, null, last.systems(), featureFlags, "", last.name(), last.author(), last.description(), last.version(), last.id());
    }
}

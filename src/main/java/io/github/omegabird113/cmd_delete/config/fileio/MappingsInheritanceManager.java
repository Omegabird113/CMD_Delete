package io.github.omegabird113.cmd_delete.config.fileio;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
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
        final MappingsRegistry first = toMerge.getFirst();
        final Map<KeyCombo, NavAction> firstMap = first.internalRegistry();
        final Map<KeyCombo, NavAction> localRegistry = new HashMap<>(firstMap);
        FeatureFlags currentFeatureFlags = first.featureFlags();

        for (int i = 1; i < toMerge.size(); i++) {
            final MappingsRegistry currentRegistry = toMerge.get(i);
            final Optional<Map<KeyCombo, NavAction>> disabledMap = Optional.ofNullable(currentRegistry.internalDisabledRegistry());
            if (disabledMap.isPresent())
                for (Map.Entry<KeyCombo, NavAction> entry : disabledMap.get().entrySet())
                    localRegistry.remove(entry.getKey(), entry.getValue());
            final Map<KeyCombo, NavAction> enabledMap = currentRegistry.internalRegistry();
            localRegistry.putAll(enabledMap);
            currentFeatureFlags = FeatureFlags.merge(currentFeatureFlags, currentRegistry.featureFlags());
        }

        final MappingsRegistry last = toMerge.getLast();

        return new MappingsRegistry(localRegistry, null, last.systems(), currentFeatureFlags, "", last.name(), last.author(), last.description(), last.version(), last.id());
    }
}

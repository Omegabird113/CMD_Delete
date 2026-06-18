package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;

import java.util.*;

public final class MappingsInheritanceManager {
    private MappingsInheritanceManager() {}

    public static MappingsRegistry merge(Chain chain) {
        List<MappingsRegistry> toMerge = List.copyOf(chain.registries());

        MappingsRegistry first = toMerge.getFirst();
        Map<KeyCombo, NavAction> firstMap = first.getInternalRegistry();
        Map<KeyCombo, NavAction> localRegistry = new HashMap<>(firstMap);

        for (int i = 1; i < toMerge.size(); i++) {
            MappingsRegistry currentRegistry = toMerge.get(i);
            Optional<Map<KeyCombo, NavAction>> disabledMap = currentRegistry.getInternalDisabledRegistry();
            if (disabledMap.isPresent())
                for (Map.Entry<KeyCombo, NavAction> entry : disabledMap.get().entrySet())
                    localRegistry.remove(entry.getKey(), entry.getValue());
            Map<KeyCombo, NavAction> enabledMap = currentRegistry.getInternalRegistry();
            localRegistry.putAll(enabledMap);
        }

        MappingsRegistry last = toMerge.getLast();

        return new MappingsRegistry(localRegistry, last.getSystems(), "", last.getName(), last.getAuthor(), last.getDescription(), last.getVersion(), last.getId());
    }

    public static Chain constructChain(Collection<MappingsRegistry> toMerge) {
        List<String> ids = new ArrayList<>();
        for (MappingsRegistry currentRegistry : toMerge)
            ids.add(currentRegistry.getId());
        return new Chain(new LinkedHashSet<>(ids), new LinkedHashSet<>(toMerge));
    }

    public record Chain (LinkedHashSet<String> ids, LinkedHashSet<MappingsRegistry> registries) {
    }
}

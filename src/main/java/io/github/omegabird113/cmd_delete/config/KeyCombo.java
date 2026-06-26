package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.stream.Collectors;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {

    private static final @NonNull Map<String, Integer> keyMap = KeyCodeRegistry.getKeyMap();
    private static final @NonNull Map<Integer, String> reversedKeyMap = keyMap.entrySet().stream().collect(
            Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));
    private static final boolean isMac = Os.getCurrent() == Os.MAC;

    @Override
    public @NonNull String toString() {
        return "<" +
                (control ? "ctrl+" : "")
                + (superCommand ? (isMac ? "cmd+" : "sup+") : "")
                + (altOption ? (isMac ? "opt+" : "alt+") : "")
                + (shift ? "shift+" : "")
                + (reversedKeyMap.get(key) != null ? reversedKeyMap.get(key) : key)
                + ">";
    }
}

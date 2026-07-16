package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.stream.Collectors;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {
    private static final @NonNull Map<String, Integer> KEY_MAP = KeyCodeRegistry.getKeyMap();
    private static final @NonNull Map<Integer, String> REVERSED_KEY_MAP = KEY_MAP.entrySet().stream().collect(
            Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));
    private static final boolean IS_MAC = Os.USING == Os.MAC;

    @Override
    public @NonNull String toString() {
        return "<" +
                (control ? "ctrl+" : "")
                + (superCommand ? (IS_MAC ? "cmd+" : "sup+") : "")
                + (altOption ? (IS_MAC ? "opt+" : "alt+") : "")
                + (shift ? "shift+" : "")
                + (REVERSED_KEY_MAP.get(key) != null ? REVERSED_KEY_MAP.get(key) : key)
                + ">";
    }
}

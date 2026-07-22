package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {
    @Override
    public @NonNull String toString() {
        final Map<Integer, String> reversedKeyMap = KeyNameRegistry.getReverseKeyMap();
        return "<" +
                (control ? "ctrl+" : "")
                + (superCommand ? (Os.IS_USING_MAC ? "cmd+" : "sup+") : "")
                + (altOption ? (Os.IS_USING_MAC ? "opt+" : "alt+") : "")
                + (shift ? "shift+" : "")
                + (reversedKeyMap.getOrDefault(key, Integer.toString(key)))
                + ">";
    }
}

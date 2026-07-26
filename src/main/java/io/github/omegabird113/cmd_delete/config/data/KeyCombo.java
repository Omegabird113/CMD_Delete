package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.utils.Os;
import org.jspecify.annotations.NonNull;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {
    @Override
    public @NonNull String toString() {
        return "<" +
                (control ? "ctrl+" : "")
                + (superCommand ? (Os.IS_USING_MAC ? "cmd+" : "sup+") : "")
                + (altOption ? (Os.IS_USING_MAC ? "opt+" : "alt+") : "")
                + (shift ? "shift+" : "")
                + (KeyNameRegistry.getReverseKeyMap().getOrDefault(key, Integer.toString(key)))
                + ">";
    }
}

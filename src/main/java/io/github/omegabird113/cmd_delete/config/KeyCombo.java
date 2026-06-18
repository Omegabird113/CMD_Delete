package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {

    @Override
    public @NonNull String toString() {
        boolean isMac = Os.getCurrent() == Os.MAC;
        return (control ? "ctrl+" : "")
                + (superCommand ? (isMac ? "cmd+": "sup+") : "")
                + (altOption ?  (isMac ? "opt+": "alt+") : "")
                + (shift ? "shift+" : "") +
                key;
    }
}

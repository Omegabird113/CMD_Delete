package io.github.omegabird113.cmd_delete.config;

import org.jspecify.annotations.NonNull;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {

    @Override
    public @NonNull String toString() {
        return key
                + (control ? "+ctrl" : "")
                + (superCommand ? "+sup/cmd" : "")
                + (altOption ? "+alt/opt" : "")
                + (shift ? "+shift" : "");
    }
}

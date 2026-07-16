package io.github.omegabird113.cmd_delete.config.data;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record FeatureFlags(Boolean overrideVanillaNavigation,
                           Boolean crossLineSignMovement) {
    @Contract("_, _ -> new")
    public static @NonNull FeatureFlags merge(@NonNull FeatureFlags parent, @NonNull FeatureFlags child) {
        return new FeatureFlags(
                child.overrideVanillaNavigation() != null ? child.overrideVanillaNavigation() : parent.overrideVanillaNavigation(),
                child.crossLineSignMovement() != null ? child.crossLineSignMovement() : parent.crossLineSignMovement()
        );
    }
}

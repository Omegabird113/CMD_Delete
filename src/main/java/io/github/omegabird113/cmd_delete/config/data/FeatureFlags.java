package io.github.omegabird113.cmd_delete.config.data;

public record FeatureFlags(Boolean overrideVanillaNavigation,
                           Boolean crossLineSignMovement) {
    public static FeatureFlags merge(FeatureFlags parent, FeatureFlags child) {
        return new FeatureFlags(
                child.overrideVanillaNavigation() != null ? child.overrideVanillaNavigation() : parent.overrideVanillaNavigation(),
                child.crossLineSignMovement() != null ? child.crossLineSignMovement() : parent.crossLineSignMovement()
        );
    }
}

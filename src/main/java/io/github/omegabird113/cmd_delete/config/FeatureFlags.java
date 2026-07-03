package io.github.omegabird113.cmd_delete.config;

public record FeatureFlags(Boolean overrideVanillaNavigation,
                           Boolean crossLineSignMovement) {
     static FeatureFlags merge(FeatureFlags parent, FeatureFlags child) {
        boolean overrideVanillaNavigation;
        boolean crossLineSignMovement;

        if (child.overrideVanillaNavigation == null)
            overrideVanillaNavigation = parent.overrideVanillaNavigation;
        else
            overrideVanillaNavigation = child.overrideVanillaNavigation;

        if (child.crossLineSignMovement == null)
            crossLineSignMovement = parent.crossLineSignMovement;
        else
            crossLineSignMovement = child.crossLineSignMovement;

        return new FeatureFlags(overrideVanillaNavigation, crossLineSignMovement);
    }
}

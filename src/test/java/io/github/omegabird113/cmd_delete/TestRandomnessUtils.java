package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestRandomnessUtils {
    static final @NonNull Random RANDOM = new Random();

    private static @Nullable Boolean nextRandNullableBoolean() {
        int choose = RANDOM.nextInt(0, 3);
        if (choose == 0)
            return null;
        if (choose == 1)
            return Boolean.FALSE;
        if (choose == 2)
            return Boolean.TRUE;
        return null;
    }

    static FeatureFlags nextRandFeatureFlags() {
        final Boolean overrideVanillaNavigation = nextRandNullableBoolean();
        final Boolean crossLineSignMovement = nextRandNullableBoolean();
        return new FeatureFlags(overrideVanillaNavigation, crossLineSignMovement);
    }

    private static KeyCombo genRandomKeyCombo() {
        final boolean shift = RANDOM.nextBoolean();
        final boolean ctrl = RANDOM.nextBoolean();
        final boolean altOption = RANDOM.nextBoolean();
        final boolean superCommand = RANDOM.nextBoolean();
        final int key = RANDOM.nextInt(0, 16384);
        return new KeyCombo(key, shift, altOption, ctrl, superCommand);
    }

    private static String genRandomString(int length) {
        final byte[] strBytes = new byte[length];
        RANDOM.nextBytes(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }

    private static List<Os> genSystems() {
        final boolean windows = RANDOM.nextBoolean();
        final boolean linux = RANDOM.nextBoolean();
        final boolean mac = RANDOM.nextBoolean();
        final List<Os> systems = new ArrayList<>();
        if (windows)
            systems.add(Os.WINDOWS);
        if (linux)
            systems.add(Os.LINUX);
        if (mac)
            systems.add(Os.MAC);
        return systems;
    }

    static MappingsRegistry genRandomRegistry() {
        final Map<KeyCombo, NavAction> enabled = new HashMap<>();
        final Map<KeyCombo, NavAction> disabled = new HashMap<>();
        final NavAction[] NavActions = Arrays.stream(NavAction.values()).toArray(NavAction[]::new);

        final int n1 = RANDOM.nextInt(30, 1001);
        for (int i = 0; i < n1; i++) {
            KeyCombo key = genRandomKeyCombo();
            NavAction na = NavActions[RANDOM.nextInt(0, NavActions.length)];
            enabled.put(key, na);
        }

        final int n2 = RANDOM.nextInt(30, 1001);
        for (int i = 0; i < n2; i++) {
            KeyCombo key = genRandomKeyCombo();
            NavAction na = NavActions[RANDOM.nextInt(0, NavActions.length)];
            disabled.put(key, na);
        }

        final FeatureFlags ff = nextRandFeatureFlags();
        final List<Os> systems = genSystems();

        final String name = genRandomString(RANDOM.nextInt(8, 25));
        final String description = genRandomString(RANDOM.nextInt(50, 1001));
        final String id = genRandomString(RANDOM.nextInt(6, 15));
        final String author = genRandomString(RANDOM.nextInt(7, 35));
        final String inherits = genRandomString(RANDOM.nextInt(6, 15));
        final String version = "" + RANDOM.nextLong(0, Long.MAX_VALUE);

        return new MappingsRegistry(enabled, disabled, systems, ff, inherits, name, author, description, version, id);
    }
}

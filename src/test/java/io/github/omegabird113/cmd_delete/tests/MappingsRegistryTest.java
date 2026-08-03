package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.KeyNameRegistry;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.utils.Os;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingsRegistryTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void basicRegistryBehaviorExampleTest() {
        final int aKey = KeyNameRegistry.getKeyMap().get("a");
        final int bKey = KeyNameRegistry.getKeyMap().get("b");

        final KeyCombo k1 = new KeyCombo(aKey, false, false, false, false);
        final KeyCombo k2 = new KeyCombo(bKey, true, false, false, false);

        final Map<KeyCombo, NavAction> registry = new HashMap<>();
        registry.put(k1, NavAction.NAV_LINE_LEFT);
        registry.put(k2, NavAction.SEL_WORD_RIGHT);

        final MappingsRegistry mr = new MappingsRegistry(registry, null, List.of(Os.LINUX), new FeatureFlags(true, false), "", "TestName", "Author", "Description", "1.0", "testid");

        Assertions.assertEquals(NavAction.NAV_LINE_LEFT, mr.get(k1));
        Assertions.assertEquals(2, mr.getSize());

        NavAction[] values = mr.getValues();
        Assertions.assertEquals(2, values.length);
        Assertions.assertTrue(List.of(values).contains(NavAction.NAV_LINE_LEFT));
        Assertions.assertTrue(List.of(values).contains(NavAction.SEL_WORD_RIGHT));

        final String s = mr.toString();
        Assertions.assertTrue(s.contains("TestName"));
        Assertions.assertTrue(s.contains("testid"));
        Assertions.assertTrue(s.contains("disabledRegistry=null"), "Disabled registry should be represented as null in toString");

        Assertions.assertTrue(s.contains(k1.toString()) || s.contains(k2.toString()));
    }
}


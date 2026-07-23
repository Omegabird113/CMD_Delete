package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MappingsLoadTests {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    @Order(1)
    void mappingsLoadTest() {
        NavMappingsManager.loadMappings();
        Assertions.assertDoesNotThrow(() -> {
            NavMappingsManager.getMappingsState();
        });
    }

    @Test
    @Order(2)
    void allBuiltinMappingsLoadTest() {
        MappingsState lastState = NavMappingsManager.getMappingsState();
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default") || namespacedId.startsWith(MappingsType.CUSTOM.prefix))
                continue;
            final String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);
            final MappingsType mappingsType = id.isEmpty() ? MappingsType.DEFAULT : MappingsIdResolutionUtils.resolveType(namespacedId);
            switch (mappingsType) {
                case CUSTOM -> Assertions.assertTrue(NavMappingsManager.updateMappingsToCustom(id));
                case BUILTIN -> Assertions.assertTrue(NavMappingsManager.updateMappingsToBuiltIn(id));
            }
            final MappingsState current = NavMappingsManager.getMappingsState();
            Assertions.assertNotEquals(lastState, current, () -> "Mappings failed to load: " + namespacedId);
            lastState = current;
        }
    }

    @Test
    @Order(3)
    void sampleLoadTest() {
        final MappingsState before = NavMappingsManager.getMappingsState();
        boolean success = NavMappingsManager.updateMappingsToCustom("sample");
        final MappingsState after = NavMappingsManager.getMappingsState();
        Assertions.assertNotEquals(before, after, "sample mappings failed to load");
        Assertions.assertTrue(success);
    }

    @Test
    @Order(4)
    void switchToDefaultMappingsTest() {
        final MappingsState before = NavMappingsManager.getMappingsState();
        Assertions.assertDoesNotThrow(NavMappingsManager::updateMappingsToDefault);
        final MappingsState after = NavMappingsManager.getMappingsState();
        Assertions.assertNotEquals(before, after, "Default mappings failed to load");
    }
}

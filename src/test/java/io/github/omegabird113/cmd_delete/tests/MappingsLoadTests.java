/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        final MappingsState state = NavMappingsManager.getMappingsState();
        Assertions.assertNotNull(state);
        Assertions.assertNotNull(state.mappings());
        Assertions.assertNotNull(state.type());
        Assertions.assertNotNull(state.id());
        if (state.type() == MappingsType.DEFAULT)
            Assertions.assertTrue(state.id().isEmpty());
        else
            Assertions.assertFalse(state.id().isBlank());
    }

    @Test
    @Order(2)
    void allBuiltinMappingsLoadTest() {
        MappingsState lastState = NavMappingsManager.getMappingsState();
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default") || namespacedId.startsWith(MappingsType.CUSTOM.prefix()))
                continue;
            final String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);
            final MappingsType mappingsType = id.isEmpty() ? MappingsType.DEFAULT : MappingsIdResolutionUtils.resolveType(namespacedId);
            Assertions.assertTrue(() -> NavMappingsManager.updateMappingsTo(mappingsType, id));
            final MappingsState current = NavMappingsManager.getMappingsState();
            Assertions.assertNotEquals(lastState, current, () -> "Mappings failed to load: " + namespacedId);
            Assertions.assertEquals(mappingsType, current.type());
            Assertions.assertEquals(id, current.id());
            lastState = current;
        }
    }

    @Test
    @Order(3)
    void sampleLoadTest() {
        final MappingsState before = NavMappingsManager.getMappingsState();
        boolean success = NavMappingsManager.updateMappingsTo(MappingsType.CUSTOM, "sample");
        final MappingsState after = NavMappingsManager.getMappingsState();
        Assertions.assertNotEquals(before, after, "sample mappings failed to load");
        Assertions.assertEquals(MappingsType.CUSTOM, after.type());
        Assertions.assertEquals("sample", after.id());
        Assertions.assertTrue(success);
    }

    @Test
    @Order(4)
    void switchToDefaultMappingsTest() {
        Assertions.assertTrue(() -> NavMappingsManager.updateMappingsTo(MappingsType.DEFAULT, ""));
    }
}

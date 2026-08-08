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
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MappingsInfoCollectionUtilsTests {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
        NavMappingsManager.loadMappings();
    }

    @Test
    void mappingsListContainsExpectedOptionsTest() {
        final List<String> mappings = Arrays.asList(MappingsInfoCollectionUtils.getMappingsList());

        Assertions.assertAll(
                () -> Assertions.assertTrue(mappings.contains("default")),
                () -> Assertions.assertTrue(mappings.contains("builtin:windows_linux")),
                () -> Assertions.assertTrue(mappings.contains("builtin:mac")),
                () -> Assertions.assertTrue(mappings.contains("builtin:emacs_windows_linux")),
                () -> Assertions.assertTrue(mappings.contains("builtin:emacs_mac")),
                () -> Assertions.assertTrue(mappings.contains("builtin:readline")),
                () -> Assertions.assertTrue(mappings.contains("custom:sample")),
                () -> Assertions.assertTrue(mappings.contains("custom:inherited")),
                () -> Assertions.assertEquals(mappings.size(), new HashSet<>(mappings).size())
        );
    }

    @Test
    void infoStringFormattingTest() {
        final String withDescription = MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), true);
        final String withoutDescription = MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), false);

        Assertions.assertAll(
                () -> Assertions.assertFalse(withDescription.isBlank()),
                () -> Assertions.assertFalse(withoutDescription.isBlank()),
                () -> Assertions.assertTrue(withDescription.contains("Description:")),
                () -> Assertions.assertFalse(withoutDescription.contains("Description:")),
                () -> Assertions.assertTrue(withDescription.contains(NavMappingsManager.getMappingsState().id())),
                () -> Assertions.assertTrue(withoutDescription.contains(NavMappingsManager.getMappingsState().id()))
        );
    }
}

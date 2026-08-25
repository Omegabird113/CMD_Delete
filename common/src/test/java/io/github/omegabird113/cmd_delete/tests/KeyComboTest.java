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
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.KeyNameRegistry;
import io.github.omegabird113.cmd_delete.utils.Os;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class KeyComboTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void mapKeyExampleEqualityAndToStringTest() {
        final int leftKey = KeyNameRegistry.getKeyMap().get("left");

        final KeyCombo kc = new KeyCombo(leftKey, true, true, true, true);
        final String s = kc.toString();

        Assertions.assertTrue(s.startsWith("<") && s.endsWith(">"), "KeyCombo string should be wrapped in <>");

        Assertions.assertTrue(s.contains("ctrl+"), "Control modifier should be present");
        Assertions.assertTrue(s.contains("shift+"), "Shift modifier should be present");

        if (Os.IS_USING_MAC) {
            Assertions.assertTrue(s.contains("cmd+"), "On mac, superCommand should appear as cmd+");
            Assertions.assertTrue(s.contains("opt+"), "On mac, altOption should appear as opt+");
        } else {
            Assertions.assertTrue(s.contains("sup+"), "On non-mac, superCommand should appear as sup+");
            Assertions.assertTrue(s.contains("alt+"), "On non-mac, altOption should appear as alt+");
        }

        final String expectedKeyName = KeyNameRegistry.getReverseKeyMap().get(leftKey);
        Assertions.assertNotNull(expectedKeyName);
        Assertions.assertTrue(s.contains(expectedKeyName), () -> "Expected key name " + expectedKeyName + " in " + s);

        final KeyCombo kc2 = new KeyCombo(leftKey, true, true, true, true);
        Map<KeyCombo, String> mmap = new HashMap<>();
        mmap.put(kc, "value");
        Assertions.assertEquals("value", mmap.get(kc2), "KeyCombo equals/hashCode should allow lookup with an equal instance");

        final KeyCombo different = new KeyCombo(leftKey + 1, true, true, true, true);
        Assertions.assertNull(mmap.get(different), "Different KeyCombo should not be present in map");
    }
}



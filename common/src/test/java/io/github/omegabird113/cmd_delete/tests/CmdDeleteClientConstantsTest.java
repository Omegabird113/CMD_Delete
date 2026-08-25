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

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.TestLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CmdDeleteClientConstantsTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void clientConstantsTest() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("cmd_delete", CmdDeleteClient.MODID),
                () -> Assertions.assertTrue(CmdDeleteClient.ISSUE_TRACKER_URL_STRING.startsWith("https://github.com/")),
                () -> Assertions.assertTrue(CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION >= CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION),
                () -> Assertions.assertNotNull(CmdDeleteClient.VERSION),
                () -> Assertions.assertFalse(CmdDeleteClient.VERSION.isBlank()),
                () -> Assertions.assertNotEquals("<unknown>", CmdDeleteClient.VERSION)
        );
    }
}

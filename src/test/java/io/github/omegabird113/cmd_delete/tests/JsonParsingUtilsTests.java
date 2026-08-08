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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.config.fileio.JsonParsingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonParsingUtilsTests {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void requireFileSafeStringAcceptsPlainIds() {
        final JsonObject json = new JsonObject();
        json.addProperty("id", "sample_mapping");

        Assertions.assertEquals("sample_mapping", JsonParsingUtils.requireFilenameSafeString(json, "id"));
    }

    @Test
    void requireFileSafeStringRejectsPathSeparators() {
        final JsonObject json = new JsonObject();
        json.addProperty("id", "../sample");

        Assertions.assertThrows(JsonParseException.class, () -> JsonParsingUtils.requireFilenameSafeString(json, "id"));
    }
}

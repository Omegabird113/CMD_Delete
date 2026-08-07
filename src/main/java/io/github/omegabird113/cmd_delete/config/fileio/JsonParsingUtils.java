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

package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.config.data.KeyNameRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public final class JsonParsingUtils {
    private JsonParsingUtils() {
    }

    @Contract(pure = true)
    public static @NonNull String getStringElse(final @NonNull JsonObject parent, final @NonNull String fieldName, final @NonNull String defaultValue) {
        if (!parent.has(fieldName))
            return defaultValue;
        final String value = requireString(parent, fieldName).trim();
        return value.isEmpty() ? defaultValue : value;
    }

    public static @NonNull JsonObject requireObject(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);
        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonObject())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an object");
        return element.getAsJsonObject();
    }

    public static @NonNull JsonArray requireArray(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);
        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonArray())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an array");
        return element.getAsJsonArray();
    }

    public static @NonNull String requireString(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);
        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string");
        return element.getAsString();
    }

    public static @NonNull String requireFileSafeString(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        final String value = requireString(parent, fieldName);
        if (value.indexOf('/') >= 0 || value.indexOf('\\') >= 0)
            throw new JsonParseException("Expected \"" + fieldName + "\" to not contain path separators");
        if (value.indexOf('\u0000') >= 0)
            throw new JsonParseException("Expected \"" + fieldName + "\" to not contain NUL characters");
        return value;
    }

    @Contract(pure = true)
    public static boolean getOptionalBoolean(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        if (!parent.has(fieldName))
            return false;
        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a boolean");
        return element.getAsBoolean();
    }

    @Contract(pure = true)
    public static @Nullable Boolean getNullableBoolean(final @NonNull JsonObject parent, final @NonNull String fieldName) {
        if (!parent.has(fieldName))
            return null;
        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            return null;
        return element.getAsBoolean();
    }

    public static int requireInt(final @NonNull JsonObject parent, final @NonNull String fieldName, final boolean strictMode) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a number");

        final String s = element.getAsString();

        if (strictMode)
            if (!s.matches("-?(0|[1-9]\\d*)"))
                throw new JsonParseException("Expected \"" + fieldName + "\" to be an integer literal");

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an integer");
        }
    }

    public static int requireKeyCode(final @NonNull JsonObject parent, final @NonNull String fieldName, final boolean strictMode) throws JsonParseException {
        final Map<String, Integer> keyMap = KeyNameRegistry.getKeyMap();

        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || (!element.getAsJsonPrimitive().isString() && !element.getAsJsonPrimitive().isNumber()))
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string or a number");

        final String keyString = element.getAsString().toLowerCase(Locale.ROOT).trim();

        if (element.getAsJsonPrimitive().isString()) {
            if (keyString.equals("f25"))
                MappingsJSONDeserializer.logWarn(
                        "The deprecated friendly keyname \"f25\" was used. This keyname will not exist in fv5",
                        strictMode
                );
            final Integer keyCode = keyMap.get(keyString);
            if (keyCode == null)
                throw new JsonParseException("Unknown key \"" + keyString + "\".");
            else
                return keyCode;
        } else
            try {
                if (strictMode)
                    if (!keyString.matches("-?(0|[1-9]\\d*)"))
                        throw new JsonParseException(
                                "Expected \"" + fieldName + "\" to be an integer literal");

                return Integer.parseInt(keyString);
            } catch (NumberFormatException e) {
                throw new JsonParseException("Expected \"" + fieldName + "\" to be a string or an integer");
            }
    }

    public static int requireFv(final @NonNull JsonObject parent) {
        final int fv = requireInt(parent, "fv", true); // we don't know fv/strict so always use it
        if (fv < CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION || fv > CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION)
            throw new JsonParseException("Invalid format version number: " + fv + ". The current format version is: " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION);
        if (fv != CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION)
            MappingsJSONDeserializer.logWarn(
                    "Old mappings version (" + fv + ") used by custom mappings. Please update to version " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION,
                    false
            );
        return fv;
    }
}

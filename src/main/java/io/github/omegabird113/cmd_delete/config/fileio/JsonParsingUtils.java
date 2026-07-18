package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
    public static String getStringElse(@NonNull JsonObject parent, @NonNull String fieldName, @NonNull String defaultValue) {
        if (!parent.has(fieldName))
            return defaultValue;
        final String value = requireString(parent, fieldName).trim();
        return value.isEmpty() ? defaultValue : value;
    }

    public static JsonObject requireObject(@NonNull JsonObject parent, @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonObject())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an object");

        return element.getAsJsonObject();
    }

    public static JsonArray requireArray(@NonNull JsonObject parent, @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonArray())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an array");

        return element.getAsJsonArray();
    }

    public static String requireString(@NonNull JsonObject parent, @NonNull String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string");

        return element.getAsString();
    }

    public static boolean getOptionalBoolean(@NonNull JsonObject parent, @NonNull String fieldName) {
        if (!parent.has(fieldName))
            return false;

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a boolean");

        return element.getAsBoolean();
    }

    @Contract(pure = true)
    public static @Nullable Boolean getNullableBoolean(@NonNull JsonObject parent, @NonNull String fieldName) {
        if (!parent.has(fieldName))
            return null;

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            return null;

        return element.getAsBoolean();
    }

    public static int requireInt(@NonNull JsonObject parent, @NonNull String fieldName, boolean strictMode, int fv) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a number");

        final String s = element.getAsString();

        if (strictMode && fv == 4)
            if (!s.matches("-?(0|[1-9]\\d*)"))
                throw new JsonParseException("Expected \"" + fieldName + "\" to be an integer literal");

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an integer");
        }
    }

    public static int requireKeyCode(@NonNull JsonObject parent, @NonNull String fieldName, boolean strictMode, int fv) throws JsonParseException {
        final Map<String, Integer> keyMap = KeyNameRegistry.getKeyMap();

        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        final JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || (!element.getAsJsonPrimitive().isString() && !element.getAsJsonPrimitive().isNumber()))
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string or a number");

        final String keyString = element.getAsString().toLowerCase(Locale.ROOT).trim();

        if (element.getAsJsonPrimitive().isString()) {
            final Integer keyCode = keyMap.get(keyString);
            if (keyCode == null)
                throw new JsonParseException("Unknown key \"" + keyString + "\".");
            else
                return keyCode;
        } else
            try {
                if (strictMode && fv == 4)
                    if (!keyString.matches("-?(0|[1-9]\\d*)"))
                        throw new JsonParseException(
                                "Expected \"" + fieldName + "\" to be an integer literal");

                return Integer.parseInt(keyString);
            } catch (NumberFormatException e) {
                throw new JsonParseException("Expected \"" + fieldName + "\" to be a string or an integer");
            }
    }
}

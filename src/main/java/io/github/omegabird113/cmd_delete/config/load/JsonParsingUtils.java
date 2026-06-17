package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

final class JsonParsingUtils {
    private JsonParsingUtils() {
    }

    public static String getStringElse(JsonObject parent, String fieldName, String defaultValue) {
        if (!parent.has(fieldName))
            return defaultValue;
        String value = requireString(parent, fieldName).trim();
        return value.isEmpty() ? defaultValue : value;
    }

    public static JsonObject requireObject(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonObject())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an object");

        return element.getAsJsonObject();
    }

    public static JsonArray requireArray(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonArray())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an array");

        return element.getAsJsonArray();
    }

    public static String requireString(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string");

        return element.getAsString();
    }

    public static boolean getOptionalBoolean(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            return false;

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a boolean");

        return element.getAsBoolean();
    }

    public static int requireInt(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a number");

        String s = element.getAsString();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an integer");
        }
    }
}

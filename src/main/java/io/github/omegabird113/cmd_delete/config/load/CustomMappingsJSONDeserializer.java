package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;
import io.github.omegabird113.cmd_delete.config.registry.KeyCodeRegistry;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.lang.reflect.Type;
import java.util.*;

public class CustomMappingsJSONDeserializer implements JsonDeserializer<CustomMappingsRegistry> {
    @Override
    public CustomMappingsRegistry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Expected a JSON object at root");
        JsonObject jsonObject = json.getAsJsonObject();
        CustomMappingsRegistry registry = new CustomMappingsRegistry();

        int fv = requireInt(jsonObject, "fv");
        if (fv != 1)
            throw new JsonParseException("Invalid format version number: " + fv);

        parseMeta(requireObject(jsonObject, "meta"), registry);

        JsonObject actions = requireObject(jsonObject, "actions");

        Map<String, NavAction> actionMap = getNavActionNameMap();
        Map<String, Integer> keyMap = KeyCodeRegistry.getKeyMap();
        Set<CustomMappingsRegistryKey> registeredKeys = new HashSet<>();

        for (String actionName : actions.keySet()) {
            NavAction action = actionMap.get(actionName.trim().toUpperCase(Locale.ROOT));
            if (action == null || action == NavAction.NONE)
                continue;

            JsonArray bindings = requireArray(actions, actionName);

            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException(
                            "Expected each binding for action \"" + actionName + "\" to be an object");

                JsonObject binding = bindingElement.getAsJsonObject();

                String keyName = requireString(binding, "key").trim().toLowerCase(Locale.ROOT);

                Integer keyCode = keyMap.get(keyName);
                if (keyCode == null)
                    throw new JsonParseException("Unknown key name \"" + keyName + "\" in action \"" + actionName + "\"");

                boolean hasShift = binding.has("shift");
                boolean shiftValue = getOptionalBoolean(binding, "shift");

                boolean hasAltOption = binding.has("altOption");
                boolean altOptionValue = getOptionalBoolean(binding, "altOption");

                boolean hasControl = binding.has("control");
                boolean controlValue = getOptionalBoolean(binding, "control");

                boolean hasSuperCommand = binding.has("superCommand");
                boolean superCommandValue = getOptionalBoolean(binding, "superCommand");

                List<CustomMappingsRegistryKey> keys = expandKeyWildcards(
                        keyCode,
                        hasShift, shiftValue,
                        hasAltOption, altOptionValue,
                        hasControl, controlValue,
                        hasSuperCommand, superCommandValue
                );

                for (CustomMappingsRegistryKey key : keys) {
                    if (registeredKeys.contains(key)) {
                        CmdDeleteClient.LOGGER.warn("Duplicate key binding in custom action \"{}\": {}", actionName, keyName);
                        continue;
                    }
                    registeredKeys.add(key);
                    registry.put(key, action);
                }
            }
        }

        return registry;
    }

    private Map<String, NavAction> getNavActionNameMap() {
        Map<String, NavAction> map = new HashMap<>();
        for (NavAction action : NavAction.values())
            map.put(action.name(), action);
        return map;
    }

    private Map<String, Os> getOsNameMap() {
        Map<String, Os> osMap = new HashMap<>();
        osMap.put("windows", Os.WINDOWS);
        osMap.put("mac", Os.MAC);
        osMap.put("linux", Os.LINUX);
        return osMap;
    }

    private void parseMeta(JsonObject meta, CustomMappingsRegistry registry) {
        registry.setName(getStringElse(meta, "name", "Unnamed Custom Mappings"));
        registry.setAuthor(getStringElse(meta, "author", "unknown"));
        registry.setDescription(getStringElse(meta, "description", "No description provided"));
        registry.setVersion(getStringElse(meta, "version", "unknown"));

        if (meta.has("systems")) {
            JsonArray systems = requireArray(meta, "systems");

            Set<Os> parsedSystems = parseSystems(systems);

            if (parsedSystems.isEmpty())
                throw new JsonParseException("No systems found");

            registry.setSystems(new ArrayList<>(parsedSystems));
        }
    }

    private String getStringElse(JsonObject parent, String fieldName, String defaultValue) {
        if (!parent.has(fieldName))
            return defaultValue;
        return requireString(parent, fieldName).trim();
    }

    private List<CustomMappingsRegistryKey> expandKeyWildcards(int key,
                                                               boolean hasShift, boolean shiftValue,
                                                               boolean hasAltOption, boolean altOptionValue,
                                                               boolean hasControl, boolean controlValue,
                                                               boolean hasSuperCommand, boolean superCommandValue) {

        List<Boolean> shiftVals = hasShift ? List.of(shiftValue) : List.of(false, true);
        List<Boolean> altOptionals = hasAltOption ? List.of(altOptionValue) : List.of(false, true);
        List<Boolean> controlVals = hasControl ? List.of(controlValue) : List.of(false, true);
        List<Boolean> superCommandVals = hasSuperCommand ? List.of(superCommandValue) : List.of(false, true);

        List<CustomMappingsRegistryKey> results = new ArrayList<>();
        for (boolean s : shiftVals)
            for (boolean a : altOptionals)
                for (boolean c : controlVals)
                    for (boolean sup : superCommandVals)
                        results.add(new CustomMappingsRegistryKey(key, s, a, c, sup));
        return results;
    }

    private Set<Os> parseSystems(JsonArray systemsArray) {
        Map<String, Os> osMap = getOsNameMap();
        Set<Os> systems = new HashSet<>();

        for (JsonElement systemElement : systemsArray) {
            if (!systemElement.isJsonPrimitive() || !systemElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected each entry in \"systems\" to be a string");
            String systemName = systemElement.getAsString().trim().toLowerCase(Locale.ROOT);
            Os os = osMap.get(systemName);
            if (os == null)
                throw new JsonParseException("Unknown system: " + systemName);
            systems.add(os);
        }

        return systems;
    }

    private JsonObject requireObject(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonObject())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an object");

        return element.getAsJsonObject();
    }

    private JsonArray requireArray(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonArray())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be an array");

        return element.getAsJsonArray();
    }

    private String requireString(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a string");

        return element.getAsString();
    }

    private boolean getOptionalBoolean(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            return false;

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a boolean");

        return element.getAsBoolean();
    }

    private int requireInt(JsonObject parent, String fieldName) {
        if (!parent.has(fieldName))
            throw new JsonParseException("Missing required field: " + fieldName);

        JsonElement element = parent.get(fieldName);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Expected \"" + fieldName + "\" to be a number");

        return element.getAsInt();
    }
}
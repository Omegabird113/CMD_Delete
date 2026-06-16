package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomMappingsJSONDeserializer implements JsonDeserializer<CustomMappingsRegistry> {
    private static final Map<String, Os> osMap = Map.of(
            "windows", Os.WINDOWS,
            "mac", Os.MAC,
            "linux", Os.LINUX
    );
    private static final Map<String, NavAction> navActionMap = Arrays.stream(NavAction.values())
            .collect(Collectors.toUnmodifiableMap(NavAction::name, Function.identity()));

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

        Map<String, Integer> keyMap = KeyCodeRegistry.getKeyMap();
        Set<CustomMappingsRegistryKey> registeredKeys = new HashSet<>();

        for (String actionName : actions.keySet()) {
            NavAction action = navActionMap.get(actionName.trim().toUpperCase(Locale.ROOT));
            if (action == null || action == NavAction.NONE) {
                CmdDeleteClient.LOGGER.warn("Invalid action specified by custom mappings: \"{}\". All key-combos registered in this action skipped...", actionName);
                continue;
            }

            JsonArray bindings = requireArray(actions, actionName);

            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException("Expected each binding for action \"" + actionName + "\" to be an object");

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
                    if (!registeredKeys.add(key)) {
                        CmdDeleteClient.LOGGER.warn("Duplicate key binding in custom binding with action of \"{}\" and key of \"{}\" (exactly \"{}\"). 2nd registration skipped...", actionName, keyName, key);
                        continue;
                    }
                    registry.put(key, action);
                }
            }
        }

        return registry;
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
        else {
            double d = element.getAsDouble();
            int i = element.getAsInt();
            if (d != (double) i)
                throw new JsonParseException("Expected \"" + fieldName + "\" to be a an integer.");
            else
                return i;
        }
    }
}

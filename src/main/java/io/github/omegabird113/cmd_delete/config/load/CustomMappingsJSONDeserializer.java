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

        if (!jsonObject.has("fv"))
            throw new JsonParseException("Missing format version number");
        JsonElement fvElement = jsonObject.get("fv");
        if (!fvElement.isJsonPrimitive() || !fvElement.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Expected \"fv\" to be a number");
        int version = fvElement.getAsInt();
        if (version != 1)
            throw new JsonParseException("Invalid format version number: " + version);

        if (!jsonObject.has("meta"))
            throw new JsonParseException("Missing required field: meta");
        JsonElement metaElement = jsonObject.get("meta");
        if (!metaElement.isJsonObject())
            throw new JsonParseException("Expected \"meta\" to be an object");
        JsonObject meta = metaElement.getAsJsonObject();
        parseMeta(meta, registry);

        if (!jsonObject.has("actions"))
            throw new JsonParseException("Missing required field: actions");
        JsonElement actionsElement = jsonObject.get("actions");
        if (!actionsElement.isJsonObject())
            throw new JsonParseException("Expected \"actions\" to be an object");
        JsonObject actions = actionsElement.getAsJsonObject();

        Map<String, NavAction> actionMap = getNavActionNameMap();
        Map<String, Integer> keyMap = KeyCodeRegistry.getKeyMap();
        Set<CustomMappingsRegistryKey> registeredKeys = new HashSet<>();

        for (String actionName : actions.keySet()) {
            NavAction action = actionMap.get(actionName.trim().toUpperCase(Locale.ROOT));
            if (action == null || action == NavAction.NONE)
                continue;

            JsonElement bindingsElement = actions.get(actionName);
            if (!bindingsElement.isJsonArray())
                throw new JsonParseException("Expected bindings for action \"" + actionName + "\" to be an array");
            JsonArray bindings = bindingsElement.getAsJsonArray();
            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException("Expected each binding for action \"" + actionName + "\" to be an object");
                JsonObject binding = bindingElement.getAsJsonObject();

                if (!binding.has("key"))
                    throw new JsonParseException("Binding for action \"" + actionName + "\" is missing required field: key");
                JsonElement keyElement = binding.get("key");
                if (!keyElement.isJsonPrimitive() || !keyElement.getAsJsonPrimitive().isString())
                    throw new JsonParseException("Expected \"key\" in action \"" + actionName + "\" to be a string");
                String keyName = keyElement.getAsString().trim().toLowerCase(Locale.ROOT);
                Integer keyCode = keyMap.get(keyName);
                if (keyCode == null)
                    throw new JsonParseException("Unknown key name \"" + keyName + "\" in action \"" + actionName + "\"");

                boolean hasShift = binding.has("shift");
                if (hasShift && (!binding.get("shift").isJsonPrimitive() || !binding.get("shift").getAsJsonPrimitive().isBoolean()))
                    throw new JsonParseException("Expected \"shift\" in action \"" + actionName + "\" to be a boolean");
                boolean shiftValue = hasShift && binding.get("shift").getAsBoolean();

                boolean hasAltOption = binding.has("altOption");
                if (hasAltOption && (!binding.get("altOption").isJsonPrimitive() || !binding.get("altOption").getAsJsonPrimitive().isBoolean()))
                    throw new JsonParseException("Expected \"altOption\" in action \"" + actionName + "\" to be a boolean");
                boolean altOptionValue = hasAltOption && binding.get("altOption").getAsBoolean();

                boolean hasControl = binding.has("control");
                if (hasControl && (!binding.get("control").isJsonPrimitive() || !binding.get("control").getAsJsonPrimitive().isBoolean()))
                    throw new JsonParseException("Expected \"control\" in action \"" + actionName + "\" to be a boolean");
                boolean controlValue = hasControl && binding.get("control").getAsBoolean();

                boolean hasSuperCommand = binding.has("superCommand");
                if (hasSuperCommand && (!binding.get("superCommand").isJsonPrimitive() || !binding.get("superCommand").getAsJsonPrimitive().isBoolean()))
                    throw new JsonParseException("Expected \"superCommand\" in action \"" + actionName + "\" to be a boolean");
                boolean superCommandValue = hasSuperCommand && binding.get("superCommand").getAsBoolean();

                List<CustomMappingsRegistryKey> keys = keyModifierWildcardLogicParser(keyCode,
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
                    registry.register(key, action);
                }
            }
        }

        return registry;
    }

    private Map<String, NavAction> getNavActionNameMap() {
        Map<String, NavAction> map = new HashMap<>();
        for (NavAction action : NavAction.values())
            map.put(action.toString(), action);
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
        if (meta.has("name")) {
            JsonElement nameElement = meta.get("name");
            if (!nameElement.isJsonPrimitive() || !nameElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected \"name\" in meta to be a string");
            registry.setName(nameElement.getAsString().trim());
        } else
            registry.setName("Unnamed Custom Mappings");

        if (meta.has("author")) {
            JsonElement authorElement = meta.get("author");
            if (!authorElement.isJsonPrimitive() || !authorElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected \"author\" in meta to be a string");
            registry.setAuthor(authorElement.getAsString().trim());
        } else
            registry.setAuthor("unknown");

        if (meta.has("description")) {
            JsonElement descriptionElement = meta.get("description");
            if (!descriptionElement.isJsonPrimitive() || !descriptionElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected \"description\" in meta to be a string");
            registry.setDescription(descriptionElement.getAsString().trim());
        } else
            registry.setDescription("No description provided");

        if (meta.has("version")) {
            JsonElement versionElement = meta.get("version");
            if (!versionElement.isJsonPrimitive() || !versionElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected \"version\" in meta to be a string");
            String version = versionElement.getAsString().trim();
            registry.setVersion(version);
        } else
            registry.setVersion("unknown");


        if (meta.has("systems")) {
            JsonElement systemsElement = meta.get("systems");
            if (!systemsElement.isJsonArray())
                throw new JsonParseException("Expected \"systems\" in meta to be an array");
            Set<Os> systems = parseSystems(systemsElement);
            if (!systems.isEmpty())
                registry.setSystems(new ArrayList<>(systems));
            else
                throw new JsonParseException("No systems found");
        }
    }

    private List<CustomMappingsRegistryKey> keyModifierWildcardLogicParser(int key,
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

    private Set<Os> parseSystems(JsonElement systemsElement) {
        Map<String, Os> osMap = getOsNameMap();
        Set<Os> systems = new HashSet<>();

        JsonArray systemsArray = systemsElement.getAsJsonArray();
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
}
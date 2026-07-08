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

import static io.github.omegabird113.cmd_delete.config.load.JsonParsingUtils.*;

public class CustomMappingsJSONDeserializer implements JsonDeserializer<CustomMappingsRegistry> {
    private static final Map<String, Os> osMap = genOsMap();
    private static final Map<String, NavAction> navActionMap = Arrays.stream(NavAction.values())
            .collect(Collectors.toMap(NavAction::name, Function.identity()));

    private static Map<String, Os> genOsMap() {
        Map<String, Os> map = new HashMap<>();
        map.put("windows", Os.WINDOWS);
        map.put("mac", Os.MAC);
        map.put("linux", Os.LINUX);
        return map;
    }

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
                if (keyCode == null) {
                    CmdDeleteClient.LOGGER.warn("Unknown key name \"{}\" in action \"{}\". This key skipped...", keyName, actionName);
                    continue;
                }

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

    private List<CustomMappingsRegistryKey> expandKeyWildcards(int key,
                                                               boolean hasShift, boolean shiftValue,
                                                               boolean hasAltOption, boolean altOptionValue,
                                                               boolean hasControl, boolean controlValue,
                                                               boolean hasSuperCommand, boolean superCommandValue) {

        boolean[] shiftVals = hasShift ? new boolean[]{shiftValue} : new boolean[]{false, true};
        boolean[] altOptionals = hasAltOption ? new boolean[]{altOptionValue} : new boolean[]{false, true};
        boolean[] controlVals = hasControl ? new boolean[]{controlValue} : new boolean[]{false, true};
        boolean[] superCommandVals = hasSuperCommand ? new boolean[]{superCommandValue} : new boolean[]{false, true};

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
}

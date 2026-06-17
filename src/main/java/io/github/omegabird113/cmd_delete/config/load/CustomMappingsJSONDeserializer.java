package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.KeyCombo;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.omegabird113.cmd_delete.config.load.JsonParsingUtils.*;

public class CustomMappingsJSONDeserializer implements JsonDeserializer<CustomMappingsRegistry> {
    private static final Map<String, Os> OS_MAP = Map.of(
            "windows", Os.WINDOWS,
            "mac", Os.MAC,
            "linux", Os.LINUX
    );
    private static final Map<String, NavAction> NAV_ACTION_MAP = Arrays.stream(NavAction.values())
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
        Set<KeyCombo> registeredKeys = new HashSet<>();

        for (String actionName : actions.keySet()) {
            NavAction action = NAV_ACTION_MAP.get(actionName.trim().toUpperCase(Locale.ROOT));
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

                List<KeyCombo> keys = expandKeyWildcards(
                        keyCode,
                        hasShift, shiftValue,
                        hasAltOption, altOptionValue,
                        hasControl, controlValue,
                        hasSuperCommand, superCommandValue
                );

                for (KeyCombo key : keys) {
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

    private List<KeyCombo> expandKeyWildcards(int key,
                                              boolean hasShift, boolean shiftValue,
                                              boolean hasAltOption, boolean altOptionValue,
                                              boolean hasControl, boolean controlValue,
                                              boolean hasSuperCommand, boolean superCommandValue) {

        List<Boolean> shiftVals = hasShift ? List.of(shiftValue) : List.of(false, true);
        List<Boolean> altOptionals = hasAltOption ? List.of(altOptionValue) : List.of(false, true);
        List<Boolean> controlVals = hasControl ? List.of(controlValue) : List.of(false, true);
        List<Boolean> superCommandVals = hasSuperCommand ? List.of(superCommandValue) : List.of(false, true);

        List<KeyCombo> results = new ArrayList<>();
        for (boolean s : shiftVals)
            for (boolean a : altOptionals)
                for (boolean c : controlVals)
                    for (boolean sup : superCommandVals)
                        results.add(new KeyCombo(key, s, a, c, sup));
        return results;
    }

    private Set<Os> parseSystems(JsonArray systemsArray) {
        Set<Os> systems = new HashSet<>();

        for (JsonElement systemElement : systemsArray) {
            if (!systemElement.isJsonPrimitive() || !systemElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected each entry in \"systems\" to be a string");
            String systemName = systemElement.getAsString().trim().toLowerCase(Locale.ROOT);
            Os os = OS_MAP.get(systemName);
            if (os == null)
                throw new JsonParseException("Unknown system: " + systemName);
            systems.add(os);
        }

        return systems;
    }
}

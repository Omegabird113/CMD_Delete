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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomMappingsJSONDeserializer implements JsonDeserializer<CustomMappingsRegistry> {
    private static final Pattern semVerPattern = Pattern.compile("^(?:0|[1-9]\\\\d*)(?:\\\\.(?:0|[1-9]\\\\d*|\\\\d*[A-Za-z][0-9A-Za-z-]*))+(?:-(?:0|[1-9]\\\\d*|\\\\d*[A-Za-z][0-9A-Za-z-]*)(?:\\\\.(?:0|[1-9]\\\\d*|\\\\d*[A-Za-z][0-9A-Za-z-]*))*)?(?:\\\\+[0-9A-Za-z-]+(?:\\\\.[0-9A-Za-z-]+)*)?$");

    @Override
    public CustomMappingsRegistry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        CustomMappingsRegistry registry = new CustomMappingsRegistry();

        if (!jsonObject.has("fv")) {
            throw new JsonParseException("Missing format version number");
        }
        int version = jsonObject.get("fv").getAsInt();
        if (version != 1) {
            throw new JsonParseException("Invalid format version number: " + version);
        }

        if (!jsonObject.has("meta")) {
            throw new JsonParseException("Missing required field: meta");
        }
        JsonObject meta = jsonObject.get("meta").getAsJsonObject();
        parseMeta(meta, registry);

        if (!jsonObject.has("actions")) {
            throw new JsonParseException("Missing required field: actions");
        }
        JsonObject actions = jsonObject.get("actions").getAsJsonObject();

        Map<String, NavAction> actionMap = getNavActionNameMap();
        Map<String, Integer> keyMap = KeyCodeRegistry.getKeyMap();
        Set<CustomMappingsRegistryKey> registeredKeys = new HashSet<>();

        for (String actionName : actions.keySet()) {
            NavAction action = actionMap.get(actionName.trim().toUpperCase());
            if (action == null || action == NavAction.NONE) {
                continue;
            }

            JsonArray bindings = actions.get(actionName).getAsJsonArray();
            for (JsonElement bindingElement : bindings) {
                JsonObject binding = bindingElement.getAsJsonObject();

                if (!binding.has("key")) {
                    throw new JsonParseException("Binding for action \"" + actionName + "\" is missing required field: key");
                }
                String keyName = binding.get("key").getAsString().trim().toLowerCase();
                Integer keyCode = keyMap.get(keyName);
                if (keyCode == null) {
                    throw new JsonParseException("Unknown key name \"" + keyName + "\" in action \"" + actionName + "\"");
                }

                boolean hasShift = binding.has("shift");
                boolean shiftValue = hasShift && binding.get("shift").getAsBoolean();
                boolean hasAltOption = binding.has("altOption");
                boolean altOptionValue = hasAltOption && binding.get("altOption").getAsBoolean();
                boolean hasControl = binding.has("control");
                boolean controlValue = hasControl && binding.get("control").getAsBoolean();
                boolean hasSuperCommand = binding.has("superCommand");
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
        for (NavAction action : NavAction.values()) {
            map.put(action.toString(), action);
        }
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
            registry.setName(meta.get("name").getAsString().trim());
        } else {
            registry.setName("Unnamed Custom Mappings");
        }

        if (meta.has("author")) {
            registry.setAuthor(meta.get("author").getAsString().trim());
        } else {
            registry.setAuthor("unknown");
        }

        if (meta.has("description")) {
            registry.setDescription(meta.get("description").getAsString().trim());
        } else {
            registry.setDescription("No description provided");
        }

        if (meta.has("version")) {
            String version = (meta.get("version").getAsString().trim());
            Matcher semVerMatcher = semVerPattern.matcher(version);
            if (semVerMatcher.matches()) {
                registry.setVersion(version);
            } else {
                registry.setVersion("unknown");
            }
        } else {
            registry.setVersion("unknown");
        }

        if (meta.has("systems")) {
            Map<String, Os> osMap = getOsNameMap();
            Set<Os> systems = new HashSet<>();

            JsonArray systemsArray = meta.get("systems").getAsJsonArray();
            for (JsonElement systemElement : systemsArray) {
                String systemName = systemElement.getAsString().trim().toLowerCase();
                Os os = osMap.get(systemName);
                if (os == null) {
                    throw new JsonParseException("Unknown system: " + systemName);
                }
                systems.add(os);
            }

            if (!systems.isEmpty()) {
                registry.setSystems(new ArrayList<>(systems));
            } else {
                throw new JsonParseException("No systems found");
            }
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
}
package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        Map<String, Integer> keyMap = getKeyNameMap();
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

                boolean shift = binding.has("shift") && binding.get("shift").getAsBoolean();
                boolean altOption = binding.has("altOption") && binding.get("altOption").getAsBoolean();
                boolean control = binding.has("control") && binding.get("control").getAsBoolean();
                boolean superCommand = binding.has("superCommand") && binding.get("superCommand").getAsBoolean();

                CustomMappingsRegistryKey key = new CustomMappingsRegistryKey(keyCode, shift, altOption, control, superCommand);

                if (registeredKeys.contains(key)) {
                    throw new JsonParseException("Duplicate key binding in action \"" + actionName + "\": " + keyName);
                }

                registeredKeys.add(key);
                registry.register(key, action);
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

    private Map<String, Integer> getKeyNameMap() {
        Map<String, Integer> map = new HashMap<>();

        map.put("left", GLFW.GLFW_KEY_LEFT);
        map.put("right", GLFW.GLFW_KEY_RIGHT);
        map.put("up", GLFW.GLFW_KEY_UP);
        map.put("down", GLFW.GLFW_KEY_DOWN);

        map.put("home", GLFW.GLFW_KEY_HOME);
        map.put("end", GLFW.GLFW_KEY_END);
        map.put("pageup", GLFW.GLFW_KEY_PAGE_UP);
        map.put("pagedown", GLFW.GLFW_KEY_PAGE_DOWN);

        map.put("backspace", GLFW.GLFW_KEY_BACKSPACE);
        map.put("delete", GLFW.GLFW_KEY_DELETE);
        map.put("enter", GLFW.GLFW_KEY_ENTER);
        map.put("escape", GLFW.GLFW_KEY_ESCAPE);
        map.put("tab", GLFW.GLFW_KEY_TAB);
        map.put("insert", GLFW.GLFW_KEY_INSERT);

        map.put("0", GLFW.GLFW_KEY_0);
        map.put("1", GLFW.GLFW_KEY_1);
        map.put("2", GLFW.GLFW_KEY_2);
        map.put("3", GLFW.GLFW_KEY_3);
        map.put("4", GLFW.GLFW_KEY_4);
        map.put("5", GLFW.GLFW_KEY_5);
        map.put("6", GLFW.GLFW_KEY_6);
        map.put("7", GLFW.GLFW_KEY_7);
        map.put("8", GLFW.GLFW_KEY_8);
        map.put("9", GLFW.GLFW_KEY_9);

        map.put("a", GLFW.GLFW_KEY_A);
        map.put("b", GLFW.GLFW_KEY_B);
        map.put("c", GLFW.GLFW_KEY_C);
        map.put("d", GLFW.GLFW_KEY_D);
        map.put("e", GLFW.GLFW_KEY_E);
        map.put("f", GLFW.GLFW_KEY_F);
        map.put("g", GLFW.GLFW_KEY_G);
        map.put("h", GLFW.GLFW_KEY_H);
        map.put("i", GLFW.GLFW_KEY_I);
        map.put("j", GLFW.GLFW_KEY_J);
        map.put("k", GLFW.GLFW_KEY_K);
        map.put("l", GLFW.GLFW_KEY_L);
        map.put("m", GLFW.GLFW_KEY_M);
        map.put("n", GLFW.GLFW_KEY_N);
        map.put("o", GLFW.GLFW_KEY_O);
        map.put("p", GLFW.GLFW_KEY_P);
        map.put("q", GLFW.GLFW_KEY_Q);
        map.put("r", GLFW.GLFW_KEY_R);
        map.put("s", GLFW.GLFW_KEY_S);
        map.put("t", GLFW.GLFW_KEY_T);
        map.put("u", GLFW.GLFW_KEY_U);
        map.put("v", GLFW.GLFW_KEY_V);
        map.put("w", GLFW.GLFW_KEY_W);
        map.put("x", GLFW.GLFW_KEY_X);
        map.put("y", GLFW.GLFW_KEY_Y);
        map.put("z", GLFW.GLFW_KEY_Z);

        return map;
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
            registry.setAuthor("No description provided");
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
            registry.setAuthor("unknown");
        }

        if (meta.has("systems")) {
            Map<String, Os> osMap = getOsNameMap();
            ArrayList<Os> systems = new ArrayList<>();

            JsonArray systemsArray = meta.get("systems").getAsJsonArray();
            for (JsonElement systemElement : systemsArray) {
                String systemName = systemElement.getAsString().trim().toLowerCase();
                Os os = osMap.get(systemName);
                if (os == null) {
                    throw new JsonParseException("Unknown system: " + systemName);
                }
                systems.add(os);
            }

            registry.setSystems(systems);
        }
    }
}
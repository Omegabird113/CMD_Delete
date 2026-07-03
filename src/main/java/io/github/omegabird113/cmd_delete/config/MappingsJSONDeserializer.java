package io.github.omegabird113.cmd_delete.config;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.omegabird113.cmd_delete.config.JsonParsingUtils.*;

public final class MappingsJSONDeserializer implements JsonDeserializer<MappingsRegistry> {
    private static final Logger LOGGER = LoggingManager.getLogger(MappingsJSONManager.class);
    private static final @NonNull Map<String, Os> OS_MAP = Map.of(
            "windows", Os.WINDOWS,
            "mac", Os.MAC,
            "linux", Os.LINUX
    );
    private static final @NonNull Map<@NonNull String, @NonNull NavAction> NAV_ACTION_MAP = Arrays.stream(NavAction.values())
            .collect(Collectors.toUnmodifiableMap(NavAction::name, Function.identity()));

    @Override
    public @NonNull MappingsRegistry deserialize(@NonNull JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Expected a JSON object at root");
        JsonObject jsonObject = json.getAsJsonObject();

        int fv = requireInt(jsonObject, "fv");
        if (fv != CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION)
            if (fv >= CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION)
                LOGGER.warn("Old mappings version ({}) used by custom mappings. Please update to version {}", fv, CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION);
            else
                throw new JsonParseException("Invalid format version number: " + fv + ". The current format version is: " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION);

        String inherits = getStringElse(jsonObject, "inherits", "");

        JsonObject actions = requireObject(jsonObject, "actions");

        Map<KeyCombo, NavAction> localKeys = new HashMap<>();
        Map<KeyCombo, NavAction> disabledKeys = new HashMap<>();

        for (String actionName : actions.keySet()) {
            NavAction action = NAV_ACTION_MAP.get(actionName.trim().toUpperCase(Locale.ROOT));
            if (action == null || action == NavAction.NONE) {
                LOGGER.warn("Invalid action specified by custom mappings: \"{}\". All key-combos registered in this action skipped...", actionName);
                continue;
            }

            if (ActionOffsetUtils.isOverrideAction(action) && fv == 2)
                throw new JsonParseException("Format version 2 file specified actions of fv 3: " + actionName);

            JsonArray bindings = requireArray(actions, actionName);

            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException("Expected each binding for action \"" + actionName + "\" to be an object");

                JsonObject binding = bindingElement.getAsJsonObject();

                int keyCode;
                try {
                    keyCode = requireKeyCode(binding, "key");
                } catch (JsonParseException e) {
                    LOGGER.warn("Invalid key binding due to error: {}", e.getMessage());
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

                boolean enabled = !binding.has("enabled") || binding.get("enabled").getAsBoolean();

                Map<KeyCombo, NavAction> toAdd = enabled ? localKeys : disabledKeys;

                for (KeyCombo key : keys) {
                    if (toAdd.containsKey(key))
                        LOGGER.warn("Duplicate key binding in custom binding with action of \"{}\" and key \"{}\". 2nd registration skipped...", actionName, key);
                    else
                        toAdd.put(key, action);
                }
            }
        }

        MetadataContainer container = parseMeta(requireObject(jsonObject, "meta"));
        FeatureFlags ff = parseFlags(jsonObject, fv, inherits);

        return disabledKeys.isEmpty() ? new MappingsRegistry(localKeys, container.systems(), ff, inherits, container.name(), container.author(), container.description(), container.version(), container.id())
                : new MappingsRegistry(localKeys, disabledKeys, container.systems(), ff, inherits, container.name(), container.author(), container.description(), container.version(), container.id());
    }

    @Contract("_, _, _ -> new")
    private @NonNull FeatureFlags parseFlags(JsonObject root, int fv, String inherits) {
        if (fv == 2)
            return new FeatureFlags(false, true);
        else {
            JsonObject flags;
            try {
                flags = requireObject(root, "flags");
            } catch (JsonParseException _) {
                return new FeatureFlags(false, true);
            }
            Boolean overrideVanillaNavigation = getNullableBoolean(flags, "overrideVanillaNavigation");
            Boolean crossLineSignMovement = getNullableBoolean(flags, "crossLineSignMovement");
            if (overrideVanillaNavigation == null && inherits.isEmpty())
               overrideVanillaNavigation = false;
            if (crossLineSignMovement == null && inherits.isEmpty())
                overrideVanillaNavigation = true;
            return new FeatureFlags(overrideVanillaNavigation, crossLineSignMovement);
        }
    }

    @Contract("_ -> new")
    private @NonNull MetadataContainer parseMeta(JsonObject meta) {
        String name = getStringElse(meta, "name", "Unnamed Custom Mappings");
        String author = getStringElse(meta, "author", "unknown");
        String description = getStringElse(meta, "description", "No description provided");
        String version = getStringElse(meta, "version", "unknown");
        String id = requireString(meta, "id");

        if (version.equals("$$cmd_delete$$"))
            version = CmdDeleteClient.VERSION;
        if (author.equals("$$cmd_delete$$"))
            author = "Omegabird113";

        if (meta.has("systems")) {
            JsonArray systems = requireArray(meta, "systems");
            Set<Os> parsedSystems = parseSystems(systems);
            if (parsedSystems.isEmpty())
                throw new JsonParseException("No systems found");
            return new MetadataContainer(name, author, version, description, id, parsedSystems);
        } else
            throw new JsonParseException("No systems found");
    }

    private @NonNull List<KeyCombo> expandKeyWildcards(int key,
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

    private @NonNull Set<Os> parseSystems(@NonNull JsonArray systemsArray) {
        Set<Os> systems = new LinkedHashSet<>();

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

    private record MetadataContainer(String name, String author, String version, String description, String id,
                                     Set<Os> systems) {
    }
}

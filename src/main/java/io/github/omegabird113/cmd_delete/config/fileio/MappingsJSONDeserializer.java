package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.*;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.omegabird113.cmd_delete.config.fileio.JsonParsingUtils.*;

final class MappingsJSONDeserializer implements JsonDeserializer<MappingsRegistry> {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(MappingsJSONManager.class);
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
        final JsonObject jsonObject = json.getAsJsonObject();

        final boolean strictMode = getOptionalBoolean(jsonObject, "strict");

        final int fv = requireInt(jsonObject, "fv");
        if (fv < CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION || fv > CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION)
            throw new JsonParseException("Invalid format version number: " + fv + ". The current format version is: " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION);
        if (fv != CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION)
            logWarn(
                    "Old mappings version (" + fv + ") used by custom mappings. Please update to version " + CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION,
                    strictMode,
                    fv
            );

        final String inherits = getStringElse(jsonObject, "inherits", "");

        final JsonObject actions = requireObject(jsonObject, "actions");
        final HashMap<KeyCombo, NavAction> localKeys = new HashMap<>();
        final HashMap<KeyCombo, NavAction> disabledKeys = new HashMap<>();
        parseActions(actions, localKeys, disabledKeys, fv, strictMode);

        final MetadataContainer container = parseMeta(requireObject(jsonObject, "meta"));
        final FeatureFlags ff = parseFlags(jsonObject, fv, inherits);

        return new MappingsRegistry(localKeys, (disabledKeys.isEmpty() ? null : disabledKeys), List.copyOf(container.systems()), ff, inherits, container.name(), container.author(), container.description(), container.version(), container.id());
    }

    private void logWarn(@NonNull String message, boolean strictMode, int fv) {
        if (strictMode && fv == 4)
            throw new JsonParseException(message);
        else
            LOGGER.warn(message);
    }

    private void parseActions(@NonNull JsonObject actions, @NonNull HashMap<KeyCombo, NavAction> localKeys, @NonNull HashMap<KeyCombo, NavAction> disabledKeys, int fv, boolean strictMode) {
        for (String actionName : actions.keySet()) {
            NavAction action = NAV_ACTION_MAP.get(actionName.trim().toUpperCase(Locale.ROOT));
            if (action == null || action == NavAction.NONE) {
                logWarn(
                        "Invalid action specified by custom mappings: \"" + actionName + "\". All key-combos registered in this action skipped...",
                        strictMode,
                        fv
                );
                continue;
            }

            if (ActionOffsetUtils.isOverrideAction(action) && fv == 2)
                throw new JsonParseException("Format version 2 file specified actions of fv 3: " + actionName);

            if (ActionOffsetUtils.isOverrideEditAction(action) && fv < 4)
                throw new JsonParseException("Format version 2 or 3 file specified actions of fv 4: " + actionName);

            final JsonArray bindings = requireArray(actions, actionName);

            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException("Expected each binding for action \"" + actionName + "\" to be an object");

                final JsonObject binding = bindingElement.getAsJsonObject();

                final int keyCode;
                try {
                    keyCode = requireKeyCode(binding, "key");
                } catch (JsonParseException e) {
                    logWarn(
                            "Invalid key binding due to error: " + e.getMessage(),
                            strictMode,
                            fv
                    );
                    continue;
                }

                final boolean hasShift = binding.has("shift");
                final boolean shiftValue = getOptionalBoolean(binding, "shift");

                final boolean hasAltOption = binding.has("altOption");
                final boolean altOptionValue = getOptionalBoolean(binding, "altOption");

                final boolean hasControl = binding.has("control");
                final boolean controlValue = getOptionalBoolean(binding, "control");

                final boolean hasSuperCommand = binding.has("superCommand");
                final boolean superCommandValue = getOptionalBoolean(binding, "superCommand");

                final KeyCombo[] keys = expandKeyWildcards(
                        keyCode,
                        hasShift, shiftValue,
                        hasAltOption, altOptionValue,
                        hasControl, controlValue,
                        hasSuperCommand, superCommandValue
                );

                final boolean enabled = !binding.has("enabled") || binding.get("enabled").getAsBoolean();

                final Map<KeyCombo, NavAction> toAdd = enabled ? localKeys : disabledKeys;

                for (KeyCombo key : keys) {
                    if (toAdd.containsKey(key))
                        logWarn(
                        "Duplicate key binding in custom binding with action of \"" + actionName + "\" and key \"" + key + "\". 2nd registration skipped...",
                                strictMode,
                                fv
                        );
                    else
                        toAdd.put(key, action);
                }
            }
        }
    }

    @Contract("_, _, _ -> new")
    private @NonNull FeatureFlags parseFlags(@NonNull JsonObject root, int fv, @NonNull String inherits) {
        if (fv == 2)
            return new FeatureFlags(false, true);
        else {
            final JsonObject flags;
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
                crossLineSignMovement = true;
            return new FeatureFlags(overrideVanillaNavigation, crossLineSignMovement);
        }
    }

    @Contract("_ -> new")
    private @NonNull MetadataContainer parseMeta(@NonNull JsonObject meta) {
        final String name = getStringElse(meta, "name", "Unnamed Custom Mappings");
        final String author = getStringElse(meta, "author", "unknown").replace("$$cmd_delete$$", "Omegabird113");
        final String description = getStringElse(meta, "description", "No description provided");
        final String version = getStringElse(meta, "version", "unknown").replace("$$cmd_delete$$", CmdDeleteClient.VERSION);
        final String id = requireString(meta, "id");

        final JsonArray systems = requireArray(meta, "systems");
        final Set<Os> parsedSystems = parseSystems(systems);
        if (parsedSystems.isEmpty())
            throw new JsonParseException("No systems found");
        return new MetadataContainer(name, author, version, description, id, parsedSystems);
    }

    private @NonNull KeyCombo[] expandKeyWildcards(int key,
                                                   boolean hasShift, boolean shiftValue,
                                                   boolean hasAltOption, boolean altOptionValue,
                                                   boolean hasControl, boolean controlValue,
                                                   boolean hasSuperCommand, boolean superCommandValue) {

        final boolean[] shiftVals = hasShift ? new boolean[]{shiftValue} : new boolean[]{false, true};
        final boolean[] altOptionVals = hasAltOption ? new boolean[]{altOptionValue} : new boolean[]{false, true};
        final boolean[] controlVals = hasControl ? new boolean[]{controlValue} : new boolean[]{false, true};
        final boolean[] superCommandVals = hasSuperCommand ? new boolean[]{superCommandValue} : new boolean[]{false, true};

        final List<KeyCombo> results = new ArrayList<>();
        for (boolean s : shiftVals)
            for (boolean a : altOptionVals)
                for (boolean c : controlVals)
                    for (boolean sup : superCommandVals)
                        results.add(new KeyCombo(key, s, a, c, sup));
        return results.toArray(KeyCombo[]::new);
    }

    private @NonNull Set<Os> parseSystems(@NonNull JsonArray systemsArray) {
        final Set<Os> systems = new LinkedHashSet<>();

        for (JsonElement systemElement : systemsArray) {
            if (!systemElement.isJsonPrimitive() || !systemElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected each entry in \"systems\" to be a string");
            final String systemName = systemElement.getAsString().trim().toLowerCase(Locale.ROOT);
            final Os os = OS_MAP.get(systemName);
            if (os == null)
                throw new JsonParseException("Unknown system: " + systemName);
            systems.add(os);
        }

        return systems;
    }

    private record MetadataContainer(@NonNull String name, @NonNull String author, @NonNull String version, @NonNull String description, @NonNull String id,
                                     @NonNull Set<Os> systems) {
    }
}

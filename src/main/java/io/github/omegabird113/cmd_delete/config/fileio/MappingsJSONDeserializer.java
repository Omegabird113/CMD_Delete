/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.utils.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.omegabird113.cmd_delete.config.fileio.JsonParsingUtils.*;

public final class MappingsJSONDeserializer {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(MappingsJSONManager.class);
    private static final @NonNull Map<String, Os> OS_MAP = Map.of(
            "windows", Os.WINDOWS,
            "mac", Os.MAC,
            "linux", Os.LINUX
    );
    private static final @NonNull Map<@NonNull String, @NonNull NavAction> NAV_ACTION_MAP = Arrays.stream(NavAction.values())
            .collect(Collectors.toUnmodifiableMap(NavAction::name, Function.identity()));
    private MappingsJSONDeserializer() {
    }

    static void logWarn(@NonNull String message, boolean strictMode) {
        if (strictMode)
            throw new JsonParseException(message);
        LOGGER.warn(message);
    }

    public static @NonNull MappingsRegistry deserialize(final @NonNull JsonElement json, final String fileName, final boolean custom) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Expected a JSON object at root");
        final JsonObject jsonObject = json.getAsJsonObject();

        final boolean strictModeVal = getOptionalBoolean(jsonObject, "strict");
        final int fv = requireFv(jsonObject);
        final boolean strictMode = strictModeVal && fv >= 4;

        final String inherits = getStringElse(jsonObject, "inherits", "");

        final JsonObject actions = requireObject(jsonObject, "actions");
        final HashMap<KeyCombo, NavAction> localKeys = new HashMap<>();
        final HashMap<KeyCombo, NavAction> disabledKeys = new HashMap<>();
        parseActions(actions, localKeys, disabledKeys, fv, strictMode);

        final MetadataContainer container = parseMeta(requireObject(jsonObject, "meta"), strictMode, custom);

        if (!container.id().equals(fileName))
            throw new JsonParseException(MappingsType.fromIfCustom(custom).commonName() + " mappings id \"" + container.id() + "\" does not match filename \"" + fileName + "\"");

        final FeatureFlags ff = parseFlags(jsonObject, fv, inherits);

        return new MappingsRegistry(localKeys, (disabledKeys.isEmpty() ? null : disabledKeys), List.copyOf(container.systems()), ff, inherits, container.name(), container.author(), container.description(), container.version(), container.id());
    }

    private static @NonNull String trimAndCaseIfNotStrict(final @NonNull String str, final boolean upper, final boolean strictMode) {
        if (strictMode)
            return str;
        if (upper)
            return str.trim().toUpperCase(Locale.ROOT);
        return str.trim().toLowerCase(Locale.ROOT);
    }

    private static void parseActions(final @NonNull JsonObject actions, final @NonNull HashMap<@NonNull KeyCombo, @NonNull NavAction> localKeys, final @NonNull HashMap<@NonNull KeyCombo, @NonNull NavAction> disabledKeys, final int fv, final boolean strictMode) {
        for (String actionName : actions.keySet()) {
            final NavAction action = NAV_ACTION_MAP.get(trimAndCaseIfNotStrict(actionName, true, strictMode));
            if (action == null || action == NavAction.NONE) {
                logWarn(
                        "Invalid action specified by custom mappings: \"" + actionName + "\". All key-combos registered in this action skipped...",
                        strictMode
                );
                continue;
            }

            if (action.overrideMode() && fv == 2)
                throw new JsonParseException("Format version 2 file specified actions of fv 3: " + actionName);
            if (action.isOverrideEdit() && fv < 4)
                throw new JsonParseException("Format version 2 or 3 file specified actions of fv 4: " + actionName);

            final JsonArray bindings = requireArray(actions, actionName);

            for (JsonElement bindingElement : bindings) {
                if (!bindingElement.isJsonObject())
                    throw new JsonParseException("Expected each binding for action \"" + actionName + "\" to be an object");

                final JsonObject binding = bindingElement.getAsJsonObject();

                final int keyCode;
                try {
                    keyCode = requireKeyCode(binding, "key", strictMode);
                } catch (JsonParseException e) {
                    logWarn(
                            "Invalid key binding due to error: " + e.getMessage(),
                            strictMode
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

                final boolean enabled = getOptionalBoolean(binding, "enabled", true);

                final Map<KeyCombo, NavAction> toAdd = enabled ? localKeys : disabledKeys;

                for (KeyCombo key : keys) {
                    if (toAdd.containsKey(key))
                        logWarn(
                                "Duplicate key binding in custom binding with action of \"" + actionName + "\" and key \"" + key + "\". 2nd registration skipped...",
                                strictMode
                        );
                    else
                        toAdd.put(key, action);
                }
            }
        }
    }

    @Contract("_, _, _ -> new")
    private static @NonNull FeatureFlags parseFlags(final @NonNull JsonObject root, final int fv, final @NonNull String inherits) {
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

    @Contract(pure = true)
    private static @NonNull String replacePlaceholderWithIfBuiltin(final @NonNull String input, final @NonNull String replaceWith, final boolean custom) {
        if (custom || !input.equals("$$cmd_delete$$"))
            return input;
        return replaceWith;
    }

    @Contract("_, _, _ -> new")
    private static @NonNull MetadataContainer parseMeta(final @NonNull JsonObject meta, final boolean strictMode, final boolean custom) {
        final String name = getStringElse(meta, "name", "Unnamed Custom Mappings");
        final String author = replacePlaceholderWithIfBuiltin(
                getStringElse(meta, "author", "unknown"),
                "Omegabird113", custom);
        final String description = getStringElse(meta, "description", "No description provided");
        final String version = replacePlaceholderWithIfBuiltin(
                getStringElse(meta, "version", "unknown"),
                CmdDeleteClient.VERSION, custom);
        final String id = requireFilenameSafeString(meta, "id");

        final JsonArray systems = requireArray(meta, "systems");
        final Set<Os> parsedSystems = parseSystems(systems, strictMode);
        if (parsedSystems.isEmpty())
            throw new JsonParseException("No systems found");
        return new MetadataContainer(name, author, version, description, id, parsedSystems);
    }

    private static @NonNull KeyCombo @NonNull [] expandKeyWildcards(final int key,
                                                                    final boolean hasShift, final boolean shiftValue,
                                                                    final boolean hasAltOption, final boolean altOptionValue,
                                                                    final boolean hasControl, final boolean controlValue,
                                                                    final boolean hasSuperCommand, final boolean superCommandValue) {

        final boolean[] shiftVals = hasShift ? new boolean[]{shiftValue} : new boolean[]{false, true};
        final boolean[] altOptionVals = hasAltOption ? new boolean[]{altOptionValue} : new boolean[]{false, true};
        final boolean[] controlVals = hasControl ? new boolean[]{controlValue} : new boolean[]{false, true};
        final boolean[] superCommandVals = hasSuperCommand ? new boolean[]{superCommandValue} : new boolean[]{false, true};

        final KeyCombo[] results = new KeyCombo[shiftVals.length * altOptionVals.length * controlVals.length * superCommandVals.length];
        int i = 0;
        for (boolean s : shiftVals)
            for (boolean a : altOptionVals)
                for (boolean c : controlVals)
                    for (boolean sup : superCommandVals) {
                        results[i] = new KeyCombo(key, s, a, c, sup);
                        i++;
                    }
        return results;
    }

    private static @NonNull Set<Os> parseSystems(final @NonNull JsonArray systemsArray, final boolean strictMode) {
        final Set<Os> systems = new LinkedHashSet<>();

        for (JsonElement systemElement : systemsArray) {
            if (!systemElement.isJsonPrimitive() || !systemElement.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected each entry in \"systems\" to be a string");
            final String systemName = trimAndCaseIfNotStrict(systemElement.getAsString(), false, strictMode);
            final Os os = OS_MAP.get(systemName);
            if (os == null)
                throw new JsonParseException("Unknown system: " + systemName);
            systems.add(os);
        }

        return systems;
    }

    private record MetadataContainer(@NonNull String name, @NonNull String author, @NonNull String version,
                                     @NonNull String description, @NonNull String id,
                                     @NonNull Set<Os> systems) {
    }
}

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

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MappingsInheritanceManager {
    private MappingsInheritanceManager() {
    }

    @Contract("_ -> new")
    public static @NonNull MappingsRegistry merge(final @NonNull List<@NonNull MappingsRegistry> toMerge) {
        final MappingsRegistry first = toMerge.get(0);
        final Map<KeyCombo, NavAction> firstMap = first.internalRegistry();
        final Map<KeyCombo, NavAction> localRegistry = new HashMap<>(firstMap);
        FeatureFlags currentFeatureFlags = first.featureFlags();

        for (int i = 1; i < toMerge.size(); i++) {
            final MappingsRegistry currentRegistry = toMerge.get(i);
            final Map<KeyCombo, NavAction> disabledMap = currentRegistry.internalDisabledRegistry();
            if (disabledMap != null)
                disabledMap.forEach(localRegistry::remove);
            final Map<KeyCombo, NavAction> enabledMap = currentRegistry.internalRegistry();
            localRegistry.putAll(enabledMap);
            currentFeatureFlags = FeatureFlags.merge(currentFeatureFlags, currentRegistry.featureFlags());
        }

        final MappingsRegistry last = toMerge.get(toMerge.size() - 1);

        return new MappingsRegistry(localRegistry, null, last.systems(), currentFeatureFlags, "", last.name(), last.author(), last.description(), last.version(), last.id());
    }
}

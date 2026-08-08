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

package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.utils.Os;
import org.jspecify.annotations.NonNull;

public record KeyCombo(int key, boolean shift, boolean altOption, boolean control,
                       boolean superCommand) {
    @Override
    public @NonNull String toString() {
        return "<" +
                (control ? "ctrl+" : "")
                + (superCommand ? (Os.IS_USING_MAC ? "cmd+" : "sup+") : "")
                + (altOption ? (Os.IS_USING_MAC ? "opt+" : "alt+") : "")
                + (shift ? "shift+" : "")
                + (KeyNameRegistry.getReverseKeyMap().getOrDefault(key, Integer.toString(key)))
                + ">";
    }
}

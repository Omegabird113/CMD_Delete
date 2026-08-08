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

package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public enum MappingsType {
    CUSTOM("custom:", "Custom"),
    BUILTIN("builtin:", "Builtin"),
    DEFAULT("", "Default");

    private final @NonNull String prefix;
    private final @NonNull String commonName;

    MappingsType(final @NonNull String prefix, final @NonNull String commonName) {
        this.prefix = prefix;
        this.commonName = commonName;
    }

    public static @NonNull MappingsType fromIfCustom(final boolean custom) {
        return custom ? MappingsType.CUSTOM : MappingsType.BUILTIN;
    }

    public @NonNull String prefix() {
        return prefix;
    }

    public @NonNull String commonName() {
        return commonName;
    }
}

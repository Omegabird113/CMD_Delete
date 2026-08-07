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

package io.github.omegabird113.cmd_delete.utils;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum Os {
    WINDOWS,
    LINUX,
    MAC;

    public static final @NonNull Os USING = getCurrent();
    public static final boolean IS_USING_MAC = USING == MAC;

    @Contract(pure = true)
    public static @NonNull Os getCurrent() {
        return Os.get(System.getProperty("os.name").toLowerCase(Locale.ROOT));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull Os get(final @NonNull String osName) {
        if (osName.contains("mac"))
            return MAC;
        else if (osName.contains("win"))
            return WINDOWS;
        else
            return LINUX;
    }
}

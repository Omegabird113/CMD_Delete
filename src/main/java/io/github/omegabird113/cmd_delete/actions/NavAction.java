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

package io.github.omegabird113.cmd_delete.actions;

import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Locale;

import static io.github.omegabird113.cmd_delete.actions.NavActionScope.*;
import static io.github.omegabird113.cmd_delete.actions.NavActionType.*;

@SuppressWarnings("unused")
public final class NavAction {
    private static final HashSet<NavAction> registry = new HashSet<>();
    private static final Logger LOGGER = LoggingManager.getLoggerFor(LoggingManager.class);

    static {
        LoggingManager.traceLog(LOGGER, "NavAction enum loaded. Detailed dump:\n{}", getDetailedActionDump());
    }

    private final @NonNull NavActionOffset offset;
    private final @NonNull NavActionType type;
    private final @NonNull NavActionScope scope;
    private final boolean overrideMode;

    NavAction(final @NonNull NavActionOffset offset, final @NonNull NavActionType type, final @NonNull NavActionScope scope, final boolean overrideMode) {
        this.offset = offset;
        this.overrideMode = overrideMode;
        this.type = type;
        this.scope = scope;
    }

    public static boolean register(final @NonNull NavAction action) {
        return registry.add(action);
    }

    public static @NonNull NavAction registerAndReturn(final @NonNull NavAction action) {
        registry.add(action);
        return action;
    }

    public static @NonNull String getDetailedActionDump() {
        final NavAction[] actions = registry.toArray(NavAction[]::new);

        final String[][] table = new String[actions.length + 1][5];
        table[0] = new String[]{"Action", "Type", "Scope", "Offset", "Override"};

        for (int i = 0; i < actions.length; i++) {
            final String actionStr = actions[i].toString();
            final String typeStr = actions[i].type().name();
            final String scopeStr = actions[i].scope().name();
            final String offsetStr = actions[i].offset().name();
            final String overrideStr = actions[i].overrideMode() ? "yes" : "no";

            final String[] entry = new String[]{actionStr, typeStr, scopeStr, offsetStr, overrideStr};
            table[i + 1] = entry;
        }

        final StringBuilder dump = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            final String[] row = table[i];
            dump.append(
                    String.format(Locale.ROOT, "%-18s %-7s %-11s %-7s %-3s", row[0], row[1], row[2], row[3], row[4])
            );
            if (i != table.length - 1)
                dump.append("\n");
        }
        return dump.toString();
    }

    @Contract(pure = true)
    public boolean isMove() {
        return this.type == MOVE;
    }

    @Contract(pure = true)
    public boolean isSelect() {
        return this.type == SELECT;
    }

    @Contract(pure = true)
    public boolean isDelete() {
        return this.type == DELETE;
    }

    @Contract(pure = true)
    public boolean isEdit() {
        return this.type == EDIT;
    }

    @Contract(pure = true)
    public boolean isOverrideEdit() {
        return this.overrideMode && this.isEdit();
    }

    @Contract(pure = true)
    public boolean isChar() {
        return this.scope == CHAR;
    }

    @Contract(pure = true)
    public boolean isWord() {
        return this.scope == WORD;
    }

    @Contract(pure = true)
    public boolean isWithinLine() {
        return this.scope == WITHIN_LINE;
    }

    @Contract(pure = true)
    public boolean isLine() {
        return this.scope == LINE;
    }

    @Contract(pure = true)
    public boolean isText() {
        return this.scope == TEXT;
    }

    public @NonNull NavActionOffset offset() {
        return offset;
    }

    public @NonNull NavActionType type() {
        return type;
    }

    public @NonNull NavActionScope scope() {
        return scope;
    }

    public boolean overrideMode() {
        return overrideMode;
    }
}

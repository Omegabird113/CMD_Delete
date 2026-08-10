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

import static io.github.omegabird113.cmd_delete.actions.NavActionOffset.*;
import static io.github.omegabird113.cmd_delete.actions.NavActionScope.*;
import static io.github.omegabird113.cmd_delete.actions.NavActionType.*;

public final class DefaultNavActions {
    public static final NavAction NAV_LINE_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, MOVE, WITHIN_LINE, false));
    public static final NavAction NAV_LINE_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, MOVE, WITHIN_LINE, false));
    public static final NavAction NAV_WORD_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, MOVE, WORD, false));
    public static final NavAction NAV_WORD_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, MOVE, WORD, false));
    public static final NavAction SEL_LINE_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, SELECT, WITHIN_LINE, false));
    public static final NavAction SEL_LINE_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, SELECT, WITHIN_LINE, false));
    public static final NavAction SEL_WORD_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, SELECT, WORD, false));
    public static final NavAction SEL_WORD_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, SELECT, WORD, false));
    public static final NavAction DEL_LINE_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, DELETE, WITHIN_LINE, false));
    public static final NavAction DEL_LINE_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, DELETE, WITHIN_LINE, false));
    public static final NavAction DEL_WORD_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, DELETE, WORD, false));
    public static final NavAction DEL_WORD_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, DELETE, WORD, false));
    public static final NavAction NAV_TEXT_START = NavAction.registerAndReturn(new NavAction(UP, MOVE, TEXT, false));
    public static final NavAction NAV_TEXT_END = NavAction.registerAndReturn(new NavAction(DOWN, MOVE, TEXT, false));
    public static final NavAction SEL_TEXT_START = NavAction.registerAndReturn(new NavAction(UP, SELECT, TEXT, false));
    public static final NavAction SEL_TEXT_END = NavAction.registerAndReturn(new NavAction(DOWN, SELECT, TEXT, false));
    public static final NavAction SEL_TEXT_UP = NavAction.registerAndReturn(new NavAction(UP, SELECT, LINE, false));
    public static final NavAction SEL_TEXT_DOWN = NavAction.registerAndReturn(new NavAction(DOWN, SELECT, LINE, false));
    public static final NavAction OVR_NAV_CHAR_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, MOVE, CHAR, true));
    public static final NavAction OVR_NAV_CHAR_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, MOVE, CHAR, true));
    public static final NavAction OVR_SEL_CHAR_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, SELECT, CHAR, true));
    public static final NavAction OVR_SEL_CHAR_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, SELECT, CHAR, true));
    public static final NavAction OVR_DEL_CHAR_LEFT = NavAction.registerAndReturn(new NavAction(LEFT, DELETE, CHAR, true));
    public static final NavAction OVR_DEL_CHAR_RIGHT = NavAction.registerAndReturn(new NavAction(RIGHT, DELETE, CHAR, true));
    public static final NavAction OVR_NAV_TEXT_UP = NavAction.registerAndReturn(new NavAction(UP, MOVE, LINE, true));
    public static final NavAction OVR_NAV_TEXT_DOWN = NavAction.registerAndReturn(new NavAction(DOWN, MOVE, LINE, true));
    public static final NavAction OVR_COPY = NavAction.registerAndReturn(new NavAction(INVALID, EDIT, TEXT, true));
    public static final NavAction OVR_CUT = NavAction.registerAndReturn(new NavAction(INVALID, EDIT, TEXT, true));
    public static final NavAction OVR_PASTE = NavAction.registerAndReturn(new NavAction(INVALID, EDIT, TEXT, true));
    public static final NavAction OVR_SELECT_ALL = NavAction.registerAndReturn(new NavAction(INVALID, EDIT, TEXT, true));
    public static final NavAction NONE = NavAction.registerAndReturn(new NavAction(INVALID, NO_TYPE, NO_SCOPE, false));

    private DefaultNavActions() {
    }
}

package io.github.omegabird113.cmd_delete.actions;

import static io.github.omegabird113.cmd_delete.actions.NavActionOffset.*;

public enum NavAction {
    NAV_LINE_LEFT(LEFT, Type.MOVE, false),
    NAV_LINE_RIGHT(RIGHT, Type.MOVE, false),
    NAV_WORD_LEFT(LEFT, Type.MOVE, false),
    NAV_WORD_RIGHT(RIGHT, Type.MOVE, false),
    SEL_LINE_LEFT(LEFT, Type.SELECT, false),
    SEL_LINE_RIGHT(RIGHT, Type.SELECT,false),
    SEL_WORD_LEFT(LEFT, Type.SELECT,false),
    SEL_WORD_RIGHT(RIGHT, Type.SELECT, false),
    DEL_LINE_LEFT(LEFT, Type.DELETE, false),
    DEL_LINE_RIGHT(RIGHT, Type.DELETE, false),
    DEL_WORD_LEFT(LEFT, Type.DELETE, false),
    DEL_WORD_RIGHT(RIGHT, Type.DELETE, false),
    NAV_TEXT_START(UP, Type.MOVE, false),
    NAV_TEXT_END(DOWN, Type.MOVE, false),
    SEL_TEXT_START(UP, Type.SELECT, false),
    SEL_TEXT_END(DOWN, Type.SELECT, false),
    SEL_TEXT_UP(UP, Type.SELECT, false),
    SEL_TEXT_DOWN(DOWN, Type.SELECT, false),
    OVR_NAV_CHAR_LEFT(LEFT, Type.MOVE, true),
    OVR_NAV_CHAR_RIGHT(RIGHT, Type.MOVE, true),
    OVR_SEL_CHAR_LEFT(LEFT, Type.SELECT, true),
    OVR_SEL_CHAR_RIGHT(RIGHT, Type.SELECT, true),
    OVR_DEL_CHAR_LEFT(LEFT, Type.DELETE, true),
    OVR_DEL_CHAR_RIGHT(RIGHT, Type.DELETE, true),
    OVR_NAV_TEXT_UP(UP, Type.MOVE, true),
    OVR_NAV_TEXT_DOWN(DOWN, Type.MOVE, true),
    OVR_COPY(INVALID, Type.EDIT, true),
    OVR_CUT(INVALID, Type.EDIT, true),
    OVR_PASTE(INVALID, Type.EDIT, true),
    OVR_SELECT_ALL(INVALID, Type.EDIT, true),
    NONE(INVALID, Type.NONE, false);

    public final NavActionOffset offset;
    public final Type type;
    public final boolean override;

    NavAction(NavActionOffset offset, Type type, boolean override) {
        this.offset = offset;
        this.override = override;
        this.type = type;
    }

    public enum Type {
        MOVE, SELECT, DELETE, EDIT, NONE
    }
}

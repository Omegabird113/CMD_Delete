package io.github.omegabird113.cmd_delete.config.load;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

final class KeyCodeRegistry {
    private static final Map<String, Integer> KEY_MAP = Map.<String, Integer>ofEntries(
            Map.entry("left", GLFW_KEY_LEFT),
            Map.entry("right", GLFW_KEY_RIGHT),
            Map.entry("up", GLFW_KEY_UP),
            Map.entry("down", GLFW_KEY_DOWN),

            Map.entry("home", GLFW_KEY_HOME),
            Map.entry("end", GLFW_KEY_END),
            Map.entry("pageup", GLFW_KEY_PAGE_UP),
            Map.entry("pagedown", GLFW_KEY_PAGE_DOWN),

            Map.entry("backspace", GLFW_KEY_BACKSPACE),
            Map.entry("delete", GLFW_KEY_DELETE),
            Map.entry("enter", GLFW_KEY_ENTER),
            Map.entry("escape", GLFW_KEY_ESCAPE),
            Map.entry("tab", GLFW_KEY_TAB),
            Map.entry("insert", GLFW_KEY_INSERT),
            Map.entry("print_screen", GLFW_KEY_PRINT_SCREEN),

            Map.entry("pause", GLFW_KEY_PAUSE),
            Map.entry("menu", GLFW_KEY_MENU),

            Map.entry("0", GLFW_KEY_0),
            Map.entry("1", GLFW_KEY_1),
            Map.entry("2", GLFW_KEY_2),
            Map.entry("3", GLFW_KEY_3),
            Map.entry("4", GLFW_KEY_4),
            Map.entry("5", GLFW_KEY_5),
            Map.entry("6", GLFW_KEY_6),
            Map.entry("7", GLFW_KEY_7),
            Map.entry("8", GLFW_KEY_8),
            Map.entry("9", GLFW_KEY_9),

            Map.entry("a", GLFW_KEY_A),
            Map.entry("b", GLFW_KEY_B),
            Map.entry("c", GLFW_KEY_C),
            Map.entry("d", GLFW_KEY_D),
            Map.entry("e", GLFW_KEY_E),
            Map.entry("f", GLFW_KEY_F),
            Map.entry("g", GLFW_KEY_G),
            Map.entry("h", GLFW_KEY_H),
            Map.entry("i", GLFW_KEY_I),
            Map.entry("j", GLFW_KEY_J),
            Map.entry("k", GLFW_KEY_K),
            Map.entry("l", GLFW_KEY_L),
            Map.entry("m", GLFW_KEY_M),
            Map.entry("n", GLFW_KEY_N),
            Map.entry("o", GLFW_KEY_O),
            Map.entry("p", GLFW_KEY_P),
            Map.entry("q", GLFW_KEY_Q),
            Map.entry("r", GLFW_KEY_R),
            Map.entry("s", GLFW_KEY_S),
            Map.entry("t", GLFW_KEY_T),
            Map.entry("u", GLFW_KEY_U),
            Map.entry("v", GLFW_KEY_V),
            Map.entry("w", GLFW_KEY_W),
            Map.entry("x", GLFW_KEY_X),
            Map.entry("y", GLFW_KEY_Y),
            Map.entry("z", GLFW_KEY_Z),

            Map.entry("backtick", GLFW_KEY_GRAVE_ACCENT),
            Map.entry("hyphen", GLFW_KEY_MINUS),
            Map.entry("left_bracket", GLFW_KEY_LEFT_BRACKET),
            Map.entry("right_bracket", GLFW_KEY_RIGHT_BRACKET),
            Map.entry("forwardslash", GLFW_KEY_SLASH),
            Map.entry("equals", GLFW_KEY_EQUAL),
            Map.entry("apostrophe", GLFW_KEY_APOSTROPHE),
            Map.entry("semicolon", GLFW_KEY_SEMICOLON),
            Map.entry("comma", GLFW_KEY_COMMA),
            Map.entry("period", GLFW_KEY_PERIOD),
            Map.entry("backslash", GLFW_KEY_BACKSLASH),

            Map.entry("f1", GLFW_KEY_F1),
            Map.entry("f2", GLFW_KEY_F2),
            Map.entry("f3", GLFW_KEY_F3),
            Map.entry("f4", GLFW_KEY_F4),
            Map.entry("f5", GLFW_KEY_F5),
            Map.entry("f6", GLFW_KEY_F6),
            Map.entry("f7", GLFW_KEY_F7),
            Map.entry("f8", GLFW_KEY_F8),
            Map.entry("f9", GLFW_KEY_F9),
            Map.entry("f10", GLFW_KEY_F10),
            Map.entry("f11", GLFW_KEY_F11),
            Map.entry("f12", GLFW_KEY_F12),
            Map.entry("f13", GLFW_KEY_F13),
            Map.entry("f14", GLFW_KEY_F14),
            Map.entry("f15", GLFW_KEY_F15),
            Map.entry("f16", GLFW_KEY_F16),
            Map.entry("f17", GLFW_KEY_F17),
            Map.entry("f18", GLFW_KEY_F18),
            Map.entry("f19", GLFW_KEY_F19),
            Map.entry("f20", GLFW_KEY_F20),
            Map.entry("f21", GLFW_KEY_F21),
            Map.entry("f22", GLFW_KEY_F22),
            Map.entry("f23", GLFW_KEY_F23),
            Map.entry("f24", GLFW_KEY_F24),
            Map.entry("f25", GLFW_KEY_F25),

            Map.entry("numpad_0", GLFW_KEY_KP_0),
            Map.entry("numpad_1", GLFW_KEY_KP_1),
            Map.entry("numpad_2", GLFW_KEY_KP_2),
            Map.entry("numpad_3", GLFW_KEY_KP_3),
            Map.entry("numpad_4", GLFW_KEY_KP_4),
            Map.entry("numpad_5", GLFW_KEY_KP_5),
            Map.entry("numpad_6", GLFW_KEY_KP_6),
            Map.entry("numpad_7", GLFW_KEY_KP_7),
            Map.entry("numpad_8", GLFW_KEY_KP_8),
            Map.entry("numpad_9", GLFW_KEY_KP_9),
            Map.entry("numpad_slash", GLFW_KEY_KP_DIVIDE),
            Map.entry("numpad_star", GLFW_KEY_KP_MULTIPLY),
            Map.entry("numpad_minus", GLFW_KEY_KP_SUBTRACT),
            Map.entry("numpad_plus", GLFW_KEY_KP_ADD),
            Map.entry("numpad_enter", GLFW_KEY_KP_ENTER),
            Map.entry("numpad_dot", GLFW_KEY_KP_DECIMAL),
            Map.entry("numpad_equals", GLFW_KEY_KP_EQUAL),

            Map.entry("space", GLFW_KEY_SPACE),

            Map.entry("caps_lock", GLFW_KEY_CAPS_LOCK),
            Map.entry("num_lock", GLFW_KEY_NUM_LOCK),
            Map.entry("scroll_lock", GLFW_KEY_SCROLL_LOCK)
    );

    private KeyCodeRegistry() {
    }

    public static Map<String, Integer> getKeyMap() {
        return KEY_MAP;
    }
}
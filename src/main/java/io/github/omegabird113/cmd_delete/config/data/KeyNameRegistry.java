package io.github.omegabird113.cmd_delete.config.data;

import org.jspecify.annotations.NonNull;

import java.util.Map;

import static org.lwjgl.sdl.SDLScancode.*;

public final class KeyNameRegistry {
    private static final Map<@NonNull String, @NonNull Integer> KEY_MAP = Map.<String, Integer>ofEntries(
            Map.entry("left", SDL_SCANCODE_LEFT),
            Map.entry("right", SDL_SCANCODE_RIGHT),
            Map.entry("up", SDL_SCANCODE_UP),
            Map.entry("down", SDL_SCANCODE_DOWN),

            Map.entry("home", SDL_SCANCODE_HOME),
            Map.entry("end", SDL_SCANCODE_END),
            Map.entry("pageup", SDL_SCANCODE_PAGEUP),
            Map.entry("pagedown", SDL_SCANCODE_PAGEDOWN),

            Map.entry("backspace", SDL_SCANCODE_BACKSPACE),
            Map.entry("delete", SDL_SCANCODE_DELETE),
            Map.entry("enter", SDL_SCANCODE_RETURN),
            Map.entry("escape", SDL_SCANCODE_ESCAPE),
            Map.entry("tab", SDL_SCANCODE_TAB),
            Map.entry("insert", SDL_SCANCODE_INSERT),
            Map.entry("print_screen", SDL_SCANCODE_PRINTSCREEN),

            Map.entry("pause", SDL_SCANCODE_PAUSE),
            Map.entry("menu", SDL_SCANCODE_MENU),

            Map.entry("0", SDL_SCANCODE_0),
            Map.entry("1", SDL_SCANCODE_1),
            Map.entry("2", SDL_SCANCODE_2),
            Map.entry("3", SDL_SCANCODE_3),
            Map.entry("4", SDL_SCANCODE_4),
            Map.entry("5", SDL_SCANCODE_5),
            Map.entry("6", SDL_SCANCODE_6),
            Map.entry("7", SDL_SCANCODE_7),
            Map.entry("8", SDL_SCANCODE_8),
            Map.entry("9", SDL_SCANCODE_9),

            Map.entry("a", SDL_SCANCODE_A),
            Map.entry("b", SDL_SCANCODE_B),
            Map.entry("c", SDL_SCANCODE_C),
            Map.entry("d", SDL_SCANCODE_D),
            Map.entry("e", SDL_SCANCODE_E),
            Map.entry("f", SDL_SCANCODE_F),
            Map.entry("g", SDL_SCANCODE_G),
            Map.entry("h", SDL_SCANCODE_H),
            Map.entry("i", SDL_SCANCODE_I),
            Map.entry("j", SDL_SCANCODE_J),
            Map.entry("k", SDL_SCANCODE_K),
            Map.entry("l", SDL_SCANCODE_L),
            Map.entry("m", SDL_SCANCODE_M),
            Map.entry("n", SDL_SCANCODE_N),
            Map.entry("o", SDL_SCANCODE_O),
            Map.entry("p", SDL_SCANCODE_P),
            Map.entry("q", SDL_SCANCODE_Q),
            Map.entry("r", SDL_SCANCODE_R),
            Map.entry("s", SDL_SCANCODE_S),
            Map.entry("t", SDL_SCANCODE_T),
            Map.entry("u", SDL_SCANCODE_U),
            Map.entry("v", SDL_SCANCODE_V),
            Map.entry("w", SDL_SCANCODE_W),
            Map.entry("x", SDL_SCANCODE_X),
            Map.entry("y", SDL_SCANCODE_Y),
            Map.entry("z", SDL_SCANCODE_Z),

            Map.entry("backtick", SDL_SCANCODE_GRAVE),
            Map.entry("hyphen", SDL_SCANCODE_MINUS),
            Map.entry("left_bracket", SDL_SCANCODE_LEFTBRACKET),
            Map.entry("right_bracket", SDL_SCANCODE_RIGHTBRACKET),
            Map.entry("forwardslash", SDL_SCANCODE_SLASH),
            Map.entry("equals", SDL_SCANCODE_EQUALS),
            Map.entry("apostrophe", SDL_SCANCODE_APOSTROPHE),
            Map.entry("semicolon", SDL_SCANCODE_SEMICOLON),
            Map.entry("comma", SDL_SCANCODE_COMMA),
            Map.entry("period", SDL_SCANCODE_PERIOD),
            Map.entry("backslash", SDL_SCANCODE_BACKSLASH),

            Map.entry("f1", SDL_SCANCODE_F1),
            Map.entry("f2", SDL_SCANCODE_F2),
            Map.entry("f3", SDL_SCANCODE_F3),
            Map.entry("f4", SDL_SCANCODE_F4),
            Map.entry("f5", SDL_SCANCODE_F5),
            Map.entry("f6", SDL_SCANCODE_F6),
            Map.entry("f7", SDL_SCANCODE_F7),
            Map.entry("f8", SDL_SCANCODE_F8),
            Map.entry("f9", SDL_SCANCODE_F9),
            Map.entry("f10", SDL_SCANCODE_F10),
            Map.entry("f11", SDL_SCANCODE_F11),
            Map.entry("f12", SDL_SCANCODE_F12),
            Map.entry("f13", SDL_SCANCODE_F13),
            Map.entry("f14", SDL_SCANCODE_F14),
            Map.entry("f15", SDL_SCANCODE_F15),
            Map.entry("f16", SDL_SCANCODE_F16),
            Map.entry("f17", SDL_SCANCODE_F17),
            Map.entry("f18", SDL_SCANCODE_F18),
            Map.entry("f19", SDL_SCANCODE_F19),
            Map.entry("f20", SDL_SCANCODE_F20),
            Map.entry("f21", SDL_SCANCODE_F21),
            Map.entry("f22", SDL_SCANCODE_F22),
            Map.entry("f23", SDL_SCANCODE_F23),
            Map.entry("f24", SDL_SCANCODE_F24),

            Map.entry("numpad_0", SDL_SCANCODE_KP_0),
            Map.entry("numpad_1", SDL_SCANCODE_KP_1),
            Map.entry("numpad_2", SDL_SCANCODE_KP_2),
            Map.entry("numpad_3", SDL_SCANCODE_KP_3),
            Map.entry("numpad_4", SDL_SCANCODE_KP_4),
            Map.entry("numpad_5", SDL_SCANCODE_KP_5),
            Map.entry("numpad_6", SDL_SCANCODE_KP_6),
            Map.entry("numpad_7", SDL_SCANCODE_KP_7),
            Map.entry("numpad_8", SDL_SCANCODE_KP_8),
            Map.entry("numpad_9", SDL_SCANCODE_KP_9),
            Map.entry("numpad_slash", SDL_SCANCODE_KP_DIVIDE),
            Map.entry("numpad_star", SDL_SCANCODE_KP_MULTIPLY),
            Map.entry("numpad_minus", SDL_SCANCODE_KP_MINUS),
            Map.entry("numpad_plus", SDL_SCANCODE_KP_PLUS),
            Map.entry("numpad_enter", SDL_SCANCODE_KP_ENTER),
            Map.entry("numpad_dot", SDL_SCANCODE_KP_DECIMAL),
            Map.entry("numpad_equals", SDL_SCANCODE_KP_EQUALS),

            Map.entry("space", SDL_SCANCODE_SPACE),

            Map.entry("caps_lock", SDL_SCANCODE_CAPSLOCK),
            Map.entry("num_lock", SDL_SCANCODE_NUMLOCKCLEAR),
            Map.entry("scroll_lock", SDL_SCANCODE_SCROLLLOCK)
    );

    private KeyNameRegistry() {
    }

    public static @NonNull Map<@NonNull String, @NonNull Integer> getKeyMap() {
        return KEY_MAP;
    }

    public static @NonNull String getDumpString() {
        return "{"
                + String.join(", ", KEY_MAP.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).toArray(String[]::new))
                + "}";
    }
}
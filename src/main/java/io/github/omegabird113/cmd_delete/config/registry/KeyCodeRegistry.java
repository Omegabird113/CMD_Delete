package io.github.omegabird113.cmd_delete.config.registry;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class KeyCodeRegistry {
    public static Map<String, Integer> get() {
        Map<String, Integer> map = new HashMap<>();

        map.put("left", GLFW_KEY_LEFT);
        map.put("right", GLFW_KEY_RIGHT);
        map.put("up", GLFW_KEY_UP);
        map.put("down", GLFW_KEY_DOWN);

        map.put("home", GLFW_KEY_HOME);
        map.put("end", GLFW_KEY_END);
        map.put("pageup", GLFW_KEY_PAGE_UP);
        map.put("pagedown", GLFW_KEY_PAGE_DOWN);

        map.put("backspace", GLFW_KEY_BACKSPACE);
        map.put("delete", GLFW_KEY_DELETE);
        map.put("enter", GLFW_KEY_ENTER);
        map.put("escape", GLFW_KEY_ESCAPE);
        map.put("tab", GLFW_KEY_TAB);
        map.put("insert", GLFW_KEY_INSERT);
        map.put("print_screen", GLFW_KEY_PRINT_SCREEN);

        map.put("pause",  GLFW_KEY_PAUSE);
        map.put("menu", GLFW_KEY_MENU);

        map.put("0", GLFW_KEY_0);
        map.put("1", GLFW_KEY_1);
        map.put("2", GLFW_KEY_2);
        map.put("3", GLFW_KEY_3);
        map.put("4", GLFW_KEY_4);
        map.put("5", GLFW_KEY_5);
        map.put("6", GLFW_KEY_6);
        map.put("7", GLFW_KEY_7);
        map.put("8", GLFW_KEY_8);
        map.put("9", GLFW_KEY_9);

        map.put("a", GLFW_KEY_A);
        map.put("b", GLFW_KEY_B);
        map.put("c", GLFW_KEY_C);
        map.put("d", GLFW_KEY_D);
        map.put("e", GLFW_KEY_E);
        map.put("f", GLFW_KEY_F);
        map.put("g", GLFW_KEY_G);
        map.put("h", GLFW_KEY_H);
        map.put("i", GLFW_KEY_I);
        map.put("j", GLFW_KEY_J);
        map.put("k", GLFW_KEY_K);
        map.put("l", GLFW_KEY_L);
        map.put("m", GLFW_KEY_M);
        map.put("n", GLFW_KEY_N);
        map.put("o", GLFW_KEY_O);
        map.put("p", GLFW_KEY_P);
        map.put("q", GLFW_KEY_Q);
        map.put("r", GLFW_KEY_R);
        map.put("s", GLFW_KEY_S);
        map.put("t", GLFW_KEY_T);
        map.put("u", GLFW_KEY_U);
        map.put("v", GLFW_KEY_V);
        map.put("w", GLFW_KEY_W);
        map.put("x", GLFW_KEY_X);
        map.put("y", GLFW_KEY_Y);
        map.put("z", GLFW_KEY_Z);

        map.put("backtick", GLFW_KEY_GRAVE_ACCENT);
        map.put("hyphen", GLFW_KEY_MINUS);
        map.put("left_bracket", GLFW_KEY_LEFT_BRACKET);
        map.put("right_bracket", GLFW_KEY_RIGHT_BRACKET);
        map.put("forwardslash", GLFW_KEY_SLASH);
        map.put("equals", GLFW_KEY_EQUAL);
        map.put("apostrophe", GLFW_KEY_APOSTROPHE);
        map.put("semicolon", GLFW_KEY_SEMICOLON);
        map.put("comma", GLFW_KEY_COMMA);
        map.put("period", GLFW_KEY_PERIOD);
        map.put("backslash", GLFW_KEY_BACKSLASH);

        map.put("f1", GLFW_KEY_F1);
        map.put("f2", GLFW_KEY_F2);
        map.put("f3", GLFW_KEY_F3);
        map.put("f4", GLFW_KEY_F4);
        map.put("f5", GLFW_KEY_F5);
        map.put("f6", GLFW_KEY_F6);
        map.put("f7", GLFW_KEY_F7);
        map.put("f8", GLFW_KEY_F8);
        map.put("f9", GLFW_KEY_F9);
        map.put("f10", GLFW_KEY_F10);
        map.put("f11", GLFW_KEY_F11);
        map.put("f12", GLFW_KEY_F12);
        map.put("f13", GLFW_KEY_F13);
        map.put("f14", GLFW_KEY_F14);
        map.put("f15", GLFW_KEY_F15);
        map.put("f16", GLFW_KEY_F16);
        map.put("f17", GLFW_KEY_F17);
        map.put("f18", GLFW_KEY_F18);
        map.put("f19", GLFW_KEY_F19);
        map.put("f20", GLFW_KEY_F20);
        map.put("f21", GLFW_KEY_F21);
        map.put("f22", GLFW_KEY_F22);
        map.put("f23", GLFW_KEY_F23);
        map.put("f24", GLFW_KEY_F24);
        map.put("f25", GLFW_KEY_F25);

        map.put("numpad_0", GLFW_KEY_KP_0);
        map.put("numpad_1", GLFW_KEY_KP_1);
        map.put("numpad_2", GLFW_KEY_KP_2);
        map.put("numpad_3", GLFW_KEY_KP_3);
        map.put("numpad_4", GLFW_KEY_KP_4);
        map.put("numpad_5", GLFW_KEY_KP_5);
        map.put("numpad_6", GLFW_KEY_KP_6);
        map.put("numpad_7", GLFW_KEY_KP_7);
        map.put("numpad_8", GLFW_KEY_KP_8);
        map.put("numpad_9", GLFW_KEY_KP_9);
        map.put("numpad_slash", GLFW_KEY_KP_DIVIDE);
        map.put("numpad_star", GLFW_KEY_KP_MULTIPLY);
        map.put("numpad_minus", GLFW_KEY_KP_SUBTRACT);
        map.put("numpad_plus", GLFW_KEY_KP_ADD);
        map.put("numpad_enter", GLFW_KEY_KP_ENTER);
        map.put("numpad_dot", GLFW_KEY_KP_DECIMAL);
        map.put("numpad_equals", GLFW_KEY_KP_EQUAL);

        map.put("space", GLFW_KEY_SPACE);

        map.put("caps_lock", GLFW_KEY_CAPS_LOCK);
        map.put("num_lock", GLFW_KEY_NUM_LOCK);
        map.put("scroll_lock", GLFW_KEY_SCROLL_LOCK);

        return map;
    }
}

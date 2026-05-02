package io.github.omegabird113.cmd_delete.mapping;

abstract public class NavMappingManager {
    private static INavMapping current;
    private static boolean useCustomMapping = false;

    public static INavMapping getCurrent() {
        return current;
    }

    public static void LoadMapping() {
        current = getOsMapping();
    }

    public static Os getOs() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return Os.MAC;
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Os.WINDOWS;
        } else {
            return Os.LINUX;
        }
    }

    public static INavMapping getOsMapping() {
        return switch(getOs()) {
            case MAC -> new MacNavMapping();
            case WINDOWS -> new WindowsNavMapping();
            case LINUX -> new LinuxNavMapping();
        };
    }
}

package io.github.omegabird113.cmd_delete.mappings;

abstract public class NavMappingsManager {
    private static INavMappings current;
    private static boolean useCustomMapping = false;

    public static INavMappings getCurrent() {
        return current;
    }

    public static void LoadMappings() {
        current = getOsMappings();
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

    private static INavMappings getOsMappings() {
        return switch(getOs()) {
            case MAC -> new MacNavMappings();
            case LINUX -> new LinuxNavMappings();
            default -> new WindowsNavMappings();
        };
    }
}

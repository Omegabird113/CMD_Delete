package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.*;

public final class MappingsRegistry {
    private final Map<KeyCombo, NavAction> registry;
    private final Map<KeyCombo, NavAction> disabledRegistry;
    private final List<Os> systems;
    private final String inherits;
    private final String name;
    private final String author;
    private final String description;
    private final String version;
    private final String id;

    MappingsRegistry(Map<KeyCombo, NavAction> registry, Collection<Os> systems, String inherits, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.disabledRegistry = null;
        this.systems = List.copyOf(systems);
        this.inherits = inherits;
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
    }

    MappingsRegistry(Map<KeyCombo, NavAction> registry, Map<KeyCombo, NavAction> disabledRegistry, Collection<Os> systems, String inherits, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.disabledRegistry = Map.copyOf(disabledRegistry);
        this.systems = List.copyOf(systems);
        this.inherits = inherits;
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
    }

    Map<KeyCombo, NavAction> getInternalRegistry() {
        return registry;
    }

    Optional<Map<KeyCombo, NavAction>> getInternalDisabledRegistry() {
        return Optional.ofNullable(disabledRegistry);
    }

    public NavAction get(KeyCombo key) {
        return registry.get(key);
    }

    public NavAction[] getValues() {
        return registry.values().toArray(NavAction[]::new);
    }

    public List<Os> getSystems() {
        return systems;
    }

    public String getInherits() {
        return inherits;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return registry.size();
    }

    private String registryStringUtil(Map<KeyCombo, NavAction> registry) {
        if (registry == null)
            return "null";
        if (registry.isEmpty())
            return "{\n\t<empty>\n}";

        final Map<NavAction, ArrayList<KeyCombo>> local = new HashMap<>();
        for (Map.Entry<KeyCombo, NavAction> entry : registry.entrySet())
            local.computeIfAbsent(entry.getValue(), _ -> new ArrayList<>()).add(entry.getKey());

        final ArrayList<String> stringEntries = new ArrayList<>();
        for (Map.Entry<NavAction, ArrayList<KeyCombo>> entry : local.entrySet())
            stringEntries.add("("
                    + String.join(", ", entry.getValue().stream().map(KeyCombo::toString).toList())
                    + " -> "
                    + entry.getKey()
                    + ")"
            );

        return "{\n\t\t" + String.join(",\n\t\t", stringEntries) + "\n\t}";
    }

    @Override
    public String toString() {
        return String.format("""
                        MappingsRegistry(
                            name="%s",
                            author="%s",
                            description="%s",
                            version="%s",
                            id="%s",
                            inherits="%s",
                            hashCode=%d,
                            registry=%s,
                            disabledRegistry=%s
                        )""",
                name, author, description, version, id, inherits,
                hashCode(),
                registryStringUtil(registry),
                registryStringUtil(disabledRegistry));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MappingsRegistry mr))
            return false;

        return Objects.equals(id, mr.id)
                && Objects.equals(version, mr.version)
                && Objects.equals(author, mr.author)
                && Objects.equals(description, mr.description)
                && Objects.equals(registry, mr.registry)
                && Objects.equals(name, mr.name)
                && Objects.equals(disabledRegistry, mr.disabledRegistry)
                && Objects.equals(systems, mr.systems)
                && Objects.equals(inherits, mr.inherits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                registry,
                disabledRegistry,
                systems,
                inherits,
                name,
                author,
                description,
                version,
                id
        );
    }
}

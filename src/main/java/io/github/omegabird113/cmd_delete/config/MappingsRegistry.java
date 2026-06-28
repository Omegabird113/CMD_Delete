package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public final class MappingsRegistry {
    private final @NonNull Map<@NonNull KeyCombo, @NonNull NavAction> registry;
    private final @Nullable Map<@NonNull KeyCombo, @NonNull NavAction> disabledRegistry;
    private final @NonNull List<@NonNull Os> systems;
    private final @NonNull String inherits;
    private final @NonNull String name;
    private final @NonNull String author;
    private final @NonNull String description;
    private final @NonNull String version;
    private final @NonNull String id;

    MappingsRegistry(Map<@NonNull KeyCombo, @NonNull NavAction> registry, @Nullable Map<@NonNull KeyCombo, @NonNull NavAction> disabledRegistry, Collection<@NonNull Os> systems, @NonNull String inherits, @NonNull String name, @NonNull String author, @NonNull String description, @NonNull String version, @NonNull String id) {
        this.registry = Map.copyOf(registry);
        if (disabledRegistry != null)
            this.disabledRegistry = Map.copyOf(disabledRegistry);
        else
            this.disabledRegistry = null;
        this.systems = List.copyOf(systems);
        this.inherits = inherits;
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
    }

    MappingsRegistry(Map<@NonNull KeyCombo, @NonNull NavAction> registry, Collection<@NonNull Os> systems, @NonNull String inherits, @NonNull String name, @NonNull String author, @NonNull String description, @NonNull String version, @NonNull String id) {
        this(registry, null, systems, inherits, name, author, description, version, id);
    }

    @NonNull Map<@NonNull KeyCombo, @NonNull NavAction> getInternalRegistry() {
        return registry;
    }

    @Contract(pure = true)
    @NonNull Optional<Map<@NonNull KeyCombo, @NonNull NavAction>> getInternalDisabledRegistry() {
        return Optional.ofNullable(disabledRegistry);
    }

    public @Nullable NavAction get(@NonNull KeyCombo key) {
        return registry.get(key);
    }

    public NavAction[] getValues() {
        return registry.values().toArray(NavAction[]::new);
    }

    public @NonNull List<@NonNull Os> getSystems() {
        return systems;
    }

    public @NonNull String getInherits() {
        return inherits;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getAuthor() {
        return author;
    }

    public @NonNull String getDescription() {
        return description;
    }

    public @NonNull String getVersion() {
        return version;
    }

    public @NonNull String getId() {
        return id;
    }

    public int getSize() {
        return registry.size();
    }

    @Contract(pure = true)
    private @NonNull String registryStringUtil(@Nullable Map<@NonNull KeyCombo, @NonNull NavAction> registry) {
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

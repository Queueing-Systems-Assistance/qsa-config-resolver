package com.unideb.qsa.config.resolver.domain.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.unideb.qsa.config.resolver.domain.exception.ConfigPackException;

/**
 * Immutable object for a config pack.
 */
public final class ConfigPack {

    private final Map<ConfigKey, ConfigDefinition> keysToDefinitions;
    private final Map<String, Collection<ConfigDefinition>> namesToDefinitions;

    public ConfigPack(Map<ConfigKey, ConfigDefinition> keysToDefinitions) {
        if (keysToDefinitions == null) {
            throw new ConfigPackException("Config keys cannot be null!");
        }
        this.keysToDefinitions = keysToDefinitions;
        this.namesToDefinitions = getNamesToDefinitions(keysToDefinitions);
    }

    /**
     * Get the configs mapped as config key - config definition.
     * @return a map, where the key is the config key and the value is the corresponding definition.
     */
    public Map<ConfigKey, ConfigDefinition> getKeysToDefinitions() {
        return this.keysToDefinitions;
    }

    /**
     * Get the configs mapped as config name - config definition.
     * @return a map, where the key is the config file name and the value is the corresponding definition list.
     */
    public Map<String, Collection<ConfigDefinition>> getNamesToDefinitions() {
        return namesToDefinitions;
    }

    @Override
    public String toString() {
        return "ConfigPack{"
               + "keysToDefinitions=" + keysToDefinitions
               + ", namesToDefinitions=" + namesToDefinitions
               + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (ConfigPack) o;
        return keysToDefinitions.equals(that.keysToDefinitions)
               && namesToDefinitions.equals(that.namesToDefinitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keysToDefinitions, namesToDefinitions);
    }

    private Map<String, Collection<ConfigDefinition>> getNamesToDefinitions(Map<ConfigKey, ConfigDefinition> keysToDefinitions) {
        var namesToDefinitions = new HashMap<String, Collection<ConfigDefinition>>();
        for (var configDefinition : keysToDefinitions.values()) {
            var configNameName = configDefinition.name();
            if (namesToDefinitions.containsKey(configNameName)) {
                var definitions = new ArrayList<>(namesToDefinitions.get(configNameName));
                definitions.add(configDefinition);
                namesToDefinitions.put(configNameName, definitions);
            } else {
                namesToDefinitions.put(configNameName, List.of(configDefinition));
            }
        }
        return namesToDefinitions;
    }
}

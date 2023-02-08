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

    private final Map<ConfigKey, ConfigDefinition> keyToDefinitionsMap;
    private final Map<String, Collection<ConfigDefinition>> nameToDefinitions;

    public ConfigPack(Map<ConfigKey, ConfigDefinition> keyToDefinitionsMap) {
        if (keyToDefinitionsMap == null) {
            throw new ConfigPackException("Config keys cannot be null!");
        }
        this.keyToDefinitionsMap = keyToDefinitionsMap;
        this.nameToDefinitions = createNameToDefinitionsMap(keyToDefinitionsMap);
    }

    /**
     * Get the configs mapped as config key - config definition.
     * @return a map, where the key is the config key and the value is the corresponding definition.
     */
    public Map<ConfigKey, ConfigDefinition> getKeyToDefinitionsMap() {
        return this.keyToDefinitionsMap;
    }

    /**
     * Get the configs mapped as config name - config definition.
     * @return a map, where the key is the config file name and the value is the corresponding definition list.
     */
    public Map<String, Collection<ConfigDefinition>> getNameToDefinitions() {
        return nameToDefinitions;
    }

    @Override
    public String toString() {
        return "ConfigPack{"
               + "configKeyToConfigDefinitionMap=" + keyToDefinitionsMap
               + ", configNameToConfigDefinitionsMap=" + nameToDefinitions
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
        return keyToDefinitionsMap.equals(that.keyToDefinitionsMap)
               && nameToDefinitions.equals(that.nameToDefinitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyToDefinitionsMap, nameToDefinitions);
    }

    private Map<String, Collection<ConfigDefinition>> createNameToDefinitionsMap(Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
        var configNameToConfigDefinitionsMap = new HashMap<String, Collection<ConfigDefinition>>();
        for (var configDefinition : configKeyToConfigDefinitionMap.values()) {
            var configNameName = configDefinition.getName();
            if (configNameToConfigDefinitionsMap.containsKey(configNameName)) {
                var definitions = new ArrayList<>(configNameToConfigDefinitionsMap.get(configNameName));
                definitions.add(configDefinition);
                configNameToConfigDefinitionsMap.put(configNameName, definitions);
            } else {
                configNameToConfigDefinitionsMap.put(configNameName, List.of(configDefinition));
            }
        }
        return configNameToConfigDefinitionsMap;
    }
}

package com.unideb.qsa.domain.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.unideb.qsa.domain.exception.ConfigPackException;

/**
 * Immutable object for a config pack.
 */
public final class ConfigPack {

    private final Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap;
    private final Map<String, Collection<ConfigDefinition>> configNameToConfigDefinitionsMap;

    public ConfigPack(Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
        if (configKeyToConfigDefinitionMap == null) {
            throw new ConfigPackException("Config keys cannot be null!");
        }
        this.configKeyToConfigDefinitionMap = configKeyToConfigDefinitionMap;
        this.configNameToConfigDefinitionsMap = createConfigNameToConfigDefinitionsMap(configKeyToConfigDefinitionMap);
    }

    /**
     * Get the configs mapped as config key - config definition.
     * @return a map, where the key is the config key and the value is the corresponding definition.
     */
    public Map<ConfigKey, ConfigDefinition> getConfigKeyToConfigDefinitionMap() {
        return this.configKeyToConfigDefinitionMap;
    }

    /**
     * Get the configs mapped as config name - config definition.
     * @return a map, where the key is the config file name and the value is the corresponding definition list.
     */
    public Map<String, Collection<ConfigDefinition>> getConfigNameToConfigDefinitionsMap() {
        return configNameToConfigDefinitionsMap;
    }

    @Override
    public String toString() {
        return "ConfigPack{"
               + "configKeyToConfigDefinitionMap=" + configKeyToConfigDefinitionMap
               + ", configNameToConfigDefinitionsMap=" + configNameToConfigDefinitionsMap
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
        return configKeyToConfigDefinitionMap.equals(that.configKeyToConfigDefinitionMap)
               && configNameToConfigDefinitionsMap.equals(that.configNameToConfigDefinitionsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configKeyToConfigDefinitionMap, configNameToConfigDefinitionsMap);
    }

    private Map<String, Collection<ConfigDefinition>> createConfigNameToConfigDefinitionsMap(Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
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

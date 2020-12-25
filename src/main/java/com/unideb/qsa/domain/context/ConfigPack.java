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

    private ConfigPack(Builder builder) {
        this.configKeyToConfigDefinitionMap = builder.configKeyToConfigDefinitionMap;
        this.configNameToConfigDefinitionsMap = createConfigNameToConfigDefinitionsMap(configKeyToConfigDefinitionMap);
    }

    public Map<ConfigKey, ConfigDefinition> getConfigKeyToConfigDefinitionMap() {
        return this.configKeyToConfigDefinitionMap;
    }

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
        ConfigPack that = (ConfigPack) o;
        return configKeyToConfigDefinitionMap.equals(that.configKeyToConfigDefinitionMap)
               && configNameToConfigDefinitionsMap.equals(that.configNameToConfigDefinitionsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configKeyToConfigDefinitionMap, configNameToConfigDefinitionsMap);
    }

    private Map<String, Collection<ConfigDefinition>> createConfigNameToConfigDefinitionsMap(
            Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
        Map<String, Collection<ConfigDefinition>> configNameToConfigDefinitionsMap = new HashMap<>();
        for (ConfigDefinition configDefinition : configKeyToConfigDefinitionMap.values()) {
            String configNameName = configDefinition.getName();
            if (configNameToConfigDefinitionsMap.containsKey(configNameName)) {
                List<ConfigDefinition> definitions = new ArrayList<>(configNameToConfigDefinitionsMap.get(configNameName));
                definitions.add(configDefinition);
                configNameToConfigDefinitionsMap.put(configNameName, definitions);
            } else {
                configNameToConfigDefinitionsMap.put(configNameName, List.of(configDefinition));
            }
        }
        return configNameToConfigDefinitionsMap;
    }

    /**
     * Builder for {@link ConfigPack}.
     */
    public static class Builder {

        private final Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap;

        public Builder(Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
            if (configKeyToConfigDefinitionMap == null) {
                throw new ConfigPackException("Config keys cannot be null!");
            }

            this.configKeyToConfigDefinitionMap = configKeyToConfigDefinitionMap;
        }

        /**
         * Builds a {@link ConfigPack}.
         * @return {@link ConfigPack}
         */
        public ConfigPack build() {
            return new ConfigPack(this);
        }
    }
}

package com.unideb.qsa.domain.context;


import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

/**
 * Immutable object for a config pack.
 */
public final class ConfigPack {

    private final ImmutableMap<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap;
    private final ImmutableSortedMap<String, ImmutableCollection<ConfigDefinition>> configNameToConfigDefinitionsMap;

    private ConfigPack(Builder builder) {
        this.configKeyToConfigDefinitionMap = builder.configKeyToConfigDefinitionMap;
        this.configNameToConfigDefinitionsMap = createConfigNameToConfigDefinitionsMap(configKeyToConfigDefinitionMap);
    }

    public ImmutableMap<ConfigKey, ConfigDefinition> getConfigKeyToConfigDefinitionMap() {
        return this.configKeyToConfigDefinitionMap;
    }

    public ImmutableSortedMap<String, ImmutableCollection<ConfigDefinition>> getConfigNameToConfigDefinitionsMap() {
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

    private ImmutableSortedMap<String, ImmutableCollection<ConfigDefinition>> createConfigNameToConfigDefinitionsMap(
            ImmutableMap<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
        Map<String, ImmutableCollection<ConfigDefinition>> configNameToConfigDefinitionsMap = new HashMap<>();
        for (ConfigDefinition configDefinition : configKeyToConfigDefinitionMap.values()) {
            String configNameName = configDefinition.getName();
            if (configNameToConfigDefinitionsMap.containsKey(configNameName)) {
                configNameToConfigDefinitionsMap.put(configNameName, new ImmutableList.Builder<ConfigDefinition>()
                        .addAll(configNameToConfigDefinitionsMap.get(configNameName))
                        .add(configDefinition)
                        .build());
            } else {
                configNameToConfigDefinitionsMap.put(configNameName, ImmutableList.of(configDefinition));
            }
        }
        return ImmutableSortedMap
                .<String, ImmutableCollection<ConfigDefinition>>naturalOrder()
                .putAll(configNameToConfigDefinitionsMap)
                .build();
    }

    /**
     * Builder for {@link ConfigPack}.
     */
    public static class Builder {

        private final ImmutableMap<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap;

        public Builder(ImmutableMap<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap) {
            checkArgument(configKeyToConfigDefinitionMap != null);

            this.configKeyToConfigDefinitionMap = configKeyToConfigDefinitionMap;
        }

        /**
         * Builds a {@link ConfigPack}.
         *
         * @return {@link ConfigPack}
         */
        public ConfigPack build() {
            return new ConfigPack(this);
        }
    }
}

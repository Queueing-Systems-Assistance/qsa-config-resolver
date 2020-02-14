package com.unideb.qsa.domain.context;

import java.util.Objects;

import com.google.common.collect.ImmutableMap;

/**
 * Immutable object for representing a config name and qualifiers.
 */
public final class ConfigKey {

    private final String configName;
    private final ImmutableMap<String, String> qualifiers;

    public ConfigKey(String configName) {
        this(configName, null);
    }

    public ConfigKey(ConfigDefinition configDefinition) {
        this(configDefinition.getName(), configDefinition.getQualifiers());
    }

    public ConfigKey(String configName, ImmutableMap<String, String> qualifiers) {
        this.configName = configName;
        this.qualifiers = (qualifiers != null) ? qualifiers : ImmutableMap.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigKey configKey = (ConfigKey) o;
        return Objects.equals(configName, configKey.configName)
               && Objects.equals(qualifiers, configKey.qualifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configName, qualifiers);
    }

    @Override
    public String toString() {
        return "ConfigKey{"
               + "configName='" + configName + "'"
               + ", qualifiers=" + qualifiers
               + "}";
    }

    public String getConfigName() {
        return configName;
    }

    public ImmutableMap<String, String> getQualifiers() {
        return qualifiers;
    }
}

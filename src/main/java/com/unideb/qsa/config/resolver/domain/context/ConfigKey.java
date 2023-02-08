package com.unideb.qsa.config.resolver.domain.context;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable object for representing a config name and qualifiers.
 */
public final class ConfigKey {

    private final String configName;
    private final Map<String, String> qualifiers;

    public ConfigKey(String configName) {
        this(configName, null);
    }

    public ConfigKey(ConfigDefinition configDefinition) {
        this(configDefinition.getName(), configDefinition.getQualifiers());
    }

    public ConfigKey(String configName, Map<String, String> qualifiers) {
        this.configName = configName;
        this.qualifiers = (qualifiers != null) ? qualifiers : Map.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var configKey = (ConfigKey) o;
        return Objects.equals(configName, configKey.configName) && Objects.equals(qualifiers, configKey.qualifiers);
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

    /**
     * Same as the {@link ConfigDefinition#getName()}.
     * @return the config file name
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * Same as the {@link ConfigDefinition#getQualifiers()}.
     * @return a map, where the key is the config qualifier name, the value is the corresponding value.
     */
    public Map<String, String> getQualifiers() {
        return qualifiers;
    }
}

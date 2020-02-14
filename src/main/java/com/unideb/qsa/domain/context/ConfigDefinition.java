package com.unideb.qsa.domain.context;

import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;

import com.unideb.qsa.domain.exception.ConfigDefinitionException;

/**
 * Immutable object representing a config.
 */
public final class ConfigDefinition {

    private static final String NAME_EXCEPTION = "ConfigDefinition 'name' must not be null";

    private final String name;
    private final ImmutableSortedSet<ConfigValue> configValues;
    private final ImmutableMap<String, String> qualifiers;

    public ConfigDefinition(String name, ImmutableSortedSet<ConfigValue> configValues, ImmutableMap<String, String> qualifiers) {
        if (name == null) {
            throw new ConfigDefinitionException(NAME_EXCEPTION);
        }
        this.name = name;
        this.configValues = configValues;
        this.qualifiers = qualifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigDefinition that = (ConfigDefinition) o;
        return Objects.equals(name, that.name)
               && Objects.equals(configValues, that.configValues)
               && Objects.equals(qualifiers, that.qualifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configValues, qualifiers);
    }

    @Override
    public String toString() {
        return "ConfigDefinition{"
               + "name='" + name + "'"
               + ", configValues=" + configValues
               + ", qualifiers=" + qualifiers
               + "}";
    }

    public String getName() {
        return name;
    }

    public ImmutableSortedSet<ConfigValue> getConfigValues() {
        return configValues;
    }

    public ImmutableMap<String, String> getQualifiers() {
        return qualifiers;
    }

}

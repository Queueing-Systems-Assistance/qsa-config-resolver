package com.unideb.qsa.domain.context;

import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import com.unideb.qsa.domain.exception.ConfigValueException;

/**
 * Immutable object representing the value of a config and any qualifiers associated with it.
 */
public final class ConfigValue {

    private static final String VALUE_EXCEPTION = "ConfigValue 'value' must not be null";
    private static final String QUALIFIERS_EXCEPTION = "ConfigValue 'qualifiers' must not be null";

    private final String value;
    private final ImmutableMap<String, ImmutableSet<String>> qualifiers;

    public ConfigValue(String value, ImmutableMap<String, ImmutableSet<String>> qualifiers) {
        if (value == null) {
            throw new ConfigValueException(VALUE_EXCEPTION);
        }
        if (qualifiers == null) {
            throw new ConfigValueException(QUALIFIERS_EXCEPTION);
        }

        this.value = value;
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
        ConfigValue that = (ConfigValue) o;
        return Objects.equals(value, that.value)
               && Objects.equals(qualifiers, that.qualifiers);
    }

    @Override
    public String toString() {
        return "ConfigValue{"
               + "value='" + value + "'"
               + ", qualifiers=" + qualifiers
               + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, qualifiers);
    }

    public String getValue() {
        return value;
    }

    public ImmutableMap<String, ImmutableSet<String>> getQualifiers() {
        return qualifiers;
    }
}

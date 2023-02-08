package com.unideb.qsa.config.resolver.domain.context;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.unideb.qsa.config.resolver.domain.exception.ConfigValueException;

/**
 * Immutable object representing the value of a config and any qualifiers associated with it.
 */
public final class ConfigValue {

    private static final String VALUE_EXCEPTION = "ConfigValue 'value' must not be null";
    private static final String QUALIFIERS_EXCEPTION = "ConfigValue 'qualifiers' must not be null";

    private final String value;
    private final Map<String, Set<String>> qualifiers;

    public ConfigValue(String value, Map<String, Set<String>> qualifiers) {
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
        var that = (ConfigValue) o;
        return Objects.equals(value, that.value) && Objects.equals(qualifiers, that.qualifiers);
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

    /**
     * Get the config value. For example the following config will return with 'value of the config' string:
     * <pre>
     * {@code
     * {
     *   "value": "value of the config",
     *   "locale": [
     *     "hu"
     *   ]
     * }
     * }
     * </pre>
     * @return value of the config.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the config value qualifiers. For example in the following config the result will be a map, where 'locale' is the key and 'hu' is the value:
     * <pre>
     * {@code
     * {
     *   "value": "value of the config",
     *   "locale": [
     *     "hu"
     *   ]
     * }
     * }
     * </pre>
     * @return a map, where the keys are the configCondition values and the values are the corresponding values.
     */
    public Map<String, Set<String>> getQualifiers() {
        return qualifiers;
    }
}

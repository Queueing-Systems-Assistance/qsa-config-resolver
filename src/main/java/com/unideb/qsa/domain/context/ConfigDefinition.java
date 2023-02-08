package com.unideb.qsa.domain.context;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.unideb.qsa.domain.exception.ConfigDefinitionException;

/**
 * Immutable object representing a config.
 */
public final class ConfigDefinition {

    private static final String NAME_EXCEPTION = "ConfigDefinition [name] must not be null";

    private final String name;
    private final Set<ConfigValue> configValues;
    private final Map<String, String> qualifiers;

    public ConfigDefinition(String name, Set<ConfigValue> configValues, Map<String, String> qualifiers) {
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
        var that = (ConfigDefinition) o;
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

    /**
     * Get the config name. For example, this is the name of the config file:
     * <pre>
     * {@code
     * {
     *   "config": "CONFIG_NAME",
     *   "values": [
     *     {
     *       "value": ""
     *     }
     *   ]
     * }
     * }
     * </pre>
     * @return the config file name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the config values (both default and qualifier ones). These qualifiers only applies to the config values, not for the config file itself.
     * For example:
     * <pre>
     * {@code
     * {
     *   "config": "",
     *   "configCondition": [
     *     "locale"
     *   ],
     *   "values": [
     *     {
     *       "value": "Qualified config value",
     *       "locale": [
     *         "hu"
     *       ]
     *     },
     *     {
     *       "value": "Default config value"
     *     }
     *   ]
     * }
     * }
     * </pre>
     * @return config file values
     */
    public Set<ConfigValue> getConfigValues() {
        return configValues;
    }

    /**
     * Get the config file qualifiers. Not the value qualifiers (config conditions) but the config file conditions itself (eg.: name). For example:
     * <pre>
     * {@code
     * {
     *   "config": "NAME",
     *   "name": "configQualifier",
     *   "values": [
     *     {
     *       "value": ""
     *     }
     *   ]
     * }
     * }
     * </pre>
     * @return a map, where the key is the config qualifier name, the value is the corresponding value. In the case above: key - name, value - configQualifier
     */
    public Map<String, String> getQualifiers() {
        return qualifiers;
    }

}

package com.unideb.qsa.config.resolver.domain.context;

import java.util.Map;
import java.util.Set;

import com.unideb.qsa.config.resolver.domain.exception.ConfigValueException;

/**
 * Immutable object representing the value of a config and all qualifiers associated with it.
 * @param value The config value. For example the following config's value is 'value of the config':
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
 * @param qualifiers Config value's qualifiers. For example in the following config the result will be a map, where 'locale' is the key and 'hu' is the value:
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
 */
public record ConfigValue(String value, Map<String, Set<String>> qualifiers) {

    public ConfigValue {
        if (value == null) {
            throw new ConfigValueException("ConfigValue 'value' must not be null");
        }
        if (qualifiers == null) {
            throw new ConfigValueException("ConfigValue 'qualifiers' must not be null");
        }
    }
}

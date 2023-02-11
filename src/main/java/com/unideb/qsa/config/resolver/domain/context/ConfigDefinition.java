package com.unideb.qsa.config.resolver.domain.context;

import java.util.Map;
import java.util.Set;

import com.unideb.qsa.config.resolver.domain.exception.ConfigDefinitionException;

/**
 * Immutable object representing a config.
 * @param name Config name.
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
 * In this case, it is the "CONFIG_NAME"
 * @param configValues Both default and qualifier config values should be present.
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
 * @param qualifiers A map, where the key is the config qualifier name, the value is the corresponding value.
 * <pre>
 * {@code
 * {
 *   "config": "CONFIG_NAME",
 *   "name": "configQualifier",
 *   "values": [
 *     {
 *       "value": ""
 *     }
 *   ]
 * }
 * }
 * </pre>
 * In this case, [name - configQualifier]
 */
public record ConfigDefinition(String name, Set<ConfigValue> configValues, Map<String, String> qualifiers) {

    public ConfigDefinition {
        if (name == null) {
            throw new ConfigDefinitionException("ConfigDefinition [name] must not be null");
        }
    }
}

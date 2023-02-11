package com.unideb.qsa.config.resolver.domain.context;

import java.util.Map;

/**
 * Immutable object for representing a config name and qualifiers.
 * @param configName Same as the {@link ConfigDefinition#name()}.
 * @param qualifiers Same as the {@link ConfigDefinition#qualifiers()}.
 */
public record ConfigKey(String configName, Map<String, String> qualifiers) {

    public ConfigKey {
        qualifiers = (qualifiers != null) ? qualifiers : Map.of();
    }
}

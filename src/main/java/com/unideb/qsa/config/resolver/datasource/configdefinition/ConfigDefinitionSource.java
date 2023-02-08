package com.unideb.qsa.config.resolver.datasource.configdefinition;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.unideb.qsa.config.resolver.datasource.configpack.ConfigPackSource;
import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigKey;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.context.Qualifier;

/**
 * Resolves {@link ConfigDefinition}.
 */
public class ConfigDefinitionSource {

    private static final String DEFAULT_CONFIG_QUALIFIER = "name";
    private static final String EMPTY_QUALIFIER = "";

    private final ConfigPackSource compositeConfigPackSource;

    public ConfigDefinitionSource(ConfigPackSource compositeConfigPackSource) {
        this.compositeConfigPackSource = compositeConfigPackSource;
    }

    /**
     * Resolves {@link ConfigDefinition} based on the config name and qualifier.
     * @param configName config name
     * @param qualifier  condition
     * @return resolved {@link ConfigDefinition}s
     */
    public List<ConfigDefinition> getConfigDefinitions(String configName, Qualifier qualifier) {
        return compositeConfigPackSource
                .getConfigPacks()
                .stream()
                .map(configPack -> getConfigDefinitions(configPack, configName, qualifier))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<ConfigDefinition> getConfigDefinitions(ConfigPack configPack, String configName, Qualifier qualifier) {
        var updatedQualifier = createQualifierWithDefaultValue(qualifier);
        return Stream.of(resolveConfigDefinitions(configPack, configName, updatedQualifier),
                             resolveGlobalConfigDefinition(configPack, configName))
                     .flatMap(Optional::stream)
                     .collect(Collectors.toList());
    }

    private Qualifier createQualifierWithDefaultValue(Qualifier givenQualifier) {
        var result = givenQualifier;
        if (!givenQualifier.containsKey(DEFAULT_CONFIG_QUALIFIER)) {
            result = new Qualifier.Builder()
                    .putAll(givenQualifier.asMap())
                    .put(DEFAULT_CONFIG_QUALIFIER, EMPTY_QUALIFIER)
                    .build();
        }
        return result;
    }

    private Optional<ConfigDefinition> resolveConfigDefinitions(ConfigPack configPack, String configName, Qualifier qualifier) {
        return resolveConfigDefinition(configPack, configName, Map.of(DEFAULT_CONFIG_QUALIFIER, qualifier.get(DEFAULT_CONFIG_QUALIFIER)));
    }

    private Optional<ConfigDefinition> resolveGlobalConfigDefinition(ConfigPack configPack, String configName) {
        return resolveConfigDefinition(configPack, configName, Map.of());
    }

    private Optional<ConfigDefinition> resolveConfigDefinition(ConfigPack configPack, String configName, Map<String, String> qualifiers) {
        var configKey = new ConfigKey(configName, qualifiers);
        return configPack.getKeyToDefinitionsMap()
                         .entrySet()
                         .stream()
                         .filter(entry -> entry.getKey().equals(configKey))
                         .findFirst()
                         .map(Map.Entry::getValue);
    }
}

package com.unideb.qsa.config.resolver.datasource.configdefinition;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.unideb.qsa.config.resolver.datasource.configpack.ConfigPackSource;
import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigKey;
import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.context.Qualifier;

/**
 * Resolves {@link ConfigDefinition}.
 */
@Component
public class ConfigDefinitionSource {

    private static final String DEFAULT_CONFIG_QUALIFIER = "name";
    private static final String EMPTY_QUALIFIER = "";

    @Autowired
    private ConfigPackSource compositeConfigPackSource;

    /**
     * Resolves {@link ConfigDefinition} based on the config name and qualifier.
     *
     * @param configName config name
     * @param qualifier  condition
     * @return resolved {@link ConfigDefinition}s
     */
    public Collection<ConfigDefinition> getConfigDefinitions(String configName, Qualifier qualifier) {
        ImmutableList.Builder<ConfigDefinition> configDefinitions = new ImmutableList.Builder<>();
        compositeConfigPackSource
                .getConfigPacks()
                .stream()
                .map(configPack -> getConfigDefinitions(configPack, configName, qualifier))
                .forEach(configDefinitions::addAll);
        return configDefinitions.build();
    }

    private Collection<ConfigDefinition> getConfigDefinitions(ConfigPack configPack, String configName, Qualifier qualifier) {
        ImmutableList.Builder<ConfigDefinition> configDefinitionBuilder = new ImmutableList.Builder<>();
        Qualifier updatedQualifier = createQualifierWithDefaultValue(qualifier);
        addConfigDefinitionIfExists(configPack, configName, updatedQualifier, configDefinitionBuilder);
        addGlobalConfigDefinitionIfExists(configPack, configName, configDefinitionBuilder);
        return configDefinitionBuilder.build();
    }

    private Qualifier createQualifierWithDefaultValue(Qualifier givenQualifier) {
        Qualifier result = givenQualifier;
        if (!givenQualifier.containsKey(DEFAULT_CONFIG_QUALIFIER)) {
            result = new Qualifier.Builder()
                    .putAll(givenQualifier.asMap())
                    .put(DEFAULT_CONFIG_QUALIFIER, EMPTY_QUALIFIER)
                    .build();
        }
        return result;
    }

    private void addConfigDefinitionIfExists(ConfigPack configPack, String configName, Qualifier qualifier, ImmutableList.Builder<ConfigDefinition> builder) {
        addConfigDefinition(builder, configPack, configName, ImmutableMap.of(DEFAULT_CONFIG_QUALIFIER, qualifier.get(DEFAULT_CONFIG_QUALIFIER)));
    }

    private void addGlobalConfigDefinitionIfExists(ConfigPack configPack, String configName, ImmutableList.Builder<ConfigDefinition> builder) {
        addConfigDefinition(builder, configPack, configName, ImmutableMap.of());
    }

    private void addConfigDefinition(ImmutableList.Builder<ConfigDefinition> builder, ConfigPack configPack, String configName,
            ImmutableMap<String, String> qualifiers) {
        ConfigKey configKey = new ConfigKey(configName, qualifiers);
        configPack.getConfigKeyToConfigDefinitionMap()
                  .entrySet()
                  .stream()
                  .filter(entry -> entry.getKey().equals(configKey))
                  .findFirst()
                  .ifPresent(entry -> builder.add(entry.getValue()));
    }
}

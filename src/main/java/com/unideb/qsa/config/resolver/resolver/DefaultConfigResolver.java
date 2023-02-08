package com.unideb.qsa.config.resolver.resolver;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import com.unideb.qsa.config.resolver.datasource.configdefinition.ConfigDefinitionSource;
import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigValue;
import com.unideb.qsa.config.resolver.domain.context.Qualifier;

/**
 * The default implementation for {@link ConfigResolver}r.
 */
public class DefaultConfigResolver implements ConfigResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigResolver.class);

    private static final String RESOLVE_ATTEMPT_LOG_MSG = "Attempting to resolve config={} for context={} and configDefinitions={}";
    private static final String RESOLVE_SUCCESS_LOG_MSG = "Resolved config={} to configValue=\"{}\" for context={}";

    private final ConfigDefinitionSource configDefinitionSource;

    public DefaultConfigResolver(ConfigDefinitionSource configDefinitionSource) {
        this.configDefinitionSource = configDefinitionSource;
    }

    @Override
    public Optional<String> resolve(String configName, Qualifier qualifier) {
        var configDefinitions = configDefinitionSource.getConfigDefinitions(configName, qualifier);
        var resolvedConfigValue = resolve(configName, qualifier, configDefinitions);
        return resolvedConfigValue.map(ConfigValue::getValue);
    }

    @Override
    public <T> Optional<T> resolve(String configName, Qualifier qualifier, Class<T> classOfT) {
        return resolve(configName, qualifier).map(resolvedValue -> new Gson().fromJson(resolvedValue, classOfT));
    }

    private Optional<ConfigValue> resolve(String configName, Qualifier qualifier, List<ConfigDefinition> configDefinitions) {
        LOGGER.info(RESOLVE_ATTEMPT_LOG_MSG, configName, qualifier, configDefinitions);
        var resolvedConfigValue = resolveConfigValue(qualifier, configDefinitions);
        LOGGER.info(RESOLVE_SUCCESS_LOG_MSG, configName, resolvedConfigValue, qualifier);
        return resolvedConfigValue;
    }

    private Optional<ConfigValue> resolveConfigValue(Qualifier qualifier, Collection<ConfigDefinition> configDefinitions) {
        return configDefinitions
                .stream()
                .map(configDefinition -> getConfigDefinition(configDefinition, qualifier))
                .filter(Optional::isPresent)
                .findAny()
                .orElse(Optional.empty());
    }

    private Optional<ConfigValue> getConfigDefinition(ConfigDefinition configDefinition, Qualifier context) {
        return configDefinition
                .getConfigValues()
                .stream()
                .filter(configValue -> doesConfigValueMatchContext(configValue, context))
                .findFirst();
    }

    private boolean doesConfigValueMatchContext(ConfigValue configValue, Qualifier givenQualifier) {
        return configValue
                .getQualifiers()
                .entrySet()
                .stream()
                .noneMatch(qualifier ->
                        !givenQualifier.containsKey(qualifier.getKey())
                        || !qualifier.getValue().contains(givenQualifier.get(qualifier.getKey())));
    }
}

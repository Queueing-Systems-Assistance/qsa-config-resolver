package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import com.unideb.qsa.config.resolver.config.ResolverProperties;
import com.unideb.qsa.domain.context.ConfigPack;

/**
 * Composite {@link ConfigPack} resolver.
 */
@Component
public class CompositeConfigPackSource implements ConfigPackSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeConfigPackSource.class);

    private static final String NO_CONFIGS_FOUND = "No configs were found under {}";

    @Autowired
    private List<ConfigPackSource> configPackSources;
    @Autowired
    private ResolverProperties resolverProperties;

    @Cacheable("configs")
    @Override
    public List<ConfigPack> getConfigPacks() {
        ImmutableList.Builder<ConfigPack> configPackBuilder = new ImmutableList.Builder<>();
        configPackSources.stream()
                         .map(ConfigPackSource::getConfigPacks)
                         .forEach(configPackBuilder::addAll);
        ImmutableList<ConfigPack> configPacks = configPackBuilder.build();
        if (configPacks.isEmpty()) {
            LOGGER.warn(NO_CONFIGS_FOUND, resolverProperties.getUris());
        }
        return configPacks;
    }
}

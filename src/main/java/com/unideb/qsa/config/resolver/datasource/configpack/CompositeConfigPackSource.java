package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;
import java.util.stream.Collectors;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Composite {@link ConfigPack} resolver.
 */
public class CompositeConfigPackSource implements ConfigPackSource {

    private final List<ConfigPackSource> configPackSources;

    public CompositeConfigPackSource(List<ConfigPackSource> configPackSources) {
        this.configPackSources = configPackSources;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {
        return resolveConfigPacks();
    }

    private List<ConfigPack> resolveConfigPacks() {
        return configPackSources.stream()
                                .map(ConfigPackSource::getConfigPacks)
                                .flatMap(List::stream)
                                .collect(Collectors.toList());
    }
}

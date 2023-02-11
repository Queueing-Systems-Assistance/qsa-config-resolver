package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * {@link ConfigPack} resolver.
 */
public interface ConfigPackSource {

    /**
     * Resolves {@link ConfigPack}s.
     * @return resolved {@link ConfigPack} in a list
     */
    List<ConfigPack> getConfigPacks();

}

package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Caches config packs based on the refresh rate.
 */
public class CacheableConfigPackSource implements ConfigPackSource {

    private static final String REFRESHING_CONFIG_PACKS = "Refreshing config packs";
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheableConfigPackSource.class);
    private static final int SECONDS = 60;
    private static final int MILLI_SECONDS = 1000;

    private final ConfigPackSource configPackSource;
    private final int refreshRate;

    private long lastRefreshedTime;
    private List<ConfigPack> cachedConfigPacks;

    public CacheableConfigPackSource(ConfigPackSource configPackSource, int refreshRateInMinutes) {
        this.configPackSource = configPackSource;
        this.refreshRate = refreshRateInMinutes * SECONDS * MILLI_SECONDS;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {
        if (isCacheExpired()) {
            LOGGER.info(REFRESHING_CONFIG_PACKS);
            cachedConfigPacks = configPackSource.getConfigPacks();
            lastRefreshedTime = System.currentTimeMillis();
        }
        return cachedConfigPacks;
    }

    private boolean isCacheExpired() {
        return System.currentTimeMillis() - lastRefreshedTime > refreshRate;
    }
}

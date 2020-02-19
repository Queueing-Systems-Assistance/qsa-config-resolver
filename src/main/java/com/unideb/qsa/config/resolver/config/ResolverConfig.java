package com.unideb.qsa.config.resolver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for this library.
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(ResolverProperties.class)
public class ResolverConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolverConfig.class);
    private static final int REFRESH_RATE = 1000 * 60 * 60;

    @Autowired
    private ResolverProperties resolverProperties;

    /**
     * Creates a {@link RestTemplate} for resolving URL configs.
     *
     * @return default {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates a default cache manager for configs.
     *
     * @return {@link ConcurrentMapCacheManager}
     */
    @Bean(name = "configs")
    public CacheManager cacheManager() {
        LOGGER.info("Config resolver refresh rate [{}] and configs {}", REFRESH_RATE, resolverProperties.getUris());
        return new ConcurrentMapCacheManager();
    }

    /**
     * Evict configs from the cache. This is useful for refreshing the cache after every 1 hour.
     */
    @Scheduled(fixedRate = REFRESH_RATE)
    @CacheEvict(allEntries = true, value = "configs")
    public void reportCacheEvict() {
        LOGGER.info("Refreshing config packs");
    }
}

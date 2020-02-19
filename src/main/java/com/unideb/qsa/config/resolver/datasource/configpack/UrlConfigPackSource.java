package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.unideb.qsa.config.resolver.config.ResolverProperties;
import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.deserializer.JsonDeserializerHelper;

/**
 * Resolves {@link ConfigPack} from URL.
 */
@Component
public class UrlConfigPackSource implements ConfigPackSource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResolverProperties resolverProperties;

    @Override
    public List<ConfigPack> getConfigPacks() {
        return resolverProperties
                .getUris()
                .stream()
                .filter(this::isConfigLocationURL)
                .map(this::getConfigContentFromPath)
                .map(JsonDeserializerHelper::deserializeToConfigPack)
                .collect(Collectors.toList());
    }

    private String getConfigContentFromPath(String configPackLocation) {
        return restTemplate.getForObject(configPackLocation, String.class);
    }
}

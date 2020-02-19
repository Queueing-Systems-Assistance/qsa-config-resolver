package com.unideb.qsa.config.resolver.datasource.configpack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;

import com.unideb.qsa.config.resolver.config.ResolverProperties;
import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.deserializer.JsonDeserializerHelper;
import com.unideb.qsa.domain.exception.ConfigPackException;

/**
 * Resolves {@link ConfigPack} from file system.
 */
@Component
public class FileConfigPackSource implements ConfigPackSource {

    private static final String ERROR_CANNOT_READ_FILE = "Cannot read from [%s]";
    private static final String RESOURCE_PATTERN = "%s/build/config-pack.json";

    @Autowired
    private ResolverProperties resolverProperties;

    @Override
    public List<ConfigPack> getConfigPacks() {
        return resolverProperties
                .getUris()
                .stream()
                .map(configLocation -> String.format(RESOURCE_PATTERN, configLocation))
                .filter(configLocation -> !isConfigLocationURL(configLocation))
                .map(this::getConfigContentFromPath)
                .map(JsonDeserializerHelper::deserializeToConfigPack)
                .collect(Collectors.toList());
    }

    private String getConfigContentFromPath(String configPackLocation) {
        File configPackFile = getConfigPackFile(configPackLocation);
        return getFileContent(configPackFile);
    }

    private File getConfigPackFile(String fullPath) {
        return new File(Paths.get(fullPath).toUri());
    }

    private String getFileContent(File file) {
        try {
            return Files.readString(file.toPath(), Charsets.UTF_8);
        } catch (IOException e) {
            throw new ConfigPackException(String.format(ERROR_CANNOT_READ_FILE, file.toPath()), e);
        }
    }
}

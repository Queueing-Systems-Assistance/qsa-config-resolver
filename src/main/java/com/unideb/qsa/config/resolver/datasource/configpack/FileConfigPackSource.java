package com.unideb.qsa.config.resolver.datasource.configpack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.deserializer.JsonDeserializerHelper;
import com.unideb.qsa.domain.exception.ConfigPackException;

/**
 * Resolves {@link ConfigPack} from file system.
 */
public class FileConfigPackSource implements ConfigPackSource {

    private static final String ERROR_CANNOT_READ_FILE = "Cannot read from [%s]";
    private static final String RESOURCE_PATTERN = "%s/build/config-pack.json";

    private final List<String> configUris;

    public FileConfigPackSource(List<String> configUris) {
        this.configUris = configUris;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {
        return configUris.stream()
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
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ConfigPackException(String.format(ERROR_CANNOT_READ_FILE, file.toPath()), e);
        }
    }
}

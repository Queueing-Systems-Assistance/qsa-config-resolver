package com.unideb.qsa.config.resolver.datasource.configpack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.unideb.qsa.config.resolver.deserializer.JsonDeserializerHelper;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.exception.ConfigPackException;

/**
 * Resolves {@link ConfigPack} from file system.
 */
public class FileConfigPackSource implements ConfigPackSource {

    private final List<String> configUris;

    public FileConfigPackSource(List<String> configUris) {
        this.configUris = configUris;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {
        return configUris.stream()
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
            throw new ConfigPackException(String.format("Cannot read from [%s]", file.toPath()), e);
        }
    }
}

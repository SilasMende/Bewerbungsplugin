package de.zappler2k.api.config;

import de.zappler2k.api.config.impl.IConfig;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private List<IConfig> iConfigs;

    public ConfigManager() {
        this.iConfigs = new ArrayList<>();
    }

    public IConfig getIConfig(Class<? extends IConfig> iConfig) {
        return iConfigs.stream().filter(streamconfig -> streamconfig.getClass().getName().equals(iConfig.getName())).findAny().orElse(null);
    }

    public boolean existsIConfig(IConfig iConfig) {
        return getIConfig(iConfig.getClass()) != null;
    }

    @SneakyThrows
    public void addIConfig(IConfig iConfig) {
        if (iConfig.getFile().getParent() != null) {
            if (!iConfig.getFile().getParentFile().exists()) iConfig.getFile().getParentFile().mkdirs();
        }
        if (!iConfig.getFile().exists()) {
            iConfig.getFile().createNewFile();
            FileWriter fileWriter = new FileWriter(iConfig.getFile());
            fileWriter.write(iConfig.getDefaultConfig());
            fileWriter.flush();
            fileWriter.close();
        }
        this.iConfigs.add(iConfig.fromJson(getContent(iConfig.getFile())));
    }

    public void removeIConfig(IConfig iConfig) {
        if (this.iConfigs.contains(iConfig)) {
            this.iConfigs.remove(iConfig);
        }
    }

    @SneakyThrows
    public void insert(IConfig iConfig, String data) {
        FileWriter fileWriter = new FileWriter(iConfig.getFile());
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }

    @SneakyThrows
    public String getContent(IConfig iConfig) {
        return new String(Files.readAllBytes(iConfig.getFile().toPath()));
    }

    @SneakyThrows
    public String getContent(File file) {
        return new String(Files.readAllBytes(file.toPath()));
    }
}

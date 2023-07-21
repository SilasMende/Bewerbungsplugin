package de.zappler2k.api.config.impl;

import java.io.File;

public interface IConfig {

    File getFile();

    String getDefaultConfig();

    IConfig fromJson(String data);

    String toJson();
}

package de.zappler2k.api.mysql.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.zappler2k.api.config.impl.IConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MySQLConfig implements IConfig {

    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;

    @Override
    public File getFile() {
        return new File("plugins//Bewerbungsplugin//mysql.json");
    }

    @Override
    public String getDefaultConfig() {
        return new MySQLConfig("localhost", 3306, "bewerbung", "root", "1234").toJson();
    }

    @Override
    public IConfig fromJson(String data) {
        return new Gson().fromJson(data, this.getClass());
    }

    @Override
    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

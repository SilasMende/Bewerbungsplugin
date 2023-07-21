package de.zappler2k.config.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.zappler2k.api.config.impl.IConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageConfig implements IConfig {

    private String adress;
    private List<String> badWords;

    @Override
    public File getFile() {
        return new File("plugins//Bewerbungsplugin//badwords.json");
    }

    @Override
    public String getDefaultConfig() {
        List list = new ArrayList<>();
        list.add("arschloch");
        list.add("huso");
        return new MessageConfig("0.0.0.0",list).toJson();
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

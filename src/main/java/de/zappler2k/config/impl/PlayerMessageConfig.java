package de.zappler2k.config.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerMessageConfig {

    private String name;
    private Long timeStamp;
    private List<String> messages;

    public PlayerMessageConfig(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
        this.timeStamp = 0L;
    }
}

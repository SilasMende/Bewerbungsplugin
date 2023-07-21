package de.zappler2k.config.impl;

import de.zappler2k.Bewerbung;
import de.zappler2k.api.http.HttpAPI;
import de.zappler2k.api.inventory.impl.Gui;
import de.zappler2k.api.item.ItemBuilder;
import de.zappler2k.config.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class PlayerGui extends Gui {

    private MessageManager messageManager;

    public PlayerGui(MessageManager messageManager, HttpAPI httpAPI) {
        super(9, "Loading....");
        this.messageManager = messageManager;
        int size = messageManager.getPlayerMessageConfigs().size();
        createInventory(scaleInventorySize(size), "Chat-Verletzungen");
        int i = 0;
        for (PlayerMessageConfig playerMessageConfig : messageManager.getPlayerMessageConfigs()) {
            Date date = new Date(playerMessageConfig.getTimeStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:yyyy");
            setItemAndConsumer(i, new ItemBuilder(Material.PLAYER_HEAD)
                    .setName(ChatColor.GRAY + " " + playerMessageConfig.getName() + ChatColor.YELLOW + " " + simpleDateFormat.format(date) + ChatColor.GRAY +" (Click Right-Click to delete)")
                    .setPlayerHead(playerMessageConfig.getName())
                    .build(), event -> {
                    if(event.getClick().equals(ClickType.LEFT)) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Chat-Verletzungen am : " + simpleDateFormat.format(date) + "\n");
                        for (String message : playerMessageConfig.getMessages()) {
                            builder.append(message + "\n");
                        }
                        try {
                            httpAPI.createContent(playerMessageConfig.getName() + "/" + playerMessageConfig.getTimeStamp(), builder.toString(), (Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                        } catch (IllegalArgumentException e) {
                            httpAPI.removeContent(playerMessageConfig.getName() + "/" + playerMessageConfig.getTimeStamp());
                            httpAPI.createContent(playerMessageConfig.getName() + "/" + playerMessageConfig.getTimeStamp(), builder.toString(), (Player) event.getWhoClicked());
                        }
                    } else if(event.getClick().equals(ClickType.RIGHT)) {
                        messageManager.deleteBadWord(playerMessageConfig.getTimeStamp());
                        event.getWhoClicked().sendMessage("^Die Chat-Verletzung von " + playerMessageConfig.getName() + " vom " + date + "wurde entfernt.");
                        messageManager.getPlayerMessageConfigs().remove(messageManager.getConfigByName(playerMessageConfig.getName()));
                        event.getWhoClicked().closeInventory();
                    }
            });
            i++;
        }
        getInventory().setContents(getInventory().getContents());
    }

    public int scaleInventorySize(int value) {
        if (value <= 9) {
            return 9;
        } else if (value <= 18) {
            return 18;
        } else if (value <= 36) {
            return 36;
        } else if (value <= 45) {
            return 45;
        } else {
            return 54;
        }
    }
}

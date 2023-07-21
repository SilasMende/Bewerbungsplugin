package de.zappler2k.config;

import com.google.gson.Gson;
import de.zappler2k.api.config.ConfigManager;
import de.zappler2k.api.mysql.MySQLConnector;
import de.zappler2k.config.impl.MessageConfig;
import de.zappler2k.config.impl.PlayerMessageConfig;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.Inet4Address;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Getter
public class MessageManager implements Listener {

    private MySQLConnector mySQLConnector;
    private List<PlayerMessageConfig> playerMessageConfigs;

    private MessageConfig messageConfig;

    public MessageManager(MySQLConnector mySQLConnector, JavaPlugin plugin, ConfigManager configManager) {
        this.mySQLConnector = mySQLConnector;
        mySQLConnector.update("CREATE TABLE IF NOT EXISTS messagefilter (timestamp BIGINT, messages TEXT);");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        playerMessageConfigs = new ArrayList<>();
        configManager.addIConfig(new MessageConfig());
        messageConfig = (MessageConfig) configManager.getIConfig(MessageConfig.class);
    }

    public PlayerMessageConfig getConfigByName(String name) {
        return playerMessageConfigs.stream().filter(playerMessageConfig -> playerMessageConfig.getName().equals(name)).findAny().orElse(null);
    }

    public List<PlayerMessageConfig> getPlayerMessageConfigs() {
        List<PlayerMessageConfig> configs = new ArrayList<>();
        ResultSet resultSet = mySQLConnector.query("SELECT timestamp, messages FROM messagefilter");
        try {
            while (resultSet.next()) {
                Long uuid = resultSet.getLong("timestamp");
                String messages = resultSet.getString("messages");
                PlayerMessageConfig playerMessageConfig = new Gson().fromJson(messages, PlayerMessageConfig.class);
                configs.add(playerMessageConfig);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configs;
    }

    public void deleteBadWord(Long timeStamp) {
        mySQLConnector.update("DELETE FROM messagefilter WHERE timestamp = ?", timeStamp);
    }

    @EventHandler
    public void onChatAsync(AsyncPlayerChatEvent event) {
        if (getConfigByName(event.getPlayer().getName()) == null) {
            playerMessageConfigs.add(new PlayerMessageConfig(event.getPlayer().getName()));
        }
        PlayerMessageConfig playerMessageConfig = getConfigByName(event.getPlayer().getName());
        playerMessageConfig.getMessages().add(event.getPlayer().getName() + ": " + event.getMessage());
        List<String> badWords = messageConfig.getBadWords();
        String message = event.getMessage();
        boolean containsBadWord = badWords.stream().anyMatch(message::contains);
        if (containsBadWord) {
            playerMessageConfig.setTimeStamp(System.currentTimeMillis());
            int messagesToKeep = 10;
            int totalMessages = playerMessageConfig.getMessages().size();
            if (totalMessages > messagesToKeep) {
                List<String> messagesToKeepList = new ArrayList<>(playerMessageConfig.getMessages().subList(totalMessages - messagesToKeep, totalMessages));
                playerMessageConfig.setMessages(messagesToKeepList);
            }
            mySQLConnector.update("INSERT INTO messagefilter (timestamp, messages) VALUES (?,?)", System.currentTimeMillis(), new Gson().toJson(playerMessageConfig));
        }
    }
}

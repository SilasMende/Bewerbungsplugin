package de.zappler2k;

import de.zappler2k.api.config.ConfigManager;
import de.zappler2k.api.http.HttpAPI;
import de.zappler2k.api.inventory.GuiManager;
import de.zappler2k.api.mysql.MySQLConnector;
import de.zappler2k.config.MessageManager;
import de.zappler2k.config.command.BadWords;
import de.zappler2k.config.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class Bewerbung extends JavaPlugin {

    private HttpAPI httpAPI;

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager();
        MySQLConnector mySQLConnector = new MySQLConnector(configManager);
        mySQLConnector.connect();
        if (!mySQLConnector.isConnected()) {
            return;
        }
        MessageManager messageManager = new MessageManager(mySQLConnector, this, configManager);
        GuiManager guiManager = new GuiManager();
        httpAPI = new HttpAPI(messageManager);
        getCommand("badwords").setExecutor(new BadWords(guiManager, messageManager, httpAPI));
        getServer().getPluginManager().registerEvents(new JoinListener(messageManager), this);
    }

    @Override
    public void onDisable() {
        httpAPI.getServer().stop(0);
    }


}

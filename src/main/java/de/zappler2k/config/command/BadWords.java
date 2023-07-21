package de.zappler2k.config.command;

import de.zappler2k.api.http.HttpAPI;
import de.zappler2k.api.inventory.GuiManager;
import de.zappler2k.config.MessageManager;
import de.zappler2k.config.impl.PlayerGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BadWords implements CommandExecutor {

    private GuiManager guiManager;
    private MessageManager messageManager;
    private HttpAPI httpAPI;

    public BadWords(GuiManager guiManager, MessageManager messageManager, HttpAPI httpAPI) {
        this.guiManager = guiManager;
        this.messageManager = messageManager;
        this.httpAPI = httpAPI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (sender.hasPermission("badwords.*")) {
            PlayerGui playerGui = new PlayerGui(messageManager, httpAPI);
            guiManager.addGui(playerGui);
            Player player = (Player) sender;
            player.openInventory(playerGui.getInventory());
        }
        return false;
    }
}

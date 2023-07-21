package de.zappler2k.config.listeners;

import de.zappler2k.config.MessageManager;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
@AllArgsConstructor
public class JoinListener implements Listener {

    private MessageManager messageManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPermission("badwords.*")) {
            return;
        }
        int size = messageManager.getPlayerMessageConfigs().size();
        switch (size) {
            case 0 -> {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Es sind gerade keine Chat-Verletzungen offen.");
            }
            case 1 -> {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Es ist gerade " + size + " Chat-Verletzung offen.");
            }
            default -> {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Es sind gerade " + size + " Chat-Verletzungen offen.");
            }
        }
    }
}

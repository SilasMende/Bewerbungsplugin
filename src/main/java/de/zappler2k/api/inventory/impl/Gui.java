package de.zappler2k.api.inventory.impl;

import de.zappler2k.Bewerbung;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class Gui implements Listener {

    private String playerName;
    private Inventory inventory;
    private HashMap<Integer, Consumer<InventoryClickEvent>> invConsumer;
    private boolean isClickable;

    public Gui(int invSize, String title) {
        inventory = Bukkit.createInventory(null, invSize, title);
        Bukkit.getServer().getPluginManager().registerEvents(this, Bewerbung.getPlugin(Bewerbung.class));
        invConsumer = new HashMap<>();
        isClickable = false;
    }

    public void createInventory(int invSize, String title) {
        inventory = Bukkit.createInventory(null, invSize, title);
    }

    public void fillInventory(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
    }

    public void setItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public void setItemAndConsumer(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> invConsumer) {
        setItem(slot, itemStack);
        this.invConsumer.put(slot, invConsumer);
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) {
            return;
        }

        if (!isClickable) {
            event.setCancelled(true);
        }
        if (invConsumer.containsKey(event.getSlot())) {
            invConsumer.get(event.getSlot()).accept(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) {
            HandlerList.unregisterAll(this);
        }
    }
}

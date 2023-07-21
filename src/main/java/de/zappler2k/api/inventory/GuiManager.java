package de.zappler2k.api.inventory;

import de.zappler2k.api.inventory.impl.Gui;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {

    private List<Gui> guis;

    public GuiManager() {
        this.guis = new ArrayList<>();
    }

    public Gui getGuiByName(String playerName) {
        return guis.stream().filter(gui -> gui.getPlayerName().equals(playerName)).findAny().orElse(null);
    }

    public void addGui(Gui gui) {
        if (getGuiByName(gui.getPlayerName()) != null) {
            guis.add(gui);
        }
    }

    public void removeGui(Gui gui) {
        if (getGuiByName(gui.getPlayerName()) == null) {
            guis.remove(gui);
            HandlerList.unregisterAll(gui);
        }
    }
}

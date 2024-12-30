package io.papermc.terraformer;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BrushSettings implements InventoryHolder {
    private final Inventory inventory;

    public BrushSettings(Terraformer plugin, BrushType brushType, int brushSize) {
        this.inventory = plugin.getServer().createInventory(null, 27, Component.text("Brush Settings").color(NamedTextColor.AQUA)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                .append(brushType.getName())
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                .append(Component.text(brushSize).color(NamedTextColor.GOLD)))));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

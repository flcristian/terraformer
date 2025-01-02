package io.papermc.terraformer.terraformer_properties.properties.brush_settings;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.constants.BrushSettingsItems;
import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.properties.BrushType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BrushSettings implements InventoryHolder {
    private final Inventory inventory;

    public BrushSettings(Terraformer plugin, BrushType brushType, int brushSize) {
        this.inventory = plugin.getServer().createInventory(this, 54,
                Component.text("Brush Settings").color(NamedTextColor.AQUA)
                        .append(Component.text(" - ").color(NamedTextColor.GRAY)
                                .append(brushType.getName())
                                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                                        .append(Component.text(brushSize).color(NamedTextColor.GOLD)))));

        for (int size = 1; size <= 9; size++) {
            ItemStack brushSizeItem = new ItemStack(Material.HEART_OF_THE_SEA);
            ItemMeta brushSizeItemMeta = brushSizeItem.getItemMeta();
            brushSizeItemMeta.customName(BrushSettingsItems.SETTINGS_BRUSH_SIZE(size));
            brushSizeItemMeta.lore(List.of(
                    Component.text("Set the brush size to " + size + "").color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Works for all brushes").color(NamedTextColor.DARK_GRAY)
                            .decorate(TextDecoration.ITALIC)));
            brushSizeItem.add(size - 1);
            brushSizeItem.setItemMeta(brushSizeItemMeta);
            inventory.setItem(size - 1, brushSizeItem);
        }
    }

    public void onInventoryClick(Terraformer plugin, InventoryClickEvent event, Player player,
            TerraformerProperties properties) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        for (int size = 1; size <= 9; size++) {
            if (meta.customName().equals(BrushSettingsItems.SETTINGS_BRUSH_SIZE(size))) {
                properties.BrushSize = size;
                player.sendMessage(Messages.CHANGED_BRUSH_SIZE(size));
                properties.Brush.openBrushSettings(plugin, player, properties.BrushSize);
                return;
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

package ro.flcristian.terraformer.terraformer_properties.properties.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;

public class SelectPaintMode implements InventoryHolder {
    private final Inventory inventory;

    public SelectPaintMode(Terraformer plugin, TerraformerProperties properties) {
        Component inventoryName = Component.text("Select Paint Brush Mode").color(NamedTextColor.BLUE);

        inventory = plugin.getServer().createInventory(this, 9,
                inventoryName);

        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.customName(inventoryName);
        empty.setItemMeta(emptyMeta);

        for (int i = 0; i < 9; i += 2) {
            inventory.setItem(i, empty);
        }

        List<ItemStack> paintModes = new ArrayList<>(List.of(
                BrushType.getBrushSettingsItem(BrushType.PAINT_TOP),
                BrushType.getBrushSettingsItem(BrushType.PAINT_WALL),
                BrushType.getBrushSettingsItem(BrushType.PAINT_BOTTOM),
                BrushType.getBrushSettingsItem(BrushType.PAINT_SURFACE)));

        for (int i = 0, j = 1; i < paintModes.size(); i++, j += 2) {
            inventory.setItem(j, paintModes.get(i));
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

        // Brush Selection

        BrushType[] brushTypes = new BrushType[] { BrushType.PAINT_TOP, BrushType.PAINT_WALL,
                BrushType.PAINT_BOTTOM, BrushType.PAINT_SURFACE };

        for (BrushType brush : brushTypes) {
            if (meta.customName().equals(brush.getName())) {
                properties.Brush.Type = brush;
                player.sendMessage(Messages.CHANGED_BRUSH(brush));
                properties.Brush.Type.openBrushSettings(plugin, player, properties);
                return;
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
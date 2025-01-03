package io.papermc.terraformer.terraformer_properties.properties.brush_settings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BrushSettings implements InventoryHolder {
    private final boolean usesMaterials;
    private final boolean usesDepth;
    private final Inventory inventory;

    public BrushSettings(Terraformer plugin, TerraformerProperties properties, boolean usesMaterials,
            boolean usesDepth) {
        this.usesMaterials = usesMaterials;
        this.usesDepth = usesDepth;

        Component inventoryName = Component.text("Brush Settings").color(NamedTextColor.AQUA)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                        .append(properties.Brush.Type.getName()));

        this.inventory = plugin.getServer().createInventory(this, 54,
                inventoryName);

        // Size Selection

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
            inventory.setItem(45 + size - 1, brushSizeItem);
        }

        // Depth Selection

        if (usesDepth) {
            for (int depth = 1; depth <= 9; depth++) {
                ItemStack brushDepthItem = new ItemStack(Material.HEART_OF_THE_SEA);
                ItemMeta brushDepthItemMeta = brushDepthItem.getItemMeta();
                brushDepthItemMeta.customName(BrushSettingsItems.SETTINGS_BRUSH_DEPTH(depth));
                brushDepthItemMeta.lore(List.of(
                        Component.text("Set the brush depth to " + depth +
                                "").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Works for: Paint, Rise, Dig").color(NamedTextColor.DARK_GRAY)
                                .decorate(TextDecoration.ITALIC)));
                brushDepthItem.add(depth - 1);
                brushDepthItem.setItemMeta(brushDepthItemMeta);
                inventory.setItem(36 + depth - 1, brushDepthItem);
            }
        }

        // Brush Selection

        List<ItemStack> brushes = Arrays.stream(BrushType.values())
                .map(BrushType::getBrushSettingsItem)
                .collect(Collectors.toList());

        for (int i = 0; i < brushes.size(); i++) {
            inventory.setItem((usesDepth ? 27 : 36) + i, brushes.get(i));
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

        // Size Selection

        for (int size = 1; size <= 9; size++) {
            if (meta.customName().equals(BrushSettingsItems.SETTINGS_BRUSH_SIZE(size))) {
                properties.Brush.BrushSize = size;
                player.sendMessage(Messages.CHANGED_BRUSH_SIZE(size));
                properties.Brush.Type.openBrushSettings(plugin, player, properties);
                return;
            }
        }

        if (usesDepth) {
            for (int depth = 1; depth <= 9; depth++) {
                if (meta.customName().equals(BrushSettingsItems.SETTINGS_BRUSH_DEPTH(depth))) {
                    properties.Brush.BrushDepth = depth;
                    player.sendMessage(Messages.CHANGED_BRUSH_DEPTH(depth));
                    properties.Brush.Type.openBrushSettings(plugin, player, properties);
                    return;
                }
            }
        }

        // Brush Selection

        BrushType[] brushTypes = BrushType.values();

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

package ro.flcristian.terraformer.terraformer_properties.properties.menus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.constants.BrushSettingsItems;
import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;

public class BrushSettings implements InventoryHolder {
    private static final Component paintName = Component.text("Paint").color(NamedTextColor.BLUE);
    private static final Component depthSlotBlocked = Component.text("This brush doesn't use depth")
            .color(NamedTextColor.GRAY);

    private final Inventory inventory;
    private final boolean usesDepth;

    public BrushSettings(Terraformer plugin, TerraformerProperties properties, boolean usesDepth) {
        this.usesDepth = usesDepth;

        Component inventoryName = Component.text("Brush Settings").color(NamedTextColor.DARK_AQUA)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                        .append(properties.Brush.Type.getName()));

        inventory = plugin.getServer().createInventory(this, 27,
                inventoryName);

        setUpMenu(properties);
    }

    private void setUpMenu(TerraformerProperties properties) {
        // Brush Selection

        List<ItemStack> brushes = Stream.of(BrushType.values())
                .filter(brushType -> brushType != BrushType.PAINT_TOP && brushType != BrushType.PAINT_WALL
                        && brushType != BrushType.PAINT_BOTTOM && brushType != BrushType.PAINT_SURFACE)
                .map(BrushType::getBrushSettingsItem)
                .collect(Collectors.toList());

        ItemStack paint = new ItemStack(Material.BLUE_DYE);
        ItemMeta paintMeta = paint.getItemMeta();
        paintMeta.customName(paintName);
        paintMeta.lore(List.of(
                Component.text("Set the brush type to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(paintName.color(NamedTextColor.LIGHT_PURPLE)),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));
        paint.setItemMeta(paintMeta);
        brushes.add(paint);

        for (int i = 0; i < brushes.size(); i++) {
            inventory.setItem(0 + i, brushes.get(i));
        }

        // Size Selection

        for (int size = 1; size <= 9; size++) {
            ItemStack brushSizeItem = new ItemStack(Material.HEART_OF_THE_SEA, size);
            ItemMeta brushSizeItemMeta = brushSizeItem.getItemMeta();
            brushSizeItemMeta.customName(BrushSettingsItems.SETTINGS_BRUSH_SIZE(size));
            brushSizeItemMeta.lore(List.of(
                    Component.text("Set the brush size to " + size + "").color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Works for all brushes").color(NamedTextColor.DARK_GRAY)
                            .decorate(TextDecoration.ITALIC)));
            brushSizeItem.setItemMeta(brushSizeItemMeta);
            inventory.setItem(9 + size - 1, brushSizeItem);
        }

        // Depth Selection

        if (usesDepth) {
            for (int depth = 1; depth <= 9; depth++) {
                ItemStack brushDepthItem = new ItemStack(Material.NAUTILUS_SHELL, depth);
                ItemMeta brushDepthItemMeta = brushDepthItem.getItemMeta();
                brushDepthItemMeta.customName(BrushSettingsItems.SETTINGS_BRUSH_DEPTH(depth));
                brushDepthItemMeta.lore(List.of(
                        Component.text("Set the brush depth to " + depth +
                                "").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Works for: Paint, Rise, Dig").color(NamedTextColor.DARK_GRAY)
                                .decorate(TextDecoration.ITALIC)));
                brushDepthItem.setItemMeta(brushDepthItemMeta);
                inventory.setItem(18 + depth - 1, brushDepthItem);
            }
        } else {
            ItemStack depthSlot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta depthSlotMeta = depthSlot.getItemMeta();
            depthSlotMeta.customName(depthSlotBlocked);
            depthSlot.setItemMeta(depthSlotMeta);

            for (int i = 18; i < 27; i++) {
                inventory.setItem(i, depthSlot);
            }
        }
    }

    public void onInventoryClick(Terraformer plugin, InventoryClickEvent event, Player player,
            TerraformerProperties properties) {
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        if (event.getClickedInventory() == inventory)
            event.setCancelled(true);
        else
            return;

        if (meta.customName() != null) {
            if (meta.customName().equals(depthSlotBlocked)) {
                return;
            }

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

            BrushType[] brushTypes = Stream.of(BrushType.values())
                    .filter(brushType -> brushType != BrushType.PAINT_TOP && brushType != BrushType.PAINT_WALL
                            && brushType != BrushType.PAINT_BOTTOM && brushType != BrushType.PAINT_SURFACE)
                    .toArray(BrushType[]::new);

            for (BrushType brush : brushTypes) {
                if (meta.customName().equals(brush.getName())) {
                    properties.Brush.Type = brush;
                    player.sendMessage(Messages.CHANGED_BRUSH(brush));
                    properties.Brush.Type.openBrushSettings(plugin, player, properties);
                    return;
                }
            }

            if (meta.customName().equals(paintName)) {
                SelectPaintMode paintMode = new SelectPaintMode(plugin, properties);
                player.openInventory(paintMode.getInventory());
                return;
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

package ro.flcristian.terraformer.terraformer_properties.properties.menus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.constants.BrushSettingsItems;
import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

public class BrushSettings implements InventoryHolder {
    private static final Component paintName = Component.text("Paint").color(NamedTextColor.BLUE);
    private static final Component depthSlotBlocked = Component.text("This brush doesn't use depth")
            .color(NamedTextColor.GRAY);
    private static final Component brushHistoryEmpty = Component
            .text("Your brush properties will appear here.")
            .color(NamedTextColor.DARK_AQUA);
    private static final Component brushHistoryEntry = Component.text("Brush History").color(NamedTextColor.DARK_AQUA);
    private final Inventory inventory;
    private final boolean usesDepth;

    public BrushSettings(Terraformer plugin, TerraformerProperties properties, boolean usesDepth) {
        this.usesDepth = usesDepth;

        Component inventoryName = Component.text("Brush Settings").color(NamedTextColor.DARK_AQUA)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                        .append(properties.Brush.Type.getName()));

        inventory = plugin.getServer().createInventory(this, 36,
                inventoryName);

        setUpMenu(properties);
    }

    private void setUpMenu(TerraformerProperties properties) {
        // Brush Selection

        List<ItemStack> brushes = Stream.of(BrushType.values())
                .filter(brushType -> brushType != BrushType.PAINT_TOP && brushType != BrushType.PAINT_WALL
                        && brushType != BrushType.PAINT_BOTTOM && brushType != BrushType.PAINT_SURFACE)
                .map(brushType -> {
                    ItemStack item = BrushType.getBrushSettingsItem(brushType);
                    if (brushType == properties.Brush.Type) {
                        item.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
                        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    return item;
                })
                .collect(Collectors.toList());

        ItemStack paint = new ItemStack(Material.BLUE_DYE);
        ItemMeta paintMeta = paint.getItemMeta();
        paintMeta.customName(paintName);
        paintMeta.lore(List.of(
                Component.text("Set the brush type to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(paintName.color(NamedTextColor.LIGHT_PURPLE)),
                Component.text("Materials ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(Component.text("affect this brush").color(NamedTextColor.GREEN)),
                Component.text("Mask ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(Component.text("affects this brush").color(NamedTextColor.GREEN)),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));
        paint.setItemMeta(paintMeta);
        brushes.add(paint);

        for (int i = 0; i < brushes.size(); i++) {
            inventory.setItem(0 + i, brushes.get(i));
        }

        // Size Selection

        for (int size = 1; size <= 9; size++) {
            ItemStack brushSizeItem = new ItemStack(Material.HEART_OF_THE_SEA, size);
            if (size == properties.Brush.BrushSize) {
                brushSizeItem.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
                brushSizeItem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
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
                if (depth == properties.Brush.BrushDepth) {
                    brushDepthItem.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
                    brushDepthItem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
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

        // Brush History

        ItemStack brushHistorySlot = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta brushHistorySlotMeta = brushHistorySlot.getItemMeta();
        brushHistorySlotMeta.customName(brushHistoryEmpty);
        brushHistorySlot.setItemMeta(brushHistorySlotMeta);
        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, brushHistorySlot);
        }

        for (int i = 0; i < properties.BrushHistory.size(); i++) {
            BrushProperties brushProperties = properties.BrushHistory.get(properties.BrushHistory.size() - 1 - i);
            ItemStack brushHistory = new ItemStack(BrushType.getBrushSettingsItem(brushProperties.Type));
            ItemMeta brushHistoryMeta = brushHistory.getItemMeta();
            brushHistoryMeta.lore(List.of(
                    Component.text("Type: ").append(brushProperties.Type.getName())
                            .color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Size: ")
                            .append(Component.text(brushProperties.BrushSize).color(NamedTextColor.GOLD))
                            .color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Depth: ")
                            .append(Component.text(brushProperties.BrushDepth).color(NamedTextColor.GOLD))
                            .color(NamedTextColor.LIGHT_PURPLE),
                    Component.text("Click to apply").color(NamedTextColor.LIGHT_PURPLE)));
            brushHistoryMeta.customName(brushHistoryEntry);
            brushHistory.setItemMeta(brushHistoryMeta);
            inventory.setItem(i + 27, brushHistory);
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
                    BrushType oldBrushType = properties.Brush.Type;
                    properties.Brush.Type = brush;
                    if (oldBrushType != BrushType.FOLIAGE && brush == BrushType.FOLIAGE) {
                        properties.Brush.setMode(MaterialMode.RANDOM);
                    } else if (oldBrushType == BrushType.FOLIAGE && brush != BrushType.FOLIAGE) {
                        properties.Brush.setMode(properties.Brush.Mode);
                    }
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

            // Brush History

            if (meta.customName().equals(brushHistoryEntry)) {
                int index = event.getSlot() - 27;
                if (index < properties.BrushHistory.size()) {
                    properties
                            .applyBrushHistory(properties.BrushHistory.get(properties.BrushHistory.size() - 1 - index));
                    player.sendMessage(Messages.APPLIED_BRUSH_HISTORY);
                    properties.Brush.Type.openBrushSettings(plugin, player, properties);
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

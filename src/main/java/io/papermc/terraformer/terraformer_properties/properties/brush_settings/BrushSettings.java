package io.papermc.terraformer.terraformer_properties.properties.brush_settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
import io.papermc.terraformer.terraformer_properties.properties.modes.MaterialMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BrushSettings implements InventoryHolder {
    private static final Component paintName = Component.text("Paint").color(NamedTextColor.BLUE);
    private static final Component materialSlotBlocked = Component.text("This brush doesn't use materials")
            .color(NamedTextColor.RED);
    private static final Component depthSlotBlocked = Component.text("This brush doesn't use depth")
            .color(NamedTextColor.DARK_BLUE);
    private static final Component materialSlotEmptyPercentage = Component.text("Place a material in the empty slot")
            .color(NamedTextColor.RED);
    private static final Component materialSlotEmpty = Component.text("Empty slot").color(NamedTextColor.GRAY);

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

        inventory = plugin.getServer().createInventory(this, 54,
                inventoryName);

        setUpMenu(properties);
    }

    private void setUpMenu(TerraformerProperties properties) {
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
            inventory.setItem(45 + size - 1, brushSizeItem);
        }

        // Depth Selection

        if (usesDepth) {
            for (int depth = 1; depth <= 9; depth++) {
                ItemStack brushDepthItem = new ItemStack(Material.HEART_OF_THE_SEA, depth);
                ItemMeta brushDepthItemMeta = brushDepthItem.getItemMeta();
                brushDepthItemMeta.customName(BrushSettingsItems.SETTINGS_BRUSH_DEPTH(depth));
                brushDepthItemMeta.lore(List.of(
                        Component.text("Set the brush depth to " + depth +
                                "").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE),
                        Component.text("Works for: Paint, Rise, Dig").color(NamedTextColor.DARK_GRAY)
                                .decorate(TextDecoration.ITALIC)));
                brushDepthItem.setItemMeta(brushDepthItemMeta);
                inventory.setItem(36 + depth - 1, brushDepthItem);
            }
        } else {
            ItemStack depthSlot = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            ItemMeta depthSlotMeta = depthSlot.getItemMeta();
            depthSlotMeta.customName(depthSlotBlocked);
            depthSlot.setItemMeta(depthSlotMeta);

            for (int i = 36; i < 45; i++) {
                inventory.setItem(i, depthSlot);
            }
        }

        // Brush Selection

        List<ItemStack> brushes = Arrays
                .stream(new BrushType[] { BrushType.BALL, BrushType.SMOOTH, BrushType.ERODE, BrushType.EXTRUDE,
                        BrushType.RISE, BrushType.DIG })
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
            inventory.setItem(27 + i, brushes.get(i));
        }

        // Material Mode Selection

        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.customName(Component.text("Terraformer").color(NamedTextColor.GRAY));
        empty.setItemMeta(emptyMeta);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(18 + i, empty);
        }

        if (usesMaterials) {
            inventory.setItem(22, MaterialMode.getBrushSettingsItem(properties.Brush.Mode));
        }

        // Material Selection

        ItemStack materialSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta materialSlotMeta = materialSlot.getItemMeta();
        materialSlotMeta.customName(materialSlotBlocked);
        materialSlot.setItemMeta(materialSlotMeta);

        if (usesMaterials) {
            materialSlotMeta.customName(materialSlotEmptyPercentage);
            materialSlot.setItemMeta(materialSlotMeta);

            ItemStack[] materials = properties.Brush.Materials.keySet().stream()
                    .map(material -> new ItemStack(material))
                    .toArray(ItemStack[]::new);

            ItemStack materialPercentage = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            ItemMeta materialPercentageMeta = materialPercentage.getItemMeta();

            int lastMaterial = 0;
            for (int i = 0; i < materials.length; i++) {
                lastMaterial = i + 1;

                ItemMeta materialMeta = materials[i].getItemMeta();
                String materialName = materials[i].getType().toString();
                String transformed = Arrays.stream(materialName.split("_"))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "));
                materialMeta.customName(Component.text(transformed).color(NamedTextColor.DARK_GREEN));
                materials[i].setItemMeta(materialMeta);
                inventory.setItem(9 + i, materials[i]);

                materialPercentageMeta.customName(
                        Component.text(properties.Brush.Materials.get(materials[i].getType()) + "%")
                                .color(NamedTextColor.YELLOW));
                materialPercentage.setItemMeta(materialPercentageMeta);
                inventory.setItem(i, materialPercentage);
            }

            ItemStack emptySlot = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            ItemMeta emptySlotMeta = emptySlot.getItemMeta();
            emptySlotMeta.customName(materialSlotEmpty);
            emptySlot.setItemMeta(emptySlotMeta);

            for (int i = lastMaterial; i < 9; i++) {
                inventory.setItem(i, materialSlot);
                inventory.setItem(9 + i, emptySlot);
            }
        } else {
            for (int i = 0; i < 18; i++) {
                inventory.setItem(i, materialSlot);
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
            if (meta.customName().equals(materialSlotBlocked) || meta.customName().equals(materialSlotEmptyPercentage)
                    || meta.customName().equals(depthSlotBlocked)) {
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

            BrushType[] brushTypes = BrushType.values();

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

            // Material Mode Selection

            MaterialMode[] materialModes = MaterialMode.values();

            for (int i = 0; i < materialModes.length; i++) {
                if (meta.customName().equals(materialModes[i].getName())) {
                    properties.Brush.Mode = materialModes[(i + 1) % materialModes.length];
                    player.sendMessage(Messages.CHANGED_MATERIAL_MODE(properties.Brush.Mode));

                    properties.Brush.Materials = new LinkedHashMap<>();
                    switch (properties.Brush.Mode) {
                        case RANDOM:
                            properties.Brush.Materials.put(Material.STONE, 100);
                            break;
                        case LAYER:
                            properties.Brush.Materials.put(Material.STONE, 100);
                            break;
                        case GRADIENT:
                            properties.Brush.Materials.put(Material.WHITE_CONCRETE, 0);
                            properties.Brush.Materials.put(Material.BLACK_CONCRETE, 100);
                            break;
                    }

                    properties.Brush.Type.openBrushSettings(plugin, player, properties);
                    return;
                }
            }

            // Material Selection

            if (event.getSlot() >= 0 && event.getSlot() < 9) {
                Material material = inventory.getItem(event.getSlot() + 9).getType();
                ClickType clickType = event.getClick();

                switch (clickType) {
                    case LEFT:
                        properties.Brush.Materials.put(material, properties.Brush.Materials.get(material) + 1);
                        break;
                    case SHIFT_LEFT:
                        properties.Brush.Materials.put(material, properties.Brush.Materials.get(material) + 10);
                        break;
                    case RIGHT:
                        properties.Brush.Materials.put(material, properties.Brush.Materials.get(material) - 1);
                        break;
                    case SHIFT_RIGHT:
                        properties.Brush.Materials.put(material, properties.Brush.Materials.get(material) - 10);
                        break;
                    default:
                        break;
                }
                properties.Brush.Type.openBrushSettings(plugin, player, properties);
                return;
            }

            if (event.getSlot() >= 9 && event.getSlot() < 18) {
                if (!meta.customName().equals(materialSlotEmpty)) {
                    properties.Brush.Materials.remove(item.getType());
                    properties.Brush.Type.openBrushSettings(plugin, player, properties);
                    return;
                } else {
                    Material materialToAdd;
                    if (event.getClick() == ClickType.NUMBER_KEY) {
                        int hotbarIndex = event.getHotbarButton();
                        materialToAdd = player.getInventory().getItem(hotbarIndex).getType();
                    } else {
                        materialToAdd = event.getCursor().getType();
                    }

                    if (materialToAdd != Material.AIR) {
                        properties.Brush.Materials.put(materialToAdd, 0);
                        properties.Brush.Type.openBrushSettings(plugin, player, properties);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public static class SelectPaintMode implements InventoryHolder {
        private final Inventory inventory;

        public SelectPaintMode(Terraformer plugin, TerraformerProperties properties) {
            Component inventoryName = Component.text("Select Brush Paint Mode").color(NamedTextColor.BLUE);

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
}

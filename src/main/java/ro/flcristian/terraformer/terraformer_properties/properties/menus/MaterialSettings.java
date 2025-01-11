package ro.flcristian.terraformer.terraformer_properties.properties.menus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.material_history.MaterialHistory;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;
import ro.flcristian.terraformer.utility.MaterialNameFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MaterialSettings implements InventoryHolder {
    private static final Component materialSlotBlocked = Component.text("This brush doesn't use materials")
            .color(NamedTextColor.RED);
    private static final Component materialSlotEmptyPercentage = Component.text("Place a material in the empty slot")
            .color(NamedTextColor.RED);
    private static final Component materialSlotEmpty = Component.text("Empty slot").color(NamedTextColor.GRAY);
    private static final Component terraformer = Component.text("Terraformer").color(NamedTextColor.GRAY);
    private static final Component nextPage = Component.text("Next Page ➡").color(NamedTextColor.GREEN);
    private static final Component previousPage = Component.text("⬅ Previous Page").color(NamedTextColor.GREEN);
    private static final Component materialHistoryEmpty = Component
            .text("Your material properties will appear here.")
            .color(NamedTextColor.DARK_RED);
    private static final Component materialHistoryEntry = Component.text("Material History")
            .color(NamedTextColor.DARK_RED);

    private final boolean usesMaterials;
    private final Inventory inventory;
    private int currentMaterialPage;

    public MaterialSettings(Terraformer plugin, TerraformerProperties properties, boolean usesMaterials) {
        this.usesMaterials = usesMaterials;
        this.currentMaterialPage = 1;

        Component inventoryName = Component.text("Material Settings").color(NamedTextColor.DARK_RED)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                        .append(properties.Brush.Type.getName()));

        inventory = plugin.getServer().createInventory(this, 45,
                inventoryName);

        setUpMenu(properties);
    }

    public MaterialSettings(Terraformer plugin, TerraformerProperties properties, boolean usesMaterials,
            int currentMaterialPage) {
        this.usesMaterials = usesMaterials;
        this.currentMaterialPage = currentMaterialPage;

        Component inventoryName = Component.text("Material Settings").color(NamedTextColor.DARK_RED)
                .append(Component.text(" - ").color(NamedTextColor.GRAY)
                        .append(properties.Brush.Type.getName()));

        inventory = plugin.getServer().createInventory(this, 45,
                inventoryName);

        setUpMenu(properties);
    }

    private void setUpMenu(TerraformerProperties properties) {
        // Material Mode Selection

        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.customName(terraformer);
        empty.setItemMeta(emptyMeta);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, empty);
            inventory.setItem(27 + i, empty);
        }

        if (usesMaterials) {
            inventory.setItem(31, MaterialMode.getBrushSettingsItem(properties.Brush.Mode));
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

            // Calculate pagination
            int materialsPerPage = 9;
            int startIndex = (currentMaterialPage - 1) * materialsPerPage;
            int endIndex = Math.min(startIndex + materialsPerPage, materials.length);

            // Clear previous materials
            for (int i = 9; i < 27; i++) {
                inventory.setItem(i, null);
            }

            // Add materials for current page
            for (int i = startIndex; i < endIndex; i++) {
                int slot = i - startIndex;

                ItemMeta materialMeta = materials[i].getItemMeta();
                materialMeta.customName(Component.text(MaterialNameFormatter.format(materials[i].getType().toString()))
                        .color(NamedTextColor.DARK_GREEN));
                materials[i].setItemMeta(materialMeta);
                inventory.setItem(18 + slot, materials[i]);

                materialPercentageMeta.customName(
                        Component.text(properties.Brush.Materials.get(materials[i].getType()) + "%")
                                .color(NamedTextColor.YELLOW));
                materialPercentage.setItemMeta(materialPercentageMeta);
                inventory.setItem(9 + slot, materialPercentage);
            }

            // Fill empty slots
            ItemStack emptySlot = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            ItemMeta emptySlotMeta = emptySlot.getItemMeta();
            emptySlotMeta.customName(materialSlotEmpty);
            emptySlot.setItemMeta(emptySlotMeta);

            for (int i = endIndex - startIndex; i < 9; i++) {
                inventory.setItem(9 + i, materialSlot);
                inventory.setItem(18 + i, emptySlot);
            }

            // Add navigation buttons
            if (currentMaterialPage > 1) {
                ItemStack prevButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta prevMeta = prevButton.getItemMeta();
                prevMeta.customName(previousPage);
                prevButton.setItemMeta(prevMeta);
                inventory.setItem(27, prevButton);
            }

            if (materials.length >= (currentMaterialPage * materialsPerPage)) {
                ItemStack nextButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta nextMeta = nextButton.getItemMeta();
                nextMeta.customName(nextPage);
                nextButton.setItemMeta(nextMeta);
                inventory.setItem(35, nextButton);
            }

            // Material History
            ItemStack materialHistorySlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta materialHistorySlotMeta = materialHistorySlot.getItemMeta();
            materialHistorySlotMeta.customName(materialHistoryEmpty);
            materialHistorySlot.setItemMeta(materialHistorySlotMeta);
            for (int i = 36; i < 45; i++) {
                inventory.setItem(i, materialHistorySlot);
            }

            for (int i = 0; i < properties.MaterialHistory.size(); i++) {
                MaterialHistory materialPropertiesHistory = properties.MaterialHistory
                        .get(properties.MaterialHistory.size() - 1 - i);
                ItemStack materialHistory = new ItemStack(
                        MaterialMode.getBrushSettingsItem(materialPropertiesHistory.Mode));
                ItemMeta materialHistoryMeta = materialHistory.getItemMeta();
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("Mode: ").append(materialPropertiesHistory.Mode.getName())
                        .color(NamedTextColor.GOLD));
                lore.add(Component.empty());

                // Add each material and its percentage
                for (Map.Entry<Material, Integer> entry : materialPropertiesHistory.Materials.entrySet()) {
                    lore.add(Component
                            .text(MaterialNameFormatter.format(entry.getKey().toString()) + ": " + entry.getValue()
                                    + "%")
                            .color(NamedTextColor.GRAY));
                }

                lore.add(Component.empty());
                lore.add(Component.text("Click to apply").color(NamedTextColor.LIGHT_PURPLE));

                materialHistoryMeta.lore(lore);
                materialHistoryMeta.customName(materialHistoryEntry);
                materialHistory.setItemMeta(materialHistoryMeta);
                inventory.setItem(i + 36, materialHistory);
            }
        } else {
            for (int i = 9; i < 27; i++) {
                inventory.setItem(i, materialSlot);
            }
            for (int i = 36; i < 45; i++) {
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
            if (meta.customName().equals(materialSlotBlocked) || meta.customName().equals(terraformer)
                    || meta.customName().equals(materialSlotEmptyPercentage)
                    || meta.customName().equals(materialHistoryEmpty)) {
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

                    properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                    return;
                }
            }

            // Material Selection

            if (meta.customName().equals(nextPage)) {
                currentMaterialPage++;
                setUpMenu(properties);
                return;
            }

            if (meta.customName().equals(previousPage)) {
                currentMaterialPage--;
                setUpMenu(properties);
                return;
            }

            if (event.getSlot() >= 9 && event.getSlot() < 18) {
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
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            if (event.getSlot() >= 18 && event.getSlot() < 27) {
                if (!meta.customName().equals(materialSlotEmpty)) {
                    properties.Brush.Materials.remove(item.getType());
                    properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                    return;
                } else {
                    Material materialToAdd;
                    if (event.getClick() == ClickType.NUMBER_KEY) {
                        int hotbarIndex = event.getHotbarButton();
                        materialToAdd = player.getInventory().getItem(hotbarIndex).getType();
                    } else {
                        materialToAdd = event.getCursor().getType();
                    }

                    if (materialToAdd.isSolid()) {
                        properties.Brush.Materials.put(materialToAdd, 0);
                        properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                        return;
                    }
                }
            }

            // Material History

            if (meta.customName().equals(materialHistoryEntry)) {
                int index = event.getSlot() - 36;
                if (index < properties.MaterialHistory.size()) {
                    properties
                            .applyMaterialHistory(
                                    properties.MaterialHistory.get(properties.MaterialHistory.size() - 1 - index));
                    player.sendMessage(Messages.APPLIED_MATERIAL_HISTORY);
                    properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

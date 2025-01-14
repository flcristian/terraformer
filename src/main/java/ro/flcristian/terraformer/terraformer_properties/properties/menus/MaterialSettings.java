package ro.flcristian.terraformer.terraformer_properties.properties.menus;

import java.util.ArrayList;
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
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;
import ro.flcristian.terraformer.utility.MaterialNameFormatter;
import ro.flcristian.terraformer.utility.MaterialObjectsParser;
import ro.flcristian.terraformer.utility.SkullTexturesApplier;
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
    private static final Component materialAdd1ToAll = Component.text("Add 1% to all materials")
            .color(NamedTextColor.BLUE);
    private static final Component materialAdd10ToAll = Component.text("Add 10% to all materials")
            .color(NamedTextColor.BLUE);
    private static final Component materialSubtract1FromAll = Component.text("Subtract 1% from all materials")
            .color(NamedTextColor.RED);
    private static final Component materialSubtract10FromAll = Component.text("Subtract 10% from all materials")
            .color(NamedTextColor.RED);
    private static final Component materialResetAll = Component.text("Reset materials")
            .color(NamedTextColor.LIGHT_PURPLE);

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
        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.customName(terraformer);
        empty.setItemMeta(emptyMeta);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, empty);
            inventory.setItem(27 + i, empty);
        }

        // Material Selection

        ItemStack materialSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta materialSlotMeta = materialSlot.getItemMeta();

        if (usesMaterials) {
            inventory.setItem(31, MaterialMode.getBrushSettingsItem(properties.Brush.Mode));

            try {
                ItemStack materialSubtract1 = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta materialSubtract1Meta = materialSubtract1.getItemMeta();
                materialSubtract1Meta.customName(materialSubtract1FromAll);
                SkullTexturesApplier.applyTextures(materialSubtract1Meta,
                        "http://textures.minecraft.net/texture/a39c846f65d5f272a839fd9c2aeb11bdc8e3f8229fbe3583486e78f4c23c8b5b");
                materialSubtract1.setItemMeta(materialSubtract1Meta);
                inventory.setItem(0, materialSubtract1);

                ItemStack materialSubtract10 = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta materialSubtract10Meta = materialSubtract10.getItemMeta();
                materialSubtract10Meta.customName(materialSubtract10FromAll);
                SkullTexturesApplier.applyTextures(materialSubtract10Meta,
                        "http://textures.minecraft.net/texture/ff32222def1c7b3bd04513b1a40493407c4287b6ec3943f7333f715773d4cb61");
                materialSubtract10.setItemMeta(materialSubtract10Meta);
                inventory.setItem(1, materialSubtract10);

                ItemStack materialReset = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta materialResetMeta = materialReset.getItemMeta();
                materialResetMeta.customName(materialResetAll);
                SkullTexturesApplier.applyTextures(materialResetMeta,
                        "http://textures.minecraft.net/texture/3f4105c62e3f75ab2792976068d8f2e1b48d671975e095a3e7a30c097a4e1c68");
                materialReset.setItemMeta(materialResetMeta);
                inventory.setItem(4, materialReset);

                ItemStack materialAdd10 = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta materialAdd10Meta = materialAdd10.getItemMeta();
                materialAdd10Meta.customName(materialAdd10ToAll);
                SkullTexturesApplier.applyTextures(materialAdd10Meta,
                        "http://textures.minecraft.net/texture/979f9a59c897af032d4002596fab2e4e9f22f5ae1ea370f755a5ee9396b372c9");
                materialAdd10.setItemMeta(materialAdd10Meta);
                inventory.setItem(7, materialAdd10);

                ItemStack materialAdd1 = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta materialAdd1Meta = materialAdd1.getItemMeta();
                materialAdd1Meta.customName(materialAdd1ToAll);
                SkullTexturesApplier.applyTextures(materialAdd1Meta,
                        "http://textures.minecraft.net/texture/28edefae8e1cce597e9c70c877b83943ee282166f10c0a3def3035e34d91b9a4");
                materialAdd1.setItemMeta(materialAdd1Meta);
                inventory.setItem(8, materialAdd1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            materialSlotMeta.customName(materialSlotEmptyPercentage);
            materialSlot.setItemMeta(materialSlotMeta);

            ItemStack[] materials = properties.Brush.Materials.keySet().stream()
                    .map(material -> {
                        if (material == Material.WATER) {
                            return new ItemStack(Material.WATER_BUCKET);
                        }

                        if (material == Material.LAVA) {
                            return new ItemStack(Material.LAVA_BUCKET);
                        }

                        return new ItemStack(material);
                    })
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
                Material stackMaterial;
                switch (materials[i].getType()) {
                    case Material.WATER_BUCKET:
                        stackMaterial = Material.WATER;
                        break;
                    case Material.LAVA_BUCKET:
                        stackMaterial = Material.LAVA;
                        break;
                    default:
                        stackMaterial = materials[i].getType();
                        break;
                }
                materialMeta.customName(
                        Component.text(MaterialNameFormatter.format(stackMaterial.toString()))
                                .color(NamedTextColor.DARK_GREEN));
                materials[i].setItemMeta(materialMeta);
                inventory.setItem(18 + slot, materials[i]);

                materialPercentageMeta.customName(
                        Component.text(properties.Brush.Materials.get(stackMaterial) + "%")
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
            materialSlotMeta.customName(materialSlotBlocked);
            materialSlot.setItemMeta(materialSlotMeta);

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

            MaterialMode[] materialModes = properties.Brush.Type == BrushType.FOLIAGE
                    ? new MaterialMode[] { MaterialMode.RANDOM }
                    : MaterialMode.values();

            for (int i = 0; i < materialModes.length; i++) {
                if (meta.customName().equals(materialModes[i].getName())) {
                    properties.Brush.setMode(materialModes[(i + 1) % materialModes.length]);
                    player.sendMessage(Messages.CHANGED_MATERIAL_MODE(properties.Brush.Mode));

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
                Material stackMaterial = inventory.getItem(event.getSlot() + 9).getType();
                Material material;
                switch (stackMaterial) {
                    case WATER_BUCKET:
                        material = Material.WATER;
                        break;
                    case LAVA_BUCKET:
                        material = Material.LAVA;
                        break;
                    default:
                        material = stackMaterial;
                        break;
                }

                ClickType clickType = event.getClick();

                switch (clickType) {
                    case LEFT:
                        properties.Brush.Materials.put(material,
                                Math.min(100, properties.Brush.Materials.get(material) + 1));
                        break;
                    case SHIFT_LEFT:
                        properties.Brush.Materials.put(material,
                                Math.min(100, properties.Brush.Materials.get(material) + 10));
                        break;
                    case RIGHT:
                        properties.Brush.Materials.put(material,
                                Math.max(0, properties.Brush.Materials.get(material) - 1));
                        break;
                    case SHIFT_RIGHT:
                        properties.Brush.Materials.put(material,
                                Math.max(0, properties.Brush.Materials.get(material) - 10));
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

                    Material material;
                    switch (materialToAdd) {
                        case WATER_BUCKET:
                            material = Material.WATER;
                            break;
                        case LAVA_BUCKET:
                            material = Material.LAVA;
                            break;
                        default:
                            material = materialToAdd;
                            break;
                    }

                    if (MaterialObjectsParser.isValidMaterial(properties.Brush.Type, material)) {
                        properties.Brush.Materials.put(material, 0);
                        properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                        return;
                    }
                }
            }

            if (meta.customName().equals(materialSubtract1FromAll)) {
                for (Material material : properties.Brush.Materials.keySet()) {
                    properties.Brush.Materials.put(material, Math.max(0, properties.Brush.Materials.get(material) - 1));
                }
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            if (meta.customName().equals(materialSubtract10FromAll)) {
                for (Material material : properties.Brush.Materials.keySet()) {
                    properties.Brush.Materials.put(material,
                            Math.max(0, properties.Brush.Materials.get(material) - 10));
                }
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            if (meta.customName().equals(materialResetAll)) {
                properties.Brush.setMode(properties.Brush.Mode);
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            if (meta.customName().equals(materialAdd1ToAll)) {
                for (Material material : properties.Brush.Materials.keySet()) {
                    properties.Brush.Materials.put(material,
                            Math.min(100, properties.Brush.Materials.get(material) + 1));
                }
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            if (meta.customName().equals(materialAdd10ToAll)) {
                for (Material material : properties.Brush.Materials.keySet()) {
                    properties.Brush.Materials.put(material,
                            Math.min(100, properties.Brush.Materials.get(material) + 10));
                }
                properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                return;
            }

            // Material History

            if (meta.customName().equals(materialHistoryEntry)) {
                int index = event.getSlot() - 36;
                if (index < properties.MaterialHistory.size()) {
                    int materialHistoryIndex = properties.MaterialHistory.size() - 1 - index;

                    boolean validMaterials = true;
                    for (Material material : properties.MaterialHistory.get(materialHistoryIndex).Materials.keySet()) {
                        if (!MaterialObjectsParser.isValidMaterial(properties.Brush.Type, material)) {
                            validMaterials = false;
                            break;
                        }
                    }

                    if (validMaterials) {
                        properties
                                .applyMaterialHistory(
                                        properties.MaterialHistory.get(materialHistoryIndex));
                        player.sendMessage(Messages.APPLIED_MATERIAL_HISTORY);
                        properties.Brush.Type.openMaterialSettings(plugin, player, properties, currentMaterialPage);
                    } else {
                        player.sendMessage(Messages.CANT_APPLY_MATERIAL_HISTORY);
                    }
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

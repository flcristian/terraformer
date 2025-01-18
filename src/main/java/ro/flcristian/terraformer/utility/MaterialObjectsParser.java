package ro.flcristian.terraformer.utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

public class MaterialObjectsParser {
    private MaterialObjectsParser() {
    }

    public static Map<Material, Integer> parseMaterialPercentages(BrushType brushType, String materials,
            MaterialMode materialMode) {
        Map<Material, Integer> materialMap = new LinkedHashMap<>();

        // Check if using percentage format
        if (materials.contains("%")) {
            // Existing percentage parsing logic
            String[] materialEntries = materials.split(",");
            for (String entry : materialEntries) {
                String[] parts = entry.trim().split("%");
                if (parts.length != 2) {
                    throw new IllegalArgumentException(
                            "Invalid material format. Expected format: '70%material_name1,30%material_name2'");
                }

                int percentage = Integer.parseInt(parts[0]);
                String materialName = parts[1].toUpperCase();
                Material material = getTrueMaterial(Material.getMaterial(materialName));

                if (material == null || !material.isBlock()) {
                    throw new IllegalArgumentException("Invalid material: " + materialName);
                }

                materialMap.put(material, percentage);
            }
        } else {
            // Equal distribution logic
            String[] materialNames = materials.split(",");

            if (materialMode == MaterialMode.GRADIENT) {
                int numMaterials = materialNames.length;

                if (numMaterials == 1) {
                    Material material = getTrueMaterial(Material.getMaterial(materialNames[0].trim().toUpperCase()));
                    if (material == null || !material.isBlock()) {
                        throw new IllegalArgumentException("Invalid material: " + materialNames[0]);
                    }

                    materialMap.put(material, 50);
                } else {
                    int interval = 100 / (numMaterials - 1);
                    for (int i = 0; i < materialNames.length; i++) {
                        Material material = getTrueMaterial(
                                Material.getMaterial(materialNames[i].trim().toUpperCase()));
                        if (material == null || !material.isBlock()) {
                            throw new IllegalArgumentException("Invalid material: " + materialNames[i]);
                        }

                        int percentage = (i == materialNames.length - 1) ? 100 : i * interval;
                        materialMap.put(material, percentage);
                    }
                }
            } else {
                // Original equal distribution logic for non-GRADIENT mode
                int equalPercentage = 100 / materialNames.length;
                int remainder = 100 % materialNames.length;

                for (String materialName : materialNames) {
                    Material material = getTrueMaterial(Material.getMaterial(materialName.trim().toUpperCase()));
                    if (material == null || !material.isBlock()) {
                        throw new IllegalArgumentException("Invalid material: " + materialName);
                    }

                    // Add extra 1% to first few materials if there's a remainder
                    int percentage = equalPercentage + (remainder > 0 ? 1 : 0);
                    remainder--;
                    materialMap.put(material, percentage);
                }
            }
        }

        materialMap.remove(Material.AIR);
        materialMap.remove(Material.FIRE);

        return materialMap;
    }

    public static List<Material> parseMaskMaterials(String materials) {
        List<Material> maskMaterials = new ArrayList<>();
        String[] materialNames = materials.split(",");

        for (String materialName : materialNames) {
            Material material = Material.getMaterial(materialName.trim().toUpperCase());
            if (material == null) {
                throw new IllegalArgumentException("Invalid material: " + materialName);
            }
            maskMaterials.add(material);
        }

        return maskMaterials;
    }

    public static ItemStack getItemStackFromMaterial(Material material) {
        return materialItemStackMap().get(material) == null ? new ItemStack(material)
                : materialItemStackMap().get(material);
    }

    public static Material getMaterialFromItemStack(ItemStack item) {
        return materialMaterialMap().get(item.getType()) == null ? item.getType()
                : materialMaterialMap().get(item.getType());
    }

    public static Material getTrueMaterial(Material material) {
        return materialMaterialMap().get(material) == null ? material : materialMaterialMap().get(material);
    }

    private static List<Pair<Material, Material>> materialItemStackPairs = new ArrayList<>(
            List.of(new Pair<>(Material.WATER, Material.WATER_BUCKET),
                    new Pair<>(Material.LAVA, Material.LAVA_BUCKET),
                    new Pair<>(Material.SWEET_BERRY_BUSH, Material.SWEET_BERRIES),
                    new Pair<>(Material.CAVE_VINES, Material.GLOW_BERRIES),
                    new Pair<>(Material.SWEET_BERRY_BUSH, Material.SWEET_BERRIES),
                    new Pair<>(Material.COCOA, Material.COCOA_BEANS),
                    new Pair<>(Material.CHORUS_PLANT, Material.CHORUS_FRUIT),
                    new Pair<>(Material.POTATOES, Material.POTATO),
                    new Pair<>(Material.CARROTS, Material.CARROT),
                    new Pair<>(Material.BEETROOTS, Material.BEETROOT),
                    new Pair<>(Material.KELP_PLANT, Material.KELP)));

    private static Map<Material, ItemStack> materialItemStackMap() {
        Map<Material, ItemStack> map = new LinkedHashMap<>();
        for (Pair<Material, Material> pair : materialItemStackPairs) {
            map.put(pair.getFirst(), new ItemStack(pair.getSecond()));
        }
        return map;
    }

    private static Map<Material, Material> materialMaterialMap() {
        Map<Material, Material> map = new LinkedHashMap<>();
        for (Pair<Material, Material> pair : materialItemStackPairs) {
            map.put(pair.getSecond(), pair.getFirst());
        }
        return map;
    }
}

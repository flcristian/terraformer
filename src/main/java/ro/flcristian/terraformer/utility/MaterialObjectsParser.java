package ro.flcristian.terraformer.utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

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
                Material material = Material.getMaterial(materialName);

                if (material == null) {
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
                    Material material = Material.getMaterial(materialNames[0].trim().toUpperCase());
                    if (material == null || !isValidMaterial(brushType, material)) {
                        throw new IllegalArgumentException("Invalid material: " + materialNames[0]);
                    }

                    materialMap.put(material, 50);
                } else {
                    int interval = 100 / (numMaterials - 1);
                    for (int i = 0; i < materialNames.length; i++) {
                        Material material = Material.getMaterial(materialNames[i].trim().toUpperCase());
                        if (material == null || !isValidMaterial(brushType, material)) {
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
                    Material material = Material.getMaterial(materialName.trim().toUpperCase());
                    if (material == null || !isValidMaterial(brushType, material)) {
                        throw new IllegalArgumentException("Invalid material: " + materialName);
                    }

                    // Add extra 1% to first few materials if there's a remainder
                    int percentage = equalPercentage + (remainder > 0 ? 1 : 0);
                    remainder--;
                    materialMap.put(material, percentage);
                }
            }
        }

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

    public static boolean isValidMaterial(BrushType brushType, Material material) {
        if (!material.isBlock())
            return false;

        if (brushType == BrushType.FOLIAGE) {
            return !material.isSolid() && material != Material.WATER && material != Material.LAVA;
        } else {
            return material.isSolid() || material == Material.WATER || material == Material.LAVA;
        }
    }
}

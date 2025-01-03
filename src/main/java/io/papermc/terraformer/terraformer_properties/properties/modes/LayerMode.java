package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;

public class LayerMode implements Mode {
    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        List<Material> materials = new ArrayList<>(properties.Materials.keySet());

        if (materials.isEmpty()) {
            return Material.STONE;
        }

        double layerHeight;
        if (properties.Type == BrushType.PAINT_TOP || properties.Type == BrushType.PAINT_WALL
                || properties.Type == BrushType.PAINT_BOTTOM) {
            layerHeight = (double) properties.BrushDepth / materials.size();
        } else {
            layerHeight = (double) (properties.BrushSize * 2 - 1) / materials.size();
        }

        // Get relative Y position based on BrushType
        int relativeY;
        if (properties.Type == BrushType.PAINT_TOP) {
            // Calculate from the top
            relativeY = targetLocation.getBlockY() - location.getBlockY();
        } else if (properties.Type == BrushType.PAINT_BOTTOM) {
            // Calculate from the bottom
            Collections.reverse(materials);
            relativeY = targetLocation.getBlockY() - location.getBlockY();
        } else {
            // Calculate from the center
            relativeY = (targetLocation.getBlockY() + properties.BrushSize) - location.getBlockY();
        }

        int materialIndex = (int) (relativeY / layerHeight);
        materialIndex = Math.min(materialIndex, materials.size() - 1);
        materialIndex = Math.max(0, materialIndex);

        return materials.get(materialIndex);
    }

    public boolean containsAllMaterials(BrushProperties properties) {
        return properties.Materials.keySet().size() <= properties.BrushSize * 2 - 1;
    }
}
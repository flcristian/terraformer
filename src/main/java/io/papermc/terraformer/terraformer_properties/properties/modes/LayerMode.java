package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class LayerMode implements Mode {
    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        // Get available materials
        List<Material> materials = new ArrayList<>(properties.Materials.keySet());
        if (materials.isEmpty()) {
            return Material.STONE;
        }

        // Calculate total height (diameter)
        int totalHeight = properties.BrushSize * 2;

        // Calculate blocks per material (equal distribution)
        int blocksPerMaterial = totalHeight / materials.size();

        // Get relative Y position from bottom of sphere
        int relativeY = location.getBlockY() - (targetLocation.getBlockY() - properties.BrushSize);

        // Calculate which material index we're at
        int materialIndex = relativeY / blocksPerMaterial;

        // Clamp index to valid range
        materialIndex = Math.min(materialIndex, materials.size() - 1);
        materialIndex = Math.max(0, materialIndex);

        return materials.get(materialIndex);
    }
}

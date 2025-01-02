package io.papermc.terraformer.terraformer_properties.properties.modes;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class GradientMode implements Mode {
    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        if (properties.Materials.isEmpty()) {
            return Material.STONE;
        }

        // Calculate relative Y position from bottom to top of sphere
        int totalHeight = properties.BrushSize * 2;
        int relativeY = location.getBlockY() - (targetLocation.getBlockY() - properties.BrushSize);

        // Convert Y position to percentage (0-100)
        double yPercentage = (relativeY * 100.0) / totalHeight;

        // Track cumulative percentage for range calculation
        int cumulativePercentage = 0;

        // Find material based on percentage ranges
        for (var entry : properties.Materials.entrySet()) {
            cumulativePercentage += entry.getValue();
            if (yPercentage <= cumulativePercentage) {
                return entry.getValue() > 0 ? entry.getKey() : Material.STONE;
            }
        }

        // Fallback to last material if we somehow exceed 100%
        return properties.Materials.keySet().iterator().next();
    }
}

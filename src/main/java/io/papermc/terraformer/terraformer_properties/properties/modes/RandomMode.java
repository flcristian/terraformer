package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class RandomMode implements Mode {
    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        int random = new Random().nextInt(100);
        int currentSum = 0;

        for (Map.Entry<Material, Integer> entry : properties.Materials.entrySet()) {
            currentSum += entry.getValue();
            if (random < currentSum) {
                return entry.getKey();
            }
        }

        return properties.Materials.entrySet().iterator().next().getKey();
    }

    public boolean containsAllMaterials(BrushProperties properties) {
        return properties.Materials.size() > 0;
    }
}

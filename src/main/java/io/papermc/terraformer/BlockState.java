package io.papermc.terraformer;

import org.bukkit.Location;
import org.bukkit.Material;

public record BlockState(Location location, Material material, Location targetLocation, BrushType brushType,
        int brushSize) {
}
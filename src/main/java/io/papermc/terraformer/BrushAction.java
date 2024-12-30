package io.papermc.terraformer;

import org.bukkit.Location;

public record BrushAction(Location targetLocation, BrushType brushType, int brushSize) {
}
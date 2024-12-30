package io.papermc.terraformer;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public record BlockState(Location location, BlockData blockData, Location targetLocation,
                BrushType brushType, int brushSize) {
}
package io.papermc.terraformer.terraformer_properties.block_history;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import io.papermc.terraformer.terraformer_properties.properties.BrushType;

public record BlockStateHistory(Location location, BlockState blockState, Location targetLocation,
                BrushType brushType, int brushSize) {
}
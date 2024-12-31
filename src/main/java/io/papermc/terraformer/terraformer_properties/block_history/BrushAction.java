package io.papermc.terraformer.terraformer_properties.block_history;

import org.bukkit.Location;

import io.papermc.terraformer.terraformer_properties.properties.BrushType;

public record BrushAction(Location targetLocation, BrushType brushType, int brushSize) {
}
package io.papermc.terraformer.terraformer_properties.block_history;

import org.bukkit.Location;

import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public record BrushAction(Location targetLocation, BrushProperties brushProperties) {

    public BrushAction(Location targetLocation, TerraformerProperties properties) {
        this(targetLocation, properties.Brush);
    }
}
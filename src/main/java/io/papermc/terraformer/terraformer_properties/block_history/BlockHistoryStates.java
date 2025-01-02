package io.papermc.terraformer.terraformer_properties.block_history;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public record BlockHistoryStates(Stack<BlockState> states, Location targetLocation,
        BrushProperties brushProperties) {

    public BlockHistoryStates(Stack<BlockState> states, Location targetLocation, TerraformerProperties properties) {
        this(states, targetLocation, properties.Brush);
    }
}

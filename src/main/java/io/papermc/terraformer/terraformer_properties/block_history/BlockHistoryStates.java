package io.papermc.terraformer.terraformer_properties.block_history;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import io.papermc.terraformer.terraformer_properties.properties.BrushType;

public record BlockHistoryStates(Stack<BlockState> states, Location targetLocation,
        BrushType brushType, int brushSize) {
}

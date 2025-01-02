package io.papermc.terraformer.terraformer_properties.properties.brushes;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockHistoryStates;

public class BrushBall extends Brush {
    public static void brush(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = properties.BrushSize;

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int y = -brushSize; y <= brushSize; y++) {
                for (int z = -brushSize; z <= brushSize; z++) {
                    Location loc = targetLocation.clone().add(x, y, z);
                    if (loc.distance(targetLocation) < brushSize) {
                        states.push(loc.getBlock().getState());
                    }
                }
            }
        }

        BlockHistoryStates historyStates = new BlockHistoryStates(states, targetLocation, properties.Brush,
                properties.BrushSize);
        if (!isRedo) {
            properties.History
                    .pushModification(historyStates);
        } else {
            properties.History.pushRedo(historyStates);
        }

        for (BlockState state : states) {
            Block block = state.getBlock();
            if (!block.getType().isSolid()) {
                block.setType(properties.getRandomMaterial());
            }
        }

        for (BlockState state : states) {
            Block block = state.getBlock();
            if (block.getType().isSolid()) {
                block.setType(properties.getRandomMaterial());
            }
        }
    }
}

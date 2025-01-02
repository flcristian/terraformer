package io.papermc.terraformer.terraformer_properties.properties.brushes;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushBall extends Brush {
    public static void brush(Terraformer plguin, Player player, BrushProperties brushProperties,
            Location targetLocation,
            boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;

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

        BlockHistoryStates historyStates = new BlockHistoryStates(states, targetLocation, brushProperties);
        TerraformerProperties terraformerProperties = plguin.getTerraformer(player);
        if (terraformerProperties == null) {
            throw new IllegalArgumentException("Player is not in terraformer mode");
        }

        if (!isRedo) {
            terraformerProperties.History
                    .pushModification(historyStates);
        } else {
            terraformerProperties.History.pushRedo(historyStates);
        }

        for (BlockState state : states) {
            Block block = state.getBlock();
            if (!block.getType().isSolid()) {
                block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation));
            }
        }

        for (BlockState state : states) {
            Block block = state.getBlock();
            if (block.getType().isSolid()) {
                block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation));
            }
        }
    }
}

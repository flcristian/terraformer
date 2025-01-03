package io.papermc.terraformer.terraformer_properties.properties.brushes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushExtrude extends Brush {
    public static void brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        HashMap<Location, BlockData> extrudeedBlocks = new HashMap<>();
        int brushSize = brushProperties.BrushSize;

        // Gather blocks in sphere
        for (int x = -brushSize; x <= brushSize; x++) {
            for (int y = -brushSize; y <= brushSize; y++) {
                for (int z = -brushSize; z <= brushSize; z++) {
                    Location loc = targetLocation.clone().add(x, y, z);
                    if (loc.distance(targetLocation) <= brushSize) {
                        states.push(loc.getBlock().getState());
                    }
                }
            }
        }

        BlockHistoryStates historyStates = new BlockHistoryStates(states, targetLocation, brushProperties.clone());
        TerraformerProperties terraformerProperties = plugin.getTerraformer(player);
        if (terraformerProperties == null) {
            throw new IllegalArgumentException("Player is not in terraformer mode");
        }

        if (!isRedo) {
            terraformerProperties.History
                    .pushModification(historyStates);
        } else {
            terraformerProperties.History.pushRedo(historyStates);
        }

        // Process each block
        for (BlockState state : states) {
            Location loc = state.getLocation();
            List<BlockData> neighbors = new ArrayList<>();

            // Get neighbors at same Y level first
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0)
                        continue;
                    Location neighborLoc = loc.clone().add(dx, 0, dz);
                    Block neighborBlock = neighborLoc.getBlock();
                    if (neighborBlock.getType().isSolid()) {
                        neighbors.add(neighborBlock.getBlockData());
                    }
                }
            }

            // Check if extrudeing needed
            if (shouldExtrude(state.getBlockData(), neighbors)) {
                BlockData extrudeed = findMostCommonBlock(neighbors);
                if (extrudeed != null) {
                    extrudeedBlocks.put(loc, extrudeed);
                }
            }
        }

        // Apply changes
        for (Map.Entry<Location, BlockData> entry : extrudeedBlocks.entrySet()) {
            entry.getKey().getBlock().setBlockData(entry.getValue());
        }
    }

    private static boolean shouldExtrude(BlockData current, List<BlockData> neighbors) {
        if (neighbors.size() == 0)
            return false;

        // Count how many neighbors are different from the current block
        int differentNeighbors = 0;
        for (BlockData neighbor : neighbors) {
            if (!neighbor.getMaterial().equals(current.getMaterial())) {
                differentNeighbors++;
            }
        }

        // Only extrude if more than 50% of neighbors are different
        return differentNeighbors > (neighbors.size() * 0.5);
    }
}

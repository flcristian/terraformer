package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushErode extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        Map<Location, BlockData> erodedBlocks = new HashMap<>();
        int brushSize = brushProperties.BrushSize;

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
            boolean isCurrentSolid = state.getBlock().isSolid();

            // Get same-Y-level neighbors in 3x3 area
            List<Block> neighbors = new ArrayList<>();
            int solidCount = 0;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0)
                        continue;
                    Location neighborLoc = loc.clone().add(dx, 0, dz);
                    Block neighborBlock = neighborLoc.getBlock();
                    neighbors.add(neighborBlock);
                    if (neighborBlock.getType().isSolid()) {
                        solidCount++;
                    }
                }
            }

            double solidPercentage = (double) solidCount / neighbors.size();

            // Fill small holes (air blocks with many solid neighbors)
            if (!isCurrentSolid && solidPercentage > 0.75) {
                List<BlockData> solidNeighbors = neighbors.stream()
                        .filter(b -> b.getType().isSolid())
                        .map(Block::getBlockData)
                        .collect(Collectors.toList());
                BlockData mostCommon = findMostCommonBlock(solidNeighbors);
                if (mostCommon != null) {
                    erodedBlocks.put(loc, mostCommon);
                }
            }
            // Remove protruding blocks (solid blocks with many air neighbors)
            else if (isCurrentSolid && solidPercentage <= 0.68) {
                erodedBlocks.put(loc, Material.AIR.createBlockData());
            }
        }

        // Apply changes
        for (Map.Entry<Location, BlockData> entry : erodedBlocks.entrySet()) {
            entry.getKey().getBlock().setBlockData(entry.getValue(), brushProperties.BlockUpdates);
        }

        return true;
    }
}

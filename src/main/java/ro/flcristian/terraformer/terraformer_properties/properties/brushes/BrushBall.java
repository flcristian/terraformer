package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushBall extends Brush {
    private static final int[][] SIZE_1_OFFSETS = {
            { 0, 1, 0 }, { 0, -1, 0 },
            { 1, 0, 0 }, { -1, 0, 0 },
            { 0, 0, 1 }, { 0, 0, -1 }
    };

    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        if (checkMaterialPercentages(brushProperties, player))
            return false;

        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;

        if (brushSize == 1) {
            // Special case for size 1 - diamond pattern
            for (int[] offset : SIZE_1_OFFSETS) {
                Location loc = targetLocation.clone().add(offset[0], offset[1], offset[2]);
                states.push(loc.getBlock().getState());
            }
            states.push(targetLocation.getBlock().getState());
        } else {
            double threshold = brushSize < 10 ? brushSize - (0.5 - 0.05 * brushSize) : brushSize - 0.5;
            for (int x = -brushSize; x <= brushSize; x++) {
                for (int y = -brushSize; y <= brushSize; y++) {
                    for (int z = -brushSize; z <= brushSize; z++) {
                        Location loc = targetLocation.clone().add(x, y, z);
                        double distance = loc.distance(targetLocation);

                        if (distance <= threshold) {
                            states.push(loc.getBlock().getState());
                        }
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
            terraformerProperties.History.pushModification(historyStates);
        } else {
            terraformerProperties.History.pushRedo(historyStates);
        }

        // Process non-solid blocks first
        for (BlockState state : states) {
            Block block = state.getBlock();
            if (!block.getType().isSolid()
                    && (brushProperties.Mask.isEmpty() || brushProperties.Mask.contains(block.getType()))) {
                block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation),
                        brushProperties.BlockUpdates);
            }
        }

        // Then process solid blocks
        for (BlockState state : states) {
            Block block = state.getBlock();
            if (block.getType().isSolid()
                    && (brushProperties.Mask.isEmpty() || brushProperties.Mask.contains(block.getType()))) {
                block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation),
                        brushProperties.BlockUpdates);
            }
        }

        return true;
    }
}
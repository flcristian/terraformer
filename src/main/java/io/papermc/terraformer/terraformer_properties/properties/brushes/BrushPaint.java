package io.papermc.terraformer.terraformer_properties.properties.brushes;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushPaint extends Brush {
    public static void brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;
        int brushDepth = brushProperties.BrushDepth;

        // Check blocks in a cylinder shape
        for (int x = -brushSize; x <= brushSize; x++) {
            for (int z = -brushSize; z <= brushSize; z++) {
                // Calculate distance from center for cylinder shape
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= brushSize) {
                    // Determine y range based on brush type
                    switch (brushProperties.Type) {
                        case PAINT_TOP:
                            // Current behavior - top down
                            for (int y = 0; y < brushDepth; y++) {
                                Location loc = targetLocation.clone().add(x, -y, z);
                                Block block = loc.getBlock();
                                if (block.getType().isSolid()) {
                                    states.push(block.getState());
                                }
                            }
                            break;

                        case PAINT_WALL:
                            // Center out
                            for (int y = -brushDepth / 2; y <= brushDepth / 2; y++) {
                                Location loc = targetLocation.clone().add(x, y, z);
                                Block block = loc.getBlock();
                                if (block.getType().isSolid()) {
                                    states.push(block.getState());
                                }
                            }
                            break;

                        case PAINT_BOTTOM:
                            // Bottom up
                            for (int y = 0; y < brushDepth; y++) {
                                Location loc = targetLocation.clone().add(x, y, z);
                                Block block = loc.getBlock();
                                if (block.getType().isSolid()) {
                                    states.push(block.getState());
                                }
                            }
                            break;
                        default:
                            return;
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

        // Single pass: replace non-solids with air
        for (BlockState state : states) {
            if (!state.getBlock().getType().isSolid()) {
                state.getBlock().setType(Material.AIR);
            }
        }

        // Single pass: replace solid blocks with the new material
        for (BlockState state : states) {
            Block block = state.getBlock();
            if (block.getType().isSolid()) {
                block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation));
            }
        }
    }
}
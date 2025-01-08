package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushPaint extends Brush {
    public static void brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;
        int brushDepth = brushProperties.BrushDepth;

        Map<String, Location> surfaceLocationMap = new HashMap<>();

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int z = -brushSize; z <= brushSize; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= brushSize) {
                    switch (brushProperties.Type) {
                        case PAINT_TOP:
                            for (int y = 0; y < brushDepth; y++) {
                                Location loc = targetLocation.clone().add(x, -y, z);
                                Block block = loc.getBlock();
                                if (block.getType().isSolid()) {
                                    states.push(block.getState());
                                }
                            }
                            break;

                        case PAINT_SURFACE:
                            Location surfaceLocation = targetLocation.clone().add(x, 0, z);

                            boolean foundSurface = false;
                            for (int searchDepth = 0; searchDepth <= brushSize; searchDepth++) {
                                Location checkLoc = surfaceLocation.clone().add(0, -searchDepth, 0);
                                if (checkLoc.getBlock().getType().isSolid() &&
                                        !checkLoc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
                                    surfaceLocation = checkLoc;
                                    foundSurface = true;
                                    break;
                                }
                            }

                            if (foundSurface) {
                                surfaceLocationMap.put(
                                        surfaceLocation.getBlockX() + "," + surfaceLocation.getBlockZ(),
                                        surfaceLocation);
                                for (int y = 0; y < brushDepth; y++) {
                                    Location loc = surfaceLocation.clone().add(0, -y, 0);
                                    Block block = loc.getBlock();
                                    if (block.getType().isSolid()) {
                                        states.push(block.getState());
                                    }
                                }
                            }

                            break;

                        case PAINT_WALL:
                            for (int y = brushDepth % 2 == 1 ? -brushDepth / 2 : -brushDepth / 2 + 1; y <= brushDepth
                                    / 2; y++) {
                                Location loc = targetLocation.clone().add(x, y, z);
                                Block block = loc.getBlock();
                                if (block.getType().isSolid()) {
                                    states.push(block.getState());
                                }
                            }
                            break;

                        case PAINT_BOTTOM:
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
            if (brushProperties.Type != BrushType.PAINT_SURFACE) {
                if (block.getType().isSolid()) {
                    block.setType(brushProperties.getMaterial(block.getLocation(), targetLocation));
                }
            } else {
                Location surfaceLocation = surfaceLocationMap.get(block.getLocation().getBlockX() + ","
                        + block.getLocation().getBlockZ());

                if (surfaceLocation != null) {
                    if (block.getType().isSolid()) {
                        block.setType(brushProperties.getMaterial(block.getLocation(), surfaceLocation));
                    }
                }
            }
        }
    }
}
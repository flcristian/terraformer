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

public class BrushRise extends Brush {
    public static void brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        Map<Location, Material> changes = new HashMap<>();
        int brushSize = brushProperties.BrushSize;
        int riseHeight = brushProperties.BrushDepth;

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int z = -brushSize; z <= brushSize; z++) {
                if (x * x + z * z <= brushSize * brushSize) {
                    Location surfaceLocation = findSurface(targetLocation.clone().add(x, 0, z));
                    if (surfaceLocation != null) {
                        for (int y = 0; y <= riseHeight; y++) {
                            Location loc = surfaceLocation.clone().add(0, y, 0);
                            Block block = loc.getBlock();
                            states.push(block.getState());
                        }

                        Material surfaceMaterial = surfaceLocation.getBlock().getType();
                        for (int y = 1; y <= riseHeight; y++) {
                            Location loc = surfaceLocation.clone().add(0, y, 0);
                            changes.put(loc, surfaceMaterial);
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

        for (Map.Entry<Location, Material> entry : changes.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
    }

    private static Location findSurface(Location location) {
        for (int y = 0; y >= -64; y--) {
            Location checkLoc = location.clone().add(0, y, 0);
            Block block = checkLoc.getBlock();

            if (block.getType().isSolid() &&
                    !block.getRelative(0, 1, 0).getType().isSolid()) {
                return checkLoc;
            }
        }
        return null;
    }
}
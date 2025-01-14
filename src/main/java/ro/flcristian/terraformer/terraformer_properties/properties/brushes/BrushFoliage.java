package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushFoliage extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;

        Map<String, Location> surfaceLocationMap = new HashMap<>();

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int z = -brushSize; z <= brushSize; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= brushSize) {
                    Location surfaceLocation = targetLocation.clone().add(x, 0, z);

                    boolean foundSurface = false;
                    for (int searchDepth = 0; searchDepth <= brushSize; searchDepth++) {
                        Location checkLoc = surfaceLocation.clone().add(0, -searchDepth, 0);
                        Material currentBlock = checkLoc.getBlock().getType();
                        Material blockAbove = checkLoc.clone().add(0, 1, 0).getBlock().getType();

                        if ((currentBlock.isSolid() || currentBlock == Material.WATER || currentBlock == Material.LAVA)
                                && blockAbove == Material.AIR) {
                            surfaceLocation = checkLoc;
                            foundSurface = true;
                            break;
                        }
                    }

                    if (foundSurface) {
                        // Count available air blocks above surface
                        int availableAirBlocks = 0;
                        for (int y = 1; y <= brushProperties.BrushDepth; y++) {
                            Location checkLoc = surfaceLocation.clone().add(0, y, 0);
                            if (checkLoc.getBlock().getType() == Material.AIR) {
                                availableAirBlocks++;
                            } else {
                                break;
                            }
                        }

                        // Store states for all available air blocks up to maxHeight
                        for (int y = 1; y <= availableAirBlocks; y++) {
                            Location foliageLocation = surfaceLocation.clone().add(0, y, 0);
                            Block block = foliageLocation.getBlock();
                            states.push(block.getState());
                        }

                        // Store surface location and available height for later use
                        surfaceLocationMap.put(
                                surfaceLocation.getBlockX() + "," + surfaceLocation.getBlockZ(),
                                surfaceLocation);
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

        Map<Material, Integer> oldMaterials = new LinkedHashMap<>(brushProperties.Materials);
        int totalPercentage = 0;
        for (Material material : brushProperties.Materials.keySet()) {
            totalPercentage += brushProperties.Materials.get(material);
        }
        brushProperties.Materials.put(Material.AIR, 100 - totalPercentage);

        // Place the new material above surface blocks
        for (Map.Entry<String, Location> entry : surfaceLocationMap.entrySet()) {
            Location surfaceLocation = entry.getValue();
            if (brushProperties.Mask.isEmpty() || brushProperties.Mask.contains(surfaceLocation.getBlock().getType())) {

                Location location = surfaceLocation.clone().add(0, 1, 0);

                Material material = brushProperties.getMaterial(location, location);

                if (material == Material.SUGAR_CANE || material == Material.BAMBOO || material == Material.CACTUS) {
                    // Place first block where it's always AIR
                    Block block = location.getBlock();
                    block.setType(material, false);
                    location = location.clone().add(0, 1, 0);

                    // Check the rest of the blocks and place if there's AIR
                    for (int y = 2; y <= brushProperties.BrushDepth; y++) {
                        if (location.getBlock().getType() == Material.AIR) {
                            block = location.getBlock();
                            block.setType(material, false);
                            location = location.clone().add(0, 1, 0);
                        } else {
                            break;
                        }
                    }
                } else if (isTallMaterial(material) && brushProperties.BrushDepth > 1) {
                    Block block = location.getBlock();
                    block.setType(material, false);
                    Bisected data = (Bisected) block.getBlockData();
                    data.setHalf(Bisected.Half.BOTTOM);
                    block.setBlockData(data, false);

                    location = location.clone().add(0, 1, 0);

                    block = location.getBlock();
                    block.setType(material, false);
                    data = (Bisected) block.getBlockData();
                    data.setHalf(Bisected.Half.TOP);
                    block.setBlockData(data, false);
                } else {
                    Block block = location.getBlock();
                    block.setType(material, false);
                    if (brushProperties.RandomHeightFoliage && isTallMaterial(material)) {
                        Random random = new Random();
                        boolean height = random.nextBoolean();
                        Bisected data = (Bisected) block.getBlockData();
                        data.setHalf(height ? Bisected.Half.BOTTOM : Bisected.Half.TOP);
                        block.setBlockData(data, false);
                    }
                }
            }
        }

        brushProperties.Materials = oldMaterials;

        return true;
    }

    private static boolean isTallMaterial(Material material) {
        return switch (material) {
            case TALL_GRASS, LARGE_FERN, TALL_SEAGRASS, SUNFLOWER,
                    LILAC, ROSE_BUSH, PEONY, PITCHER_PLANT ->
                true;
            default -> false;
        };
    }
}
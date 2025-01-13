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

public class BrushSmooth extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = brushProperties.BrushSize;

        // Map to store blocks by Y level
        Map<Integer, List<Block>> blocksByLevel = new HashMap<>();

        // Collect blocks by Y level
        for (int x = -brushSize; x <= brushSize; x++) {
            for (int y = -brushSize; y <= brushSize; y++) {
                for (int z = -brushSize; z <= brushSize; z++) {
                    Location blockLoc = targetLocation.clone().add(x, y, z);
                    if (blockLoc.distance(targetLocation) <= brushSize) {
                        blocksByLevel.computeIfAbsent(blockLoc.getBlockY(), k -> new ArrayList<>())
                                .add(blockLoc.getBlock());
                    }
                }
            }
        }

        List<Block> allBlocks = blocksByLevel.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        states = allBlocks.stream()
                .map(Block::getState)
                .collect(Collectors.toCollection(Stack::new));

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

        for (Block block : allBlocks) {
            if (!block.getType().isSolid() && block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                block.setType(Material.AIR);
            }
        }

        // Process each Y level
        for (int y : blocksByLevel.keySet()) {
            for (int radius = brushSize; radius > 1; radius--) {
                List<Location> edgePositions = new ArrayList<>();

                // Get circle edge positions at this Y level
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        double distance = Math.sqrt(x * x + z * z);
                        if (Math.abs(distance - radius) < 0.5) {
                            Location edge = targetLocation.clone().add(x, y - targetLocation.getY(), z);
                            edgePositions.add(edge);
                        }
                    }
                }

                if (areEdgesSolid(edgePositions)) {
                    fillInterior(targetLocation, radius, y, blocksByLevel.get(y));
                }
            }
        }

        // First pass: Identify and remove protruding blocks
        for (Block block : allBlocks) {
            if (block.getType().isSolid()) {
                erodeBlock(block);
            }
        }

        // Second pass: Fill holes
        for (Block block : allBlocks) {
            if (!block.getType().isSolid()) {
                fillHole(block);
            }
        }

        // Final pass: Smooth remaining blocks
        for (Block block : allBlocks) {
            smoothBlock(block);
        }

        return true;
    }

    private static void erodeBlock(Block centerBlock) {
        int[][] directions = {
                { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, // Cardinal directions
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } // Diagonal directions
        };

        // Check if block is part of a flat surface
        Block blockBelow = centerBlock.getRelative(0, -1, 0);
        Block blockAbove = centerBlock.getRelative(0, 1, 0);

        // If there's solid block above AND below, this is part of a wall - don't erode
        if (blockBelow.getType().isSolid() && blockAbove.getType().isSolid()) {
            return;
        }

        // Count neighbors at same level
        int sameLayerNeighbors = 0;
        for (int[] dir : directions) {
            if (centerBlock.getRelative(dir[0], 0, dir[1]).getType().isSolid()) {
                sameLayerNeighbors++;
            }
        }

        // If block has many neighbors at same level, it's probably part of a flat
        // surface
        if (sameLayerNeighbors >= 5) {
            return;
        }

        // Check for protrusion
        boolean isProtrusion = false;

        // Case 1: Block sticking up from flat surface
        if (!blockAbove.getType().isSolid() && blockBelow.getType().isSolid()) {
            int solidNeighborsBelow = 0;
            for (int[] dir : directions) {
                if (blockBelow.getRelative(dir[0], 0, dir[1]).getType().isSolid()) {
                    solidNeighborsBelow++;
                }
            }
            // If the layer below is mostly solid but this block has few neighbors,
            // it's likely a protrusion
            isProtrusion = solidNeighborsBelow >= 6 && sameLayerNeighbors <= 3;
        }

        // Case 2: Sharp corner or edge that needs smoothing
        if (!isProtrusion) {
            int totalNeighbors = 0;
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0)
                            continue;
                        if (centerBlock.getRelative(x, y, z).getType().isSolid()) {
                            totalNeighbors++;
                        }
                    }
                }
            }

            // Sharp corner check: few total neighbors and few same-layer neighbors
            isProtrusion = totalNeighbors <= 12 && sameLayerNeighbors <= 4;
        }

        if (isProtrusion) {
            centerBlock.setType(Material.AIR);
        }
    }

    private static boolean areEdgesSolid(List<Location> edgePositions) {
        return edgePositions.stream()
                .map(Location::getBlock)
                .allMatch(block -> block.getType().isSolid());
    }

    private static boolean isExposedToAir(Block block) {
        return !block.getRelative(0, 1, 0).getType().isSolid();
    }

    private static void fillInterior(Location targetLocation, double brushSize, int y, List<Block> edgeBlocks) {
        int radius = (int) Math.ceil(brushSize);

        // Filter to only blocks exposed to air
        List<BlockData> exposedBlocks = edgeBlocks.stream()
                .filter(blockData -> isExposedToAir(blockData))
                .collect(Collectors.toList())
                .stream()
                .map(Block::getBlockData)
                .collect(Collectors.toList());

        // If no exposed blocks found, fall back to all edge blocks
        BlockData mostCommonBlock = exposedBlocks.isEmpty() ? findMostCommonBlock(edgeBlocks.stream()
                .map(Block::getBlockData)
                .collect(Collectors.toList()))
                : findMostCommonBlock(exposedBlocks);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance < brushSize) {
                    Location interior = targetLocation.clone().add(x, y - targetLocation.getY(), z);
                    // Only fill if exposed to air
                    if (isExposedToAir(interior.getBlock())) {
                        interior.getBlock().setType(mostCommonBlock.getMaterial());
                    }
                }
            }
        }
    }

    private static void fillHole(Block centerBlock) {
        int solidNeighbors = 0;
        Material commonSolid = null;
        Map<Material, Integer> solidMaterials = new HashMap<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    Block neighbor = centerBlock.getRelative(x, y, z);
                    if (neighbor.getType().isSolid()) {
                        solidNeighbors++;
                        solidMaterials.merge(neighbor.getType(), 1, Integer::sum);
                    }
                }
            }
        }

        if (!solidMaterials.isEmpty()) {
            commonSolid = solidMaterials.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        if (solidNeighbors >= 14 && commonSolid != null) {
            centerBlock.setType(commonSolid);
        }
    }

    private static void smoothBlock(Block centerBlock) {

        Map<Material, Integer> materialCounts = new HashMap<>();

        if (centerBlock.getType().isSolid()) {
            materialCounts.put(centerBlock.getType(), 1);
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    Block neighbor = centerBlock.getRelative(x, y, z);
                    if (neighbor.getType().isSolid()) {
                        materialCounts.merge(neighbor.getType(), 1, Integer::sum);

                        if ((Math.abs(x) + Math.abs(y) + Math.abs(z)) == 1) {
                            materialCounts.merge(neighbor.getType(), 1, Integer::sum);
                        }
                    }
                }
            }
        }

        if (!materialCounts.isEmpty()) {
            Material mostCommon = materialCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(centerBlock.getType());

            int mostCommonCount = materialCounts.get(mostCommon);

            if (mostCommonCount >= 18) {
                centerBlock.setType(mostCommon);
            }
        }
    }
}

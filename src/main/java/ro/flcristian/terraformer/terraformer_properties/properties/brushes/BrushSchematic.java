package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Random;
import java.util.Stack;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.utility.schematics.records.SchematicBlockPos;
import ro.flcristian.terraformer.utility.schematics.records.SchematicData;

public class BrushSchematic extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        // Validate inputs
        if (brushProperties == null || brushProperties.LoadedSchematicsData.size() == 0) {
            player.sendMessage(Component.text("No schematic loaded").color(NamedTextColor.RED));
            return false;
        }

        try {
            SchematicData schematic;
            if (brushProperties.LoadedSchematicsData.size() == 1) {
                schematic = brushProperties.LoadedSchematicsData.get(0);
            } else {
                Random random = new Random();
                int index = random.nextInt(0, brushProperties.LoadedSchematicsData.size());
                schematic = brushProperties.LoadedSchematicsData.get(index);
            }

            if (targetLocation.getBlock().getType().isSolid()) {
                targetLocation = targetLocation.clone().add(0, 1, 0);
            }
            Stack<BlockState> states = new Stack<>();

            int rotationAngle = 0;
            if (brushProperties.RandomSchematicRotation) {
                rotationAngle = (int) (Math.random() * 4) * 90;
            }

            int width = Math.max(1, schematic.width());
            int length = Math.max(1, schematic.length());

            // Get rotated dimensions
            int[] rotatedDims = getRotatedDimensions(width, length, rotationAngle);
            int rotatedWidth = rotatedDims[0];
            int rotatedLength = rotatedDims[1];

            // Calculate offsets using rotated dimensions
            int offsetX = -rotatedWidth / 2;
            int offsetY = 0;
            int offsetZ = -rotatedLength / 2;

            // First pass: collect states
            for (var blockPair : schematic.blocks()) {
                SchematicBlockPos originalPos = blockPair.getFirst();
                if (originalPos == null)
                    continue;

                // Rotate the position
                SchematicBlockPos rotatedPos = rotatePosition(originalPos, rotationAngle, width, length);

                Location blockLocation = targetLocation.clone().add(
                        rotatedPos.x() + offsetX,
                        rotatedPos.y() + offsetY,
                        rotatedPos.z() + offsetZ);

                Block block = blockLocation.getBlock();
                if (brushProperties.Mask.isEmpty() || brushProperties.Mask.contains(block.getType())) {
                    states.push(block.getState());
                }
            }

            // Update history
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

            // Second pass: place blocks
            for (var blockPair : schematic.blocks()) {
                SchematicBlockPos originalPos = blockPair.getFirst();
                String blockData = blockPair.getSecond();

                if (originalPos == null || blockData == null)
                    continue;

                // Rotate the position
                SchematicBlockPos rotatedPos = rotatePosition(originalPos, rotationAngle, width, length);

                Location blockLocation = targetLocation.clone().add(
                        rotatedPos.x() + offsetX,
                        rotatedPos.y() + offsetY,
                        rotatedPos.z() + offsetZ);

                Block block = blockLocation.getBlock();
                if (brushProperties.Mask.isEmpty() || brushProperties.Mask.contains(block.getType())) {
                    try {
                        int colonIndex = blockData.indexOf(":");
                        int bracketIndex = blockData.indexOf("[");

                        if (colonIndex != -1) {
                            String materialName;
                            if (bracketIndex != -1) {
                                // Case with metadata: "minecraft:oak_leaves[persistent=true]"
                                materialName = blockData.substring(colonIndex + 1, bracketIndex);
                            } else {
                                // Case without metadata: "minecraft:air"
                                materialName = blockData.substring(colonIndex + 1);
                            }
                            Material material = Material.valueOf(materialName.toUpperCase());
                            block.setType(material, false);
                            if (block.getBlockData() instanceof Leaves leaves) {
                                leaves.setPersistent(true);
                                block.setBlockData((BlockData) leaves, false);
                            }
                        }
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        } catch (Exception e) {
            player.sendMessage(Component.text("Error placing schematic: " + e.getMessage()).color(NamedTextColor.RED));
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static SchematicBlockPos rotatePosition(SchematicBlockPos pos, int degrees, int width, int length) {
        // Get center point of original schematic
        double centerX = width / 2.0;
        double centerZ = length / 2.0;

        // Translate to origin
        double relativeX = pos.x() - centerX;
        double relativeZ = pos.z() - centerZ;

        // Rotate
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        int newX = (int) Math.round(relativeX * cos - relativeZ * sin);
        int newZ = (int) Math.round(relativeX * sin + relativeZ * cos);

        // Translate back using rotated center
        int[] rotatedDims = getRotatedDimensions(width, length, degrees);
        double newCenterX = rotatedDims[0] / 2.0;
        double newCenterZ = rotatedDims[1] / 2.0;

        return new SchematicBlockPos(
                newX + (int) newCenterX,
                pos.y(),
                newZ + (int) newCenterZ);
    }

    private static int[] getRotatedDimensions(int width, int length, int rotationAngle) {
        // For 90 and 270 degrees, width and length are swapped
        if (rotationAngle == 90 || rotationAngle == 270) {
            return new int[] { length, width };
        }
        return new int[] { width, length };
    }
}
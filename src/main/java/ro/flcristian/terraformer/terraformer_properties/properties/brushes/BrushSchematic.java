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

import java.util.Stack;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.utility.schematics.records.SchematicBlockPos;

public class BrushSchematic extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        // Validate inputs
        if (brushProperties == null || brushProperties.LoadedSchematicData == null) {
            player.sendMessage(Component.text("No schematic loaded").color(NamedTextColor.RED));
            return false;
        }

        try {
            if (targetLocation.getBlock().getType().isSolid()) {
                targetLocation = targetLocation.clone().add(0, 1, 0);
            }
            Stack<BlockState> states = new Stack<>();

            // Validate schematic dimensions
            int width = Math.max(1, brushProperties.LoadedSchematicData.width());
            int length = Math.max(1, brushProperties.LoadedSchematicData.length());

            int offsetX = -width / 2;
            int offsetY = 0;
            int offsetZ = -length / 2;

            // First pass: collect states
            for (var blockPair : brushProperties.LoadedSchematicData.blocks()) {
                SchematicBlockPos blockPos = blockPair.getFirst();
                if (blockPos == null)
                    continue;

                Location blockLocation = targetLocation.clone().add(
                        blockPos.x() + offsetX,
                        blockPos.y() + offsetY,
                        blockPos.z() + offsetZ);

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
            for (var blockPair : brushProperties.LoadedSchematicData.blocks()) {
                SchematicBlockPos blockPos = blockPair.getFirst();
                String blockData = blockPair.getSecond();

                if (blockPos == null || blockData == null)
                    continue;

                Location blockLocation = targetLocation.clone().add(
                        blockPos.x() + offsetX,
                        blockPos.y() + offsetY,
                        blockPos.z() + offsetZ);

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
}
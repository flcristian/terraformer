package ro.flcristian.terraformer.utility.schematics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import ro.flcristian.terraformer.utility.schematics.records.Pair;
import ro.flcristian.terraformer.utility.schematics.records.SchematicBlockPos;
import ro.flcristian.terraformer.utility.schematics.records.SchematicData;

public class SchematicParserImpl implements SchematicParser {
    private SchematicParserImpl() {
    }

    private static final Supplier<SchematicParserImpl> instance = new Supplier<>() {
        private final SchematicParserImpl singletonInstance = new SchematicParserImpl();

        @Override
        public SchematicParserImpl get() {
            return singletonInstance;
        }
    };

    public static SchematicParserImpl getInstance() {
        return instance.get();
    }

    @Override
    public SchematicData readSchematicFile(Player player, File file) throws IOException {
        NBTDeserializer deserializer = new NBTDeserializer();
        NamedTag namedTag = deserializer.fromFile(file);
        CompoundTag root = (CompoundTag) namedTag.getTag();

        // Get the schematic tag - either the root itself or its child
        CompoundTag schematic = root.containsKey("Schematic")
                ? root.getCompoundTag("Schematic")
                : root;

        // Get dimensions
        int width = schematic.getShort("Width");
        int height = schematic.getShort("Height");
        int length = schematic.getShort("Length");

        // Get block data - handle both formats
        CompoundTag blockContainer = schematic.containsKey("Blocks")
                ? schematic.getCompoundTag("Blocks")
                : schematic;

        // Get block data array - handle both formats
        byte[] blockData = blockContainer.containsKey("Data")
                ? blockContainer.getByteArray("Data")
                : blockContainer.getByteArray("BlockData");

        // Get palette - handle both formats
        CompoundTag palette = blockContainer.containsKey("Palette")
                ? blockContainer.getCompoundTag("Palette")
                : schematic.getCompoundTag("Palette");

        Map<Integer, String> paletteMap = new HashMap<>();
        paletteMap.put(0, "minecraft:air");

        if (palette != null) {
            palette.forEach((key, value) -> {
                if (value instanceof IntTag) {
                    int id = ((IntTag) value).asInt();
                    paletteMap.put(id, key);
                }
            });
        }

        // Parse blocks
        List<Pair<SchematicBlockPos, String>> blocksList = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    int index = y * width * length + z * width + x;

                    if (index < blockData.length) {
                        int blockId = blockData[index] & 0xFF;
                        SchematicBlockPos pos = new SchematicBlockPos(x, y, z);
                        String blockType = paletteMap.get(blockId);
                        blocksList.add(new Pair<>(pos, blockType));
                    }
                }
            }
        }

        boolean invalidMaterials = false;
        for (var blockPair : blocksList) {
            String checkBlockData = blockPair.getSecond();

            try {
                int colonIndex = checkBlockData.indexOf(":");
                int bracketIndex = checkBlockData.indexOf("[");

                if (colonIndex != -1) {
                    String materialName;
                    if (bracketIndex != -1) {
                        // Case with metadata: "minecraft:oak_leaves[persistent=true]"
                        materialName = checkBlockData.substring(colonIndex + 1, bracketIndex);
                    } else {
                        // Case without metadata: "minecraft:air"
                        materialName = checkBlockData.substring(colonIndex + 1);
                    }
                    Material material = Material.valueOf(materialName.toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                invalidMaterials = true;
            }
        }
        if (invalidMaterials) {
            player.sendMessage(
                    Component.text(
                            "Invalid blocks in schematic, probably from older versions or mods. The schematic will still work, but might not look the same as it should have.")
                            .color(NamedTextColor.RED));
        }

        return new SchematicData(width, height, length, blocksList);
    }
}
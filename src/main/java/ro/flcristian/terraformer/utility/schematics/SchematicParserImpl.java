package ro.flcristian.terraformer.utility.schematics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
    public SchematicData readSchematicFile(File file) throws IOException {
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

        return new SchematicData(width, height, length, blocksList);
    }

    public static void main(String[] args) {
        SchematicParserImpl parser = new SchematicParserImpl();

        try {
            File testFile = new File("D:\\Terraformer Development\\terraformer\\src\\test\\resources\\tree.schem");
            SchematicData data = parser.readSchematicFile(testFile);

            // Print parsed data to verify
            System.out.println("Schematic Dimensions: " + data.width() + "x" + data.height() + "x" + data.length());
            System.out.println("Parsed Blocks: " + data.blocks().size());
            data.blocks()
                    .forEach(block -> System.out.println("Block at " + block.getFirst() + ": " + block.getSecond()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
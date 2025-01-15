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

public class SpongeV1Parser implements SchematicParser {
    private SpongeV1Parser() {
    }

    private static final Supplier<SpongeV1Parser> instance = new Supplier<>() {
        private final SpongeV1Parser singletonInstance = new SpongeV1Parser();

        @Override
        public SpongeV1Parser get() {
            return singletonInstance;
        }
    };

    public static SpongeV1Parser getInstance() {
        return instance.get();
    }

    @Override
    public SchematicData readSchematicFile(File file) throws IOException {
        NBTDeserializer deserializer = new NBTDeserializer();
        NamedTag namedTag = deserializer.fromFile(file);
        CompoundTag root = (CompoundTag) namedTag.getTag();

        // Navigate to Schematic tag
        CompoundTag schematic = root.getCompoundTag("Schematic");

        // Get dimensions
        int width = schematic.getShort("Width");
        int height = schematic.getShort("Height");
        int length = schematic.getShort("Length");

        // Navigate to Blocks tag
        CompoundTag blocks = schematic.getCompoundTag("Blocks");

        // Get block data array
        byte[] blockData = blocks.getByteArray("Data");

        // Get palette
        CompoundTag palette = blocks.getCompoundTag("Palette");
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
        SpongeV1Parser parser = new SpongeV1Parser();

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
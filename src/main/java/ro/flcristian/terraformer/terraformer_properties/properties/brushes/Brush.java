package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class Brush {
    protected static BlockData findMostCommonBlock(List<BlockData> blocks) {
        if (blocks.isEmpty())
            return null;

        Map<Material, Integer> counts = new HashMap<>();
        for (BlockData block : blocks) {
            Material mat = block.getMaterial();
            if (mat.isSolid()) {
                counts.put(mat, counts.getOrDefault(mat, 0) + 1);
            }
        }

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().createBlockData())
                .orElse(null);
    }
}

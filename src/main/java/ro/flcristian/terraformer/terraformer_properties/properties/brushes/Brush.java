package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

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

    protected static boolean checkMaterialPercentages(BrushProperties brushProperties, Player player) {
        if (brushProperties.Mode != MaterialMode.GRADIENT
                && IntStream.of(brushProperties.Materials.values().stream().mapToInt(i -> i).toArray()).sum() != 100) {
            player.sendMessage(
                    Component.text("Material percentages must add up to 100%").color(NamedTextColor.RED));
            return true;
        }
        return false;
    }
}

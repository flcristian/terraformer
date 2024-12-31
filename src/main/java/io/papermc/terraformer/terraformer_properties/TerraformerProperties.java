package io.papermc.terraformer.terraformer_properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.block_history.BlockHistory;
import io.papermc.terraformer.terraformer_properties.properties.BrushType;
import io.papermc.terraformer.terraformer_properties.properties.Palette;

public class TerraformerProperties {
    public boolean IsTerraformer;
    public BrushType Brush;
    public int BrushSize;
    public BlockHistory History;
    public Map<Material, Integer> Materials;
    public Palette Palette;

    public TerraformerProperties() {
        Brush = BrushType.BALL;
        BrushSize = 4;
        History = new BlockHistory();
        Materials = new HashMap<>();
        Materials.put(Material.STONE, 100);
        Palette = new Palette();
        IsTerraformer = true;
    }

    public TerraformerProperties(BrushType brushType, int brushSize, BlockHistory history,
            Map<Material, Integer> materials, Palette palette, boolean isTerraformer) {
        Brush = brushType;
        BrushSize = brushSize;
        History = history;
        Materials = materials;
        Palette = palette;
        IsTerraformer = isTerraformer;
    }

    public Material getRandomMaterial() {
        int random = new Random().nextInt(100);
        int currentSum = 0;

        for (Map.Entry<Material, Integer> entry : Materials.entrySet()) {
            currentSum += entry.getValue();
            if (random < currentSum) {
                return entry.getKey();
            }
        }

        return Materials.entrySet().iterator().next().getKey();
    }
}

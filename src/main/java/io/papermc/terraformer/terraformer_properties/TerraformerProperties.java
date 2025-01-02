package io.papermc.terraformer.terraformer_properties;

import java.util.Map;

import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.block_history.BrushBlockHistory;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;
import io.papermc.terraformer.terraformer_properties.properties.MaterialsMode;
import io.papermc.terraformer.terraformer_properties.properties.Palette;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;

public class TerraformerProperties {
    public boolean IsTerraformer;
    public BrushProperties Brush;
    public BrushBlockHistory History;
    public Palette Palette;

    public TerraformerProperties() {
        IsTerraformer = true;
        Brush = new BrushProperties();
        History = new BrushBlockHistory();
        Palette = new Palette();
    }

    public TerraformerProperties(boolean isTerraformer, BrushType brushType, int brushSize, int brushDepth,
            Map<Material, Integer> materials, MaterialsMode materialsMode, BrushBlockHistory history, Palette palette) {
        IsTerraformer = isTerraformer;
        Brush = new BrushProperties(brushType, brushSize, brushDepth, materials, materialsMode);
        History = history;
        Palette = palette;
    }
}

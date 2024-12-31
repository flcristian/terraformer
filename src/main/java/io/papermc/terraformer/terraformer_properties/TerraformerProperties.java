package io.papermc.terraformer.terraformer_properties;

import io.papermc.terraformer.terraformer_properties.block_history.BlockHistory;
import io.papermc.terraformer.terraformer_properties.properties.BrushType;
import io.papermc.terraformer.terraformer_properties.properties.Palette;

public class TerraformerProperties {
    public boolean IsTerraformer;
    public BrushType Brush;
    public int BrushSize;
    public BlockHistory History;
    public Palette Palette;

    public TerraformerProperties() {
        Brush = BrushType.BALL;
        BrushSize = 4;
        History = new BlockHistory();
        Palette = new Palette();
        IsTerraformer = true;
    }

    public TerraformerProperties(BrushType brushType, int brushSize, BlockHistory history, Palette palette,
            boolean isTerraformer) {
        Brush = brushType;
        BrushSize = brushSize;
        History = history;
        Palette = palette;
        IsTerraformer = isTerraformer;
    }
}

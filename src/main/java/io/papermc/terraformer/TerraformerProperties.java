package io.papermc.terraformer;

public class TerraformerProperties {
    public BrushType Brush;
    public int BrushSize;
    public BlockHistory History;

    public TerraformerProperties() {
        Brush = BrushType.BALL;
        BrushSize = 4;
        History = new BlockHistory();
    }

    public TerraformerProperties(BrushType brushType, int brushSize) {
        Brush = brushType;
        BrushSize = brushSize;
        History = new BlockHistory();
    }
}

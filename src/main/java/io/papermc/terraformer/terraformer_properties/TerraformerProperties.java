package io.papermc.terraformer.terraformer_properties;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.block_history.BrushAction;
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

    public TerraformerProperties(boolean isTerraformer, BrushProperties brush, BrushBlockHistory history,
            Palette palette) {
        IsTerraformer = isTerraformer;
        Brush = brush;
        History = history;
        Palette = palette;
    }

    public void applyRedo(Terraformer plugin, Player player, BrushAction brushAction) {
        BrushType.applyBrush(plugin, player, brushAction.brushProperties(), brushAction.targetLocation(), true);
    }
}

package ro.flcristian.terraformer.terraformer_properties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.block_history.BrushAction;
import ro.flcristian.terraformer.terraformer_properties.block_history.BrushBlockHistory;
import ro.flcristian.terraformer.terraformer_properties.material_history.MaterialHistory;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.Palette;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

public class TerraformerProperties {
    public boolean IsTerraformer;
    public BrushProperties Brush;
    public Stack<BrushProperties> BrushHistory;
    public Stack<MaterialHistory> MaterialHistory;
    public BrushBlockHistory History;
    public Palette Palette;

    public TerraformerProperties() {
        IsTerraformer = true;
        Brush = new BrushProperties();
        BrushHistory = new Stack<BrushProperties>();
        MaterialHistory = new Stack<MaterialHistory>();
        History = new BrushBlockHistory();
        Palette = new Palette();
    }

    public TerraformerProperties(boolean isTerraformer, BrushType brushType, int brushSize, int brushDepth,
            Map<Material, Integer> materials, MaterialMode materialsMode, Stack<BrushProperties> brushHistory,
            Stack<MaterialHistory> materialHistory, BrushBlockHistory history, Palette palette) {
        IsTerraformer = isTerraformer;
        Brush = new BrushProperties(brushType, brushSize, brushDepth, materials, materialsMode);
        BrushHistory = brushHistory;
        MaterialHistory = materialHistory;
        History = history;
        Palette = palette;
    }

    public TerraformerProperties(boolean isTerraformer, BrushProperties brush, Stack<BrushProperties> brushHistory,
            Stack<MaterialHistory> materialHistory, BrushBlockHistory history, Palette palette) {
        IsTerraformer = isTerraformer;
        Brush = brush;
        BrushHistory = brushHistory;
        MaterialHistory = materialHistory;
        History = history;
        Palette = palette;
    }

    public void applyRedo(Terraformer plugin, Player player, BrushAction brushAction) {
        BrushType.applyBrush(plugin, player, brushAction.brushProperties(), brushAction.targetLocation(), true);
    }

    public void addBrushHistory(BrushProperties brushProperties) {
        int MAX_HISTORY_SIZE = 9;

        // Check for duplicate brush properties
        for (int i = 0; i < BrushHistory.size(); i++) {
            if (BrushHistory.get(i).equals(brushProperties)) {
                BrushHistory.remove(i);
                break;
            }
        }

        // Check for duplicate materials separately
        for (int i = 0; i < MaterialHistory.size(); i++) {
            if (MaterialHistory.get(i).equals(new MaterialHistory(brushProperties.Materials, brushProperties.Mode))) {
                MaterialHistory.remove(i);
                break;
            }
        }

        // Handle size limits
        if (BrushHistory.size() >= MAX_HISTORY_SIZE) {
            BrushHistory.remove(0);
        }
        if (MaterialHistory.size() >= MAX_HISTORY_SIZE) {
            MaterialHistory.remove(0);
        }

        BrushHistory.push(brushProperties.clone());
        MaterialHistory.push(new MaterialHistory(brushProperties.Materials, brushProperties.Mode));
    }

    public void applyBrushHistory(BrushProperties brushProperties) {
        BrushProperties toApply = brushProperties.clone();
        Brush = new BrushProperties(toApply.Type, toApply.BrushSize, toApply.BrushDepth, Brush.Materials,
                Brush.Mode);
    }

    public void applyMaterialHistory(MaterialHistory materialHistory) {
        Brush.Materials = new LinkedHashMap<>(materialHistory.Materials);
        Brush.Mode = materialHistory.Mode;
    }
}

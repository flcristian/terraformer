package io.papermc.terraformer.terraformer_properties.properties;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;

public class BrushProperties {
    public BrushType BrushType;
    public int BrushSize;
    public int BrushDepth;
    public Map<Material, Integer> Materials;
    public MaterialsMode Mode;

    public BrushProperties() {
        BrushSize = 4;
        BrushDepth = 1;
        Materials = new HashMap<>();
        Materials.put(Material.STONE, 100);
        Mode = MaterialsMode.RANDOM;
    }

    public BrushProperties(BrushType brushType, int brushSize, int brushDepth, Map<Material, Integer> materials,
            MaterialsMode materialsMode) {
        BrushType = brushType;
        BrushSize = brushSize;
        BrushDepth = brushDepth;
        Materials = materials;
        Mode = materialsMode;
    }

    public void applyBrush(Terraformer plugin, Player player, Location targetLocation, boolean isRedo) {
        BrushType.applyBrush(plugin, player, this, targetLocation, isRedo);
    }

    public Material getMaterial(Location location, Location targetLocation) {
        return Mode.getMaterial(location, targetLocation, this);
    }
}

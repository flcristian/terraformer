package io.papermc.terraformer.terraformer_properties.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;

public class BrushProperties implements Cloneable {
    public BrushType Type;
    public int BrushSize;
    public int BrushDepth;
    public Map<Material, Integer> Materials;
    public MaterialMode Mode;

    public BrushProperties() {
        Type = BrushType.BALL;
        BrushSize = 4;
        BrushDepth = 1;
        Materials = new LinkedHashMap<>();
        Materials.put(Material.STONE, 100);
        Mode = MaterialMode.RANDOM;
    }

    public BrushProperties(BrushType brushType, int brushSize, int brushDepth, Map<Material, Integer> materials,
            MaterialMode materialsMode) {
        Type = brushType;
        BrushSize = brushSize;
        BrushDepth = brushDepth;
        Materials = new LinkedHashMap<>(materials);
        Mode = materialsMode;
    }

    public void applyBrush(Terraformer plugin, Player player, Location targetLocation) {
        BrushType.applyBrush(plugin, player, this, targetLocation, false);
    }

    public Material getMaterial(Location location, Location targetLocation) {
        return Mode.getMaterial(location, targetLocation, this);
    }

    @Override
    public BrushProperties clone() {
        return new BrushProperties(Type, BrushSize, BrushDepth, new LinkedHashMap<>(Materials), Mode);
    }
}

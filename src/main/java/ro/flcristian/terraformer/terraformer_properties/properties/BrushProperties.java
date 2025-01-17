package ro.flcristian.terraformer.terraformer_properties.properties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;
import ro.flcristian.terraformer.utility.schematics.records.SchematicData;

public class BrushProperties implements Cloneable {
    public BrushType Type;
    public int BrushSize;
    public int BrushDepth;
    public Map<Material, Integer> Materials;
    public MaterialMode Mode;
    public List<Material> Mask;
    public boolean RandomHeightFoliage;
    public boolean RandomSchematicRotation;
    public List<SchematicData> LoadedSchematicsData;
    public boolean BlockUpdates;

    public BrushProperties() {
        Type = BrushType.BALL;
        BrushSize = 4;
        BrushDepth = 1;
        Materials = new LinkedHashMap<>();
        Materials.put(Material.STONE, 100);
        Mode = MaterialMode.RANDOM;
        Mask = new ArrayList<>();
        RandomHeightFoliage = false;
        RandomSchematicRotation = false;
        LoadedSchematicsData = new ArrayList<>();
        BlockUpdates = true;
    }

    public BrushProperties(BrushType brushType, int brushSize, int brushDepth, Map<Material, Integer> materials,
            MaterialMode materialsMode, List<Material> mask, boolean randomHeightFoliage,
            boolean randomSchematicRotation, List<SchematicData> loadedSchematicsData, boolean blockUpdates) {
        Type = brushType;
        BrushSize = brushSize;
        BrushDepth = brushDepth;
        Materials = new LinkedHashMap<>(materials);
        Mode = materialsMode;
        Mask = mask;
        RandomHeightFoliage = randomHeightFoliage;
        RandomSchematicRotation = randomSchematicRotation;
        LoadedSchematicsData = loadedSchematicsData;
        BlockUpdates = blockUpdates;
    }

    public void applyBrush(Terraformer plugin, Player player, Location targetLocation) {
        BrushType.applyBrush(plugin, player, this, targetLocation, false);
    }

    public Material getMaterial(Location location, Location targetLocation) {
        return Mode.getMaterial(location, targetLocation, this);
    }

    @Override
    public BrushProperties clone() {
        return new BrushProperties(Type, BrushSize, BrushDepth, new LinkedHashMap<>(Materials), Mode,
                new ArrayList<>(Mask), RandomHeightFoliage, RandomSchematicRotation,
                new ArrayList<>(LoadedSchematicsData), BlockUpdates);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BrushProperties))
            return false;

        BrushProperties other = (BrushProperties) obj;

        return Type == other.Type && BrushSize == other.BrushSize && BrushDepth == other.BrushDepth;
    }

    public void setMode(MaterialMode mode) {
        Mode = mode;

        Materials = new LinkedHashMap<>();
        switch (Mode) {
            case RANDOM:
                if (Type == BrushType.FOLIAGE) {
                    Materials.put(Material.SHORT_GRASS, 50);
                } else {
                    Materials.put(Material.STONE, 100);
                }
                break;
            case LAYER:
                Materials.put(Material.STONE, 100);
                break;
            case GRADIENT:
                Materials.put(Material.WHITE_CONCRETE, 0);
                Materials.put(Material.BLACK_CONCRETE, 100);
                break;
        }
    }
}

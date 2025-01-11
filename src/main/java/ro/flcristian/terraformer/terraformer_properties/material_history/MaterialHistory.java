package ro.flcristian.terraformer.terraformer_properties.material_history;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;

import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

public class MaterialHistory implements Cloneable {
    public final Map<Material, Integer> Materials;
    public final MaterialMode Mode;

    public MaterialHistory(Map<Material, Integer> materials, MaterialMode mode) {
        Materials = new LinkedHashMap<>(materials);
        Mode = mode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof MaterialHistory))
            return false;

        MaterialHistory other = (MaterialHistory) obj;

        return Materials.equals(other.Materials) && Mode.equals(other.Mode);
    }
}

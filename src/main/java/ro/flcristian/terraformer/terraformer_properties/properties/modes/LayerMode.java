package ro.flcristian.terraformer.terraformer_properties.properties.modes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;

import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;

public class LayerMode implements Mode {
    private LayerMode() {
    }

    private static final Supplier<LayerMode> instance = new Supplier<>() {
        private final LayerMode singletonInstance = new LayerMode();

        @Override
        public LayerMode get() {
            return singletonInstance;
        }
    };

    public static LayerMode getInstance() {
        return instance.get();
    }

    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        List<Material> materials = new ArrayList<>(properties.Materials.keySet());

        if (materials.isEmpty()) {
            return Material.STONE;
        }

        double layerHeight;
        int size;
        if (properties.Type == BrushType.PAINT_TOP || properties.Type == BrushType.PAINT_WALL
                || properties.Type == BrushType.PAINT_BOTTOM) {
            size = properties.BrushDepth % 2 == 1 ? properties.BrushDepth / 2 : properties.BrushDepth / 2 + 1;
            layerHeight = (double) properties.BrushDepth / materials.size();
        } else {
            size = properties.BrushSize;
            layerHeight = (double) (properties.BrushSize * 2 - 1) / materials.size();
        }

        int relativeY;
        if (properties.Type == BrushType.PAINT_TOP) {
            relativeY = targetLocation.getBlockY() - location.getBlockY();
        } else if (properties.Type == BrushType.PAINT_BOTTOM) {
            relativeY = location.getBlockY() - targetLocation.getBlockY();
        } else if (properties.Type == BrushType.PAINT_WALL) {
            if (properties.BrushDepth % 2 == 1) {
                relativeY = targetLocation.getBlockY() - (location.getBlockY() - size);
            } else {
                relativeY = targetLocation.getBlockY() - (location.getBlockY() - size) - 1;
            }
        } else {
            relativeY = targetLocation.getBlockY() - (location.getBlockY() - size) - 1;
        }

        int materialIndex = (int) (relativeY / layerHeight);
        materialIndex = Math.min(materialIndex, materials.size() - 1);
        materialIndex = Math.max(0, materialIndex);

        return materials.get(materialIndex);
    }
}
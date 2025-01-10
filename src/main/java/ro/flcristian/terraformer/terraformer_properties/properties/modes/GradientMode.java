package ro.flcristian.terraformer.terraformer_properties.properties.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;

import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.utility.BlockColorMap;
import ro.flcristian.terraformer.utility.Color;

public class GradientMode implements Mode {
    private final Map<GradientCacheKey, List<Material>> gradientCache;

    private GradientMode() {
        gradientCache = new HashMap<>();
    }

    private static class GradientCacheKey {
        private final Map<Material, Integer> materials;
        private final int length;

        public GradientCacheKey(Map<Material, Integer> materials, int length) {
            this.materials = materials;
            this.length = length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            GradientCacheKey that = (GradientCacheKey) o;
            return length == that.length && materials.equals(that.materials);
        }

        @Override
        public int hashCode() {
            return 31 * materials.hashCode() + length;
        }
    }

    private static final Supplier<GradientMode> instance = new Supplier<>() {
        private final GradientMode singletonInstance = new GradientMode();

        @Override
        public GradientMode get() {
            return singletonInstance;
        }
    };

    public static GradientMode getInstance() {
        return instance.get();
    }

    public List<Material> generateGradient(int gradientLengthInBlocks, Map<Material, Integer> gradientBlockPositions) {
        List<Material> gradient = new ArrayList<>();

        TreeMap<Integer, Color> colorPositions = new TreeMap<>();

        Color defaultStartColor = BlockColorMap.getInstance().BlockColors.get(Material.WHITE_CONCRETE);
        Color defaultEndColor = BlockColorMap.getInstance().BlockColors.get(Material.BLACK_CONCRETE);

        for (Map.Entry<Material, Integer> entry : gradientBlockPositions.entrySet()) {
            Color blockColor = BlockColorMap.getInstance().BlockColors.get(entry.getKey());
            if (blockColor != null && entry.getValue() >= 0 && entry.getValue() <= 100) {
                colorPositions.put(entry.getValue(), blockColor);
            }
        }

        if (!colorPositions.containsKey(0)) {
            colorPositions.put(0, defaultStartColor);
        }
        if (!colorPositions.containsKey(100)) {
            colorPositions.put(100, defaultEndColor);
        }

        for (int i = 0; i < gradientLengthInBlocks; i++) {
            float position = (float) i * 100 / (gradientLengthInBlocks - 1);

            Map.Entry<Integer, Color> beforeEntry = colorPositions.floorEntry((int) position);
            Map.Entry<Integer, Color> afterEntry = colorPositions.ceilingEntry((int) position);

            Color interpolatedColor;
            if (beforeEntry.getKey().equals(afterEntry.getKey())) {
                interpolatedColor = beforeEntry.getValue();
            } else {
                float ratio = (position - beforeEntry.getKey()) /
                        (afterEntry.getKey() - beforeEntry.getKey());

                interpolatedColor = interpolateColor(beforeEntry.getValue(), afterEntry.getValue(), ratio);
            }

            Material closestBlock = BlockColorMap.getInstance().findClosestColorBlock(interpolatedColor);
            gradient.add(closestBlock);
        }

        return gradient;
    }

    private Color interpolateColor(Color c1, Color c2, float ratio) {
        int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(red, green, blue);
    }

    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        if (properties.Materials.isEmpty()) {
            return Material.STONE;
        }

        // Check Cache
        int size, totalHeight;
        if (properties.Type == BrushType.PAINT_TOP || properties.Type == BrushType.PAINT_WALL
                || properties.Type == BrushType.PAINT_BOTTOM) {
            size = properties.BrushDepth % 2 == 1 ? properties.BrushDepth / 2 : properties.BrushDepth / 2 + 1;
            totalHeight = properties.BrushDepth;
        } else {
            size = properties.BrushSize;
            totalHeight = properties.BrushSize * 2 - 1;
        }

        GradientCacheKey cacheKey = new GradientCacheKey(properties.Materials, totalHeight);
        List<Material> gradient;
        if (gradientCache.containsKey(cacheKey)) {
            gradient = gradientCache.get(cacheKey);
        } else {
            gradient = generateGradient(totalHeight, properties.Materials);
            gradientCache.put(cacheKey, gradient);
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

        if (relativeY < 0 || relativeY >= gradient.size()) {
            return Material.STONE;
        }

        return gradient.get(relativeY);
    }
}
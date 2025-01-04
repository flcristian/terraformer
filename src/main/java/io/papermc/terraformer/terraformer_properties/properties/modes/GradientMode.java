package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;
import io.papermc.terraformer.utility.Color;

public class GradientMode implements Mode {
    private final Map<Material, Color> blockColors;
    private final Map<GradientCacheKey, List<Material>> gradientCache;

    public GradientMode() {
        blockColors = new HashMap<>();
        gradientCache = new HashMap<>();

        blockColors.put(Material.WHITE_CONCRETE, new Color(207, 213, 214));
        blockColors.put(Material.LIGHT_GRAY_CONCRETE, new Color(157, 157, 151));
        blockColors.put(Material.GRAY_CONCRETE, new Color(54, 57, 61));
        blockColors.put(Material.BLACK_CONCRETE, new Color(8, 10, 15));
        blockColors.put(Material.RED_CONCRETE, new Color(142, 33, 33));
        blockColors.put(Material.ORANGE_CONCRETE, new Color(224, 97, 1));
        blockColors.put(Material.YELLOW_CONCRETE, new Color(241, 175, 21));
        blockColors.put(Material.LIME_CONCRETE, new Color(94, 169, 24));
        blockColors.put(Material.GREEN_CONCRETE, new Color(73, 91, 36));
        blockColors.put(Material.CYAN_CONCRETE, new Color(21, 119, 136));
        blockColors.put(Material.LIGHT_BLUE_CONCRETE, new Color(36, 137, 199));
        blockColors.put(Material.BLUE_CONCRETE, new Color(45, 47, 143));
        blockColors.put(Material.PURPLE_CONCRETE, new Color(100, 32, 156));
        blockColors.put(Material.MAGENTA_CONCRETE, new Color(169, 48, 159));
        blockColors.put(Material.PINK_CONCRETE, new Color(213, 101, 142));
        blockColors.put(Material.BROWN_CONCRETE, new Color(96, 59, 31));
        blockColors.put(Material.WHITE_WOOL, new Color(234, 236, 237));
        blockColors.put(Material.LIGHT_GRAY_WOOL, new Color(141, 142, 135));
        blockColors.put(Material.GRAY_WOOL, new Color(62, 68, 71));
        blockColors.put(Material.BLACK_WOOL, new Color(26, 22, 22));
        blockColors.put(Material.RED_WOOL, new Color(160, 39, 34));
        blockColors.put(Material.ORANGE_WOOL, new Color(242, 127, 24));
        blockColors.put(Material.YELLOW_WOOL, new Color(249, 207, 35));
        blockColors.put(Material.LIME_WOOL, new Color(114, 185, 26));
        blockColors.put(Material.GREEN_WOOL, new Color(85, 110, 28));
        blockColors.put(Material.CYAN_WOOL, new Color(21, 138, 145));
        blockColors.put(Material.LIGHT_BLUE_WOOL, new Color(58, 175, 217));
        blockColors.put(Material.BLUE_WOOL, new Color(45, 47, 143));
        blockColors.put(Material.PURPLE_WOOL, new Color(116, 42, 163));
        blockColors.put(Material.MAGENTA_WOOL, new Color(191, 74, 196));
        blockColors.put(Material.PINK_WOOL, new Color(237, 141, 172));
        blockColors.put(Material.BROWN_WOOL, new Color(122, 72, 38));
        blockColors.put(Material.WHITE_TERRACOTTA, new Color(210, 178, 161));
        blockColors.put(Material.LIGHT_GRAY_TERRACOTTA, new Color(135, 106, 97));
        blockColors.put(Material.GRAY_TERRACOTTA, new Color(88, 65, 66));
        blockColors.put(Material.BLACK_TERRACOTTA, new Color(37, 23, 16));
        blockColors.put(Material.RED_TERRACOTTA, new Color(144, 61, 46));
        blockColors.put(Material.ORANGE_TERRACOTTA, new Color(186, 96, 28));
        blockColors.put(Material.YELLOW_TERRACOTTA, new Color(227, 167, 41));
        blockColors.put(Material.LIME_TERRACOTTA, new Color(104, 118, 53));
        blockColors.put(Material.GREEN_TERRACOTTA, new Color(76, 83, 42));
        blockColors.put(Material.CYAN_TERRACOTTA, new Color(76, 91, 91));
        blockColors.put(Material.LIGHT_BLUE_TERRACOTTA, new Color(113, 108, 137));
        blockColors.put(Material.BLUE_TERRACOTTA, new Color(74, 60, 91));
        blockColors.put(Material.PURPLE_TERRACOTTA, new Color(119, 72, 89));
        blockColors.put(Material.MAGENTA_TERRACOTTA, new Color(161, 78, 78));
        blockColors.put(Material.PINK_TERRACOTTA, new Color(213, 101, 142));
        blockColors.put(Material.BROWN_TERRACOTTA, new Color(133, 78, 39));
        blockColors.put(Material.GRANITE, new Color(149, 108, 76));
        blockColors.put(Material.DIORITE, new Color(189, 189, 189));
        blockColors.put(Material.ANDESITE, new Color(136, 136, 136));
        blockColors.put(Material.BRICKS, new Color(150, 94, 84));
        blockColors.put(Material.NETHER_BRICKS, new Color(47, 22, 27));
        blockColors.put(Material.END_STONE, new Color(222, 222, 157));
        blockColors.put(Material.QUARTZ_BLOCK, new Color(236, 233, 228));
        blockColors.put(Material.PACKED_ICE, new Color(155, 185, 230));
        blockColors.put(Material.BLUE_ICE, new Color(100, 150, 220));
        blockColors.put(Material.SNOW_BLOCK, new Color(240, 240, 240));
        blockColors.put(Material.HAY_BLOCK, new Color(181, 140, 18));
        blockColors.put(Material.TERRACOTTA, new Color(149, 94, 67));
        blockColors.put(Material.GRAY_GLAZED_TERRACOTTA, new Color(96, 96, 96));
        blockColors.put(Material.BLACK_GLAZED_TERRACOTTA, new Color(32, 32, 32));
        blockColors.put(Material.RED_GLAZED_TERRACOTTA, new Color(183, 58, 45));
        blockColors.put(Material.YELLOW_GLAZED_TERRACOTTA, new Color(254, 216, 61));
        blockColors.put(Material.LIME_GLAZED_TERRACOTTA, new Color(127, 204, 25));
        blockColors.put(Material.GREEN_GLAZED_TERRACOTTA, new Color(90, 151, 54));
        blockColors.put(Material.CYAN_GLAZED_TERRACOTTA, new Color(21, 137, 145));
        blockColors.put(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, new Color(58, 179, 218));
        blockColors.put(Material.BLUE_GLAZED_TERRACOTTA, new Color(44, 46, 143));
        blockColors.put(Material.PURPLE_GLAZED_TERRACOTTA, new Color(136, 54, 159));
        blockColors.put(Material.MAGENTA_GLAZED_TERRACOTTA, new Color(199, 78, 189));
        blockColors.put(Material.PINK_GLAZED_TERRACOTTA, new Color(237, 141, 172));
        blockColors.put(Material.COAL_ORE, new Color(115, 115, 115));
        blockColors.put(Material.IRON_ORE, new Color(216, 160, 105));
        blockColors.put(Material.COPPER_ORE, new Color(170, 113, 70));
        blockColors.put(Material.GOLD_ORE, new Color(249, 238, 85));
        blockColors.put(Material.REDSTONE_ORE, new Color(140, 28, 27));
        blockColors.put(Material.LAPIS_ORE, new Color(38, 99, 153));
        blockColors.put(Material.DIAMOND_ORE, new Color(129, 241, 226));
        blockColors.put(Material.EMERALD_ORE, new Color(40, 217, 68));
        blockColors.put(Material.DEEPSLATE_COAL_ORE, new Color(65, 65, 65));
        blockColors.put(Material.DEEPSLATE_IRON_ORE, new Color(111, 83, 62));
        blockColors.put(Material.DEEPSLATE_COPPER_ORE, new Color(101, 67, 44));
        blockColors.put(Material.DEEPSLATE_GOLD_ORE, new Color(131, 112, 42));
        blockColors.put(Material.DEEPSLATE_REDSTONE_ORE, new Color(78, 20, 20));
        blockColors.put(Material.DEEPSLATE_LAPIS_ORE, new Color(25, 63, 97));
        blockColors.put(Material.DEEPSLATE_DIAMOND_ORE, new Color(72, 134, 125));
        blockColors.put(Material.DEEPSLATE_EMERALD_ORE, new Color(25, 132, 41));
        blockColors.put(Material.NETHERRACK, new Color(112, 2, 2));
        blockColors.put(Material.BASALT, new Color(84, 84, 84));
        blockColors.put(Material.POLISHED_BASALT, new Color(104, 104, 104));
        blockColors.put(Material.BLACKSTONE, new Color(25, 25, 25));
        blockColors.put(Material.GILDED_BLACKSTONE, new Color(56, 45, 36));
        blockColors.put(Material.NETHER_QUARTZ_ORE, new Color(159, 103, 100));
        blockColors.put(Material.NETHER_GOLD_ORE, new Color(197, 157, 80));
        blockColors.put(Material.CRIMSON_NYLIUM, new Color(189, 56, 56));
        blockColors.put(Material.WARPED_NYLIUM, new Color(22, 122, 114));
        blockColors.put(Material.NETHER_BRICK, new Color(54, 29, 35));
        blockColors.put(Material.CHISELED_NETHER_BRICKS, new Color(59, 31, 34));
        blockColors.put(Material.CRACKED_NETHER_BRICKS, new Color(57, 31, 34));
        blockColors.put(Material.END_STONE_BRICKS, new Color(219, 220, 158));
        blockColors.put(Material.PURPUR_BLOCK, new Color(170, 125, 196));
        blockColors.put(Material.PURPUR_PILLAR, new Color(175, 130, 200));
        blockColors.put(Material.OBSIDIAN, new Color(15, 11, 26));
        blockColors.put(Material.ANCIENT_DEBRIS, new Color(77, 54, 47));
        blockColors.put(Material.LODESTONE, new Color(108, 108, 108));
        blockColors.put(Material.MAGMA_BLOCK, new Color(139, 66, 23));
        blockColors.put(Material.CLAY, new Color(160, 166, 177));
        blockColors.put(Material.MOSS_BLOCK, new Color(95, 141, 77));
        blockColors.put(Material.DRIPSTONE_BLOCK, new Color(134, 97, 73));
        blockColors.put(Material.TUFF, new Color(117, 110, 106));
        blockColors.put(Material.CALCITE, new Color(223, 227, 222));
        blockColors.put(Material.AMETHYST_BLOCK, new Color(154, 101, 202));
        blockColors.put(Material.BUDDING_AMETHYST, new Color(163, 108, 211));
        blockColors.put(Material.SCULK, new Color(15, 48, 59));
        blockColors.put(Material.SCULK_CATALYST, new Color(33, 85, 97));
        blockColors.put(Material.COAL_BLOCK, new Color(17, 17, 17));
        blockColors.put(Material.IRON_BLOCK, new Color(216, 216, 216));
        blockColors.put(Material.COPPER_BLOCK, new Color(184, 94, 56));
        blockColors.put(Material.EXPOSED_COPPER, new Color(142, 115, 91));
        blockColors.put(Material.WEATHERED_COPPER, new Color(91, 172, 129));
        blockColors.put(Material.OXIDIZED_COPPER, new Color(74, 152, 116));
        blockColors.put(Material.GOLD_BLOCK, new Color(250, 238, 77));
        blockColors.put(Material.DIAMOND_BLOCK, new Color(97, 219, 213));
        blockColors.put(Material.EMERALD_BLOCK, new Color(80, 219, 71));
        blockColors.put(Material.LAPIS_BLOCK, new Color(38, 97, 153));
        blockColors.put(Material.REDSTONE_BLOCK, new Color(179, 32, 32));
        blockColors.put(Material.NETHERITE_BLOCK, new Color(65, 61, 56));
        blockColors.put(Material.OAK_WOOD, new Color(154, 124, 75));
        blockColors.put(Material.SPRUCE_WOOD, new Color(111, 83, 52));
        blockColors.put(Material.BIRCH_WOOD, new Color(200, 183, 128));
        blockColors.put(Material.JUNGLE_WOOD, new Color(167, 133, 85));
        blockColors.put(Material.ACACIA_WOOD, new Color(175, 92, 51));
        blockColors.put(Material.DARK_OAK_WOOD, new Color(69, 46, 29));
        blockColors.put(Material.CRIMSON_HYPHAE, new Color(92, 26, 29));
        blockColors.put(Material.WARPED_HYPHAE, new Color(52, 98, 92));
        blockColors.put(Material.STRIPPED_OAK_WOOD, new Color(197, 158, 92));
        blockColors.put(Material.STRIPPED_SPRUCE_WOOD, new Color(130, 101, 73));
        blockColors.put(Material.STRIPPED_BIRCH_WOOD, new Color(205, 196, 140));
        blockColors.put(Material.STRIPPED_JUNGLE_WOOD, new Color(180, 142, 94));
        blockColors.put(Material.STRIPPED_ACACIA_WOOD, new Color(177, 97, 53));
        blockColors.put(Material.STRIPPED_DARK_OAK_WOOD, new Color(98, 69, 45));
        blockColors.put(Material.STRIPPED_CRIMSON_HYPHAE, new Color(128, 34, 37));
        blockColors.put(Material.STRIPPED_WARPED_HYPHAE, new Color(60, 120, 111));
        blockColors.put(Material.OAK_PLANKS, new Color(193, 158, 98));
        blockColors.put(Material.SPRUCE_PLANKS, new Color(114, 84, 50));
        blockColors.put(Material.BIRCH_PLANKS, new Color(196, 182, 128));
        blockColors.put(Material.JUNGLE_PLANKS, new Color(169, 132, 85));
        blockColors.put(Material.ACACIA_PLANKS, new Color(174, 92, 53));
        blockColors.put(Material.DARK_OAK_PLANKS, new Color(62, 43, 21));
        blockColors.put(Material.CRIMSON_PLANKS, new Color(123, 25, 25));
        blockColors.put(Material.WARPED_PLANKS, new Color(44, 120, 100));
        blockColors.put(Material.STONE, new Color(125, 125, 125));
        blockColors.put(Material.POLISHED_GRANITE, new Color(166, 120, 91));
        blockColors.put(Material.DIORITE, new Color(189, 189, 189));
        blockColors.put(Material.POLISHED_DIORITE, new Color(211, 211, 211));
        blockColors.put(Material.ANDESITE, new Color(136, 136, 136));
        blockColors.put(Material.POLISHED_ANDESITE, new Color(158, 158, 158));
        blockColors.put(Material.DEEPSLATE, new Color(83, 83, 83));
        blockColors.put(Material.POLISHED_DEEPSLATE, new Color(95, 95, 95));
        blockColors.put(Material.COBBLED_DEEPSLATE, new Color(82, 82, 82));
        blockColors.put(Material.CHISELED_DEEPSLATE, new Color(78, 78, 78));
        blockColors.put(Material.CRACKED_DEEPSLATE_BRICKS, new Color(72, 72, 72));
        blockColors.put(Material.CRACKED_DEEPSLATE_TILES, new Color(70, 70, 70));
        blockColors.put(Material.DEEPSLATE_BRICKS, new Color(75, 75, 75));
        blockColors.put(Material.DEEPSLATE_TILES, new Color(71, 71, 71));
        blockColors.put(Material.COBBLESTONE, new Color(126, 126, 126));
        blockColors.put(Material.MOSSY_COBBLESTONE, new Color(106, 135, 106));
        blockColors.put(Material.CHISELED_STONE_BRICKS, new Color(122, 122, 122));
        blockColors.put(Material.CRACKED_STONE_BRICKS, new Color(115, 115, 115));
        blockColors.put(Material.MOSSY_STONE_BRICKS, new Color(112, 128, 112));
        blockColors.put(Material.STONE_BRICKS, new Color(123, 123, 123));
        blockColors.put(Material.SANDSTONE, new Color(216, 202, 156));
        blockColors.put(Material.CHISELED_SANDSTONE, new Color(214, 200, 153));
        blockColors.put(Material.CUT_SANDSTONE, new Color(215, 201, 154));
        blockColors.put(Material.SMOOTH_SANDSTONE, new Color(220, 207, 163));
        blockColors.put(Material.RED_SANDSTONE, new Color(178, 77, 47));
        blockColors.put(Material.CHISELED_RED_SANDSTONE, new Color(175, 75, 45));
        blockColors.put(Material.CUT_RED_SANDSTONE, new Color(176, 76, 46));
        blockColors.put(Material.SMOOTH_RED_SANDSTONE, new Color(182, 79, 47));
        blockColors.put(Material.QUARTZ_BLOCK, new Color(236, 233, 228));
        blockColors.put(Material.CHISELED_QUARTZ_BLOCK, new Color(233, 230, 225));
        blockColors.put(Material.QUARTZ_PILLAR, new Color(232, 229, 224));
        blockColors.put(Material.SMOOTH_QUARTZ, new Color(240, 237, 233));
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

    public List<Material> generateGradient(int gradientLengthInBlocks, Map<Material, Integer> gradientBlockPositions) {
        List<Material> gradient = new ArrayList<>();

        TreeMap<Integer, Color> colorPositions = new TreeMap<>();

        Color defaultStartColor = blockColors.get(Material.WHITE_CONCRETE);
        Color defaultEndColor = blockColors.get(Material.BLACK_CONCRETE);

        for (Map.Entry<Material, Integer> entry : gradientBlockPositions.entrySet()) {
            Color blockColor = blockColors.get(entry.getKey());
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

            Material closestBlock = findClosestColorBlock(interpolatedColor);
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

    private Material findClosestColorBlock(Color targetColor) {
        Material closestBlock = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Material, Color> entry : blockColors.entrySet()) {
            Color blockColor = entry.getValue();
            double distance = calculateColorDistance(targetColor, blockColor);

            if (distance < minDistance) {
                minDistance = distance;
                closestBlock = entry.getKey();
            }
        }

        return closestBlock;
    }

    private double calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();

        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
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
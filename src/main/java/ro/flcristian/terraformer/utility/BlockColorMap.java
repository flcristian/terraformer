package ro.flcristian.terraformer.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Material;

public class BlockColorMap {
    public final Map<Material, Color> BlockColors;

    private BlockColorMap() {
        BlockColors = new HashMap<>();

        BlockColors.put(Material.YELLOW_WOOL, new Color(249, 198, 40));
        BlockColors.put(Material.YELLOW_TERRACOTTA, new Color(186, 133, 35));
        BlockColors.put(Material.YELLOW_GLAZED_TERRACOTTA, new Color(236, 195, 91));
        BlockColors.put(Material.YELLOW_CONCRETE, new Color(241, 175, 21));
        BlockColors.put(Material.WHITE_WOOL, new Color(234, 236, 237));
        BlockColors.put(Material.WHITE_TERRACOTTA, new Color(210, 178, 161));
        BlockColors.put(Material.WHITE_GLAZED_TERRACOTTA, new Color(186, 212, 206));
        BlockColors.put(Material.WHITE_CONCRETE, new Color(207, 213, 214));
        BlockColors.put(Material.WET_SPONGE, new Color(170, 180, 70));
        BlockColors.put(Material.WEATHERED_CUT_COPPER, new Color(109, 145, 107));
        BlockColors.put(Material.WEATHERED_COPPER, new Color(109, 154, 110));
        BlockColors.put(Material.WARPED_WART_BLOCK, new Color(23, 120, 121));
        BlockColors.put(Material.WARPED_STEM, new Color(58, 59, 78));
        BlockColors.put(Material.WARPED_PLANKS, new Color(43, 105, 99));
        BlockColors.put(Material.WARPED_NYLIUM, new Color(98, 38, 38));
        BlockColors.put(Material.TUFF, new Color(108, 110, 103));
        BlockColors.put(Material.TUBE_CORAL_BLOCK, new Color(49, 88, 207));
        BlockColors.put(Material.TNT, new Color(180, 93, 90));
        BlockColors.put(Material.TERRACOTTA, new Color(152, 94, 68));
        BlockColors.put(Material.STRIPPED_WARPED_STEM, new Color(58, 152, 149));
        BlockColors.put(Material.STRIPPED_SPRUCE_LOG, new Color(116, 90, 53));
        BlockColors.put(Material.STRIPPED_OAK_LOG, new Color(179, 145, 87));
        BlockColors.put(Material.STRIPPED_MANGROVE_LOG, new Color(120, 55, 48));
        BlockColors.put(Material.STRIPPED_JUNGLE_LOG, new Color(172, 133, 85));
        BlockColors.put(Material.STRIPPED_DARK_OAK_LOG, new Color(73, 57, 36));
        BlockColors.put(Material.STRIPPED_CRIMSON_STEM, new Color(138, 58, 91));
        BlockColors.put(Material.STRIPPED_CHERRY_LOG, new Color(216, 146, 150));
        BlockColors.put(Material.STRIPPED_BIRCH_LOG, new Color(198, 177, 119));
        BlockColors.put(Material.STRIPPED_BAMBOO_BLOCK, new Color(194, 173, 81));
        BlockColors.put(Material.STRIPPED_ACACIA_LOG, new Color(176, 93, 60));
        BlockColors.put(Material.STONE_BRICKS, new Color(122, 122, 122));
        BlockColors.put(Material.STONE, new Color(126, 126, 126));
        BlockColors.put(Material.SPRUCE_PLANKS, new Color(115, 85, 49));
        BlockColors.put(Material.SPRUCE_LOG, new Color(59, 38, 17));
        BlockColors.put(Material.SPONGE, new Color(196, 193, 75));
        BlockColors.put(Material.SOUL_SOIL, new Color(76, 58, 47));
        BlockColors.put(Material.SOUL_SAND, new Color(81, 62, 51));
        BlockColors.put(Material.SNOW_BLOCK, new Color(249, 254, 254));
        BlockColors.put(Material.SMOOTH_STONE, new Color(161, 161, 161));
        BlockColors.put(Material.SMOOTH_BASALT, new Color(72, 72, 78));
        BlockColors.put(Material.SHROOMLIGHT, new Color(242, 151, 75));
        BlockColors.put(Material.SCULK, new Color(13, 18, 23));
        BlockColors.put(Material.SANDSTONE, new Color(216, 203, 156));
        BlockColors.put(Material.ROOTED_DIRT, new Color(144, 103, 76));
        BlockColors.put(Material.RESPAWN_ANCHOR, new Color(39, 24, 62));
        BlockColors.put(Material.REINFORCED_DEEPSLATE, new Color(100, 106, 99));
        BlockColors.put(Material.RED_WOOL, new Color(161, 39, 35));
        BlockColors.put(Material.RED_TERRACOTTA, new Color(143, 61, 47));
        BlockColors.put(Material.RED_SANDSTONE, new Color(187, 99, 29));
        BlockColors.put(Material.RED_NETHER_BRICKS, new Color(70, 7, 9));
        BlockColors.put(Material.RED_GLAZED_TERRACOTTA, new Color(182, 59, 52));
        BlockColors.put(Material.RESIN_BLOCK, new Color(176, 74, 25));
        BlockColors.put(Material.RESIN_BRICKS, new Color(164, 59, 24));
        BlockColors.put(Material.CHISELED_RESIN_BRICKS, new Color(144, 49, 24));
        BlockColors.put(Material.RED_CONCRETE, new Color(142, 33, 33));
        BlockColors.put(Material.REDSTONE_ORE, new Color(142, 108, 108));
        BlockColors.put(Material.REDSTONE_BLOCK, new Color(115, 12, 0));
        BlockColors.put(Material.RAW_IRON_BLOCK, new Color(166, 136, 107));
        BlockColors.put(Material.RAW_GOLD_BLOCK, new Color(221, 168, 47));
        BlockColors.put(Material.RAW_COPPER_BLOCK, new Color(156, 106, 79));
        BlockColors.put(Material.QUARTZ_BLOCK, new Color(237, 230, 224));
        BlockColors.put(Material.PURPUR_BLOCK, new Color(170, 126, 170));
        BlockColors.put(Material.PURPLE_WOOL, new Color(122, 42, 173));
        BlockColors.put(Material.PURPLE_TERRACOTTA, new Color(118, 70, 86));
        BlockColors.put(Material.PURPLE_GLAZED_TERRACOTTA, new Color(109, 49, 152));
        BlockColors.put(Material.PURPLE_CONCRETE, new Color(100, 32, 156));
        BlockColors.put(Material.PUMPKIN, new Color(198, 117, 25));
        BlockColors.put(Material.PRISMARINE_BRICKS, new Color(99, 172, 159));
        BlockColors.put(Material.PRISMARINE, new Color(99, 162, 146));
        BlockColors.put(Material.POWDER_SNOW, new Color(248, 253, 253));
        BlockColors.put(Material.POLISHED_GRANITE, new Color(155, 107, 89));
        BlockColors.put(Material.POLISHED_DIORITE, new Color(195, 195, 196));
        BlockColors.put(Material.POLISHED_DEEPSLATE, new Color(72, 72, 73));
        BlockColors.put(Material.POLISHED_BLACKSTONE_BRICKS, new Color(48, 43, 50));
        BlockColors.put(Material.POLISHED_BLACKSTONE, new Color(53, 49, 57));
        BlockColors.put(Material.POLISHED_BASALT, new Color(91, 91, 94));
        BlockColors.put(Material.POLISHED_ANDESITE, new Color(132, 135, 134));
        BlockColors.put(Material.PODZOL, new Color(134, 96, 67));
        BlockColors.put(Material.GRASS_BLOCK, new Color(134, 96, 67));
        BlockColors.put(Material.PINK_WOOL, new Color(238, 141, 172));
        BlockColors.put(Material.PINK_TERRACOTTA, new Color(162, 78, 79));
        BlockColors.put(Material.PINK_GLAZED_TERRACOTTA, new Color(237, 156, 182));
        BlockColors.put(Material.PINK_CONCRETE, new Color(214, 101, 143));
        BlockColors.put(Material.PACKED_MUD, new Color(143, 107, 80));
        BlockColors.put(Material.PACKED_ICE, new Color(141, 180, 251));
        BlockColors.put(Material.OXIDIZED_CUT_COPPER, new Color(80, 154, 127));
        BlockColors.put(Material.OXIDIZED_COPPER, new Color(83, 164, 134));
        BlockColors.put(Material.ORANGE_WOOL, new Color(241, 118, 20));
        BlockColors.put(Material.ORANGE_TERRACOTTA, new Color(162, 84, 38));
        BlockColors.put(Material.ORANGE_GLAZED_TERRACOTTA, new Color(162, 146, 87));
        BlockColors.put(Material.ORANGE_CONCRETE, new Color(224, 97, 1));
        BlockColors.put(Material.OBSIDIAN, new Color(15, 11, 25));
        BlockColors.put(Material.OAK_PLANKS, new Color(162, 131, 79));
        BlockColors.put(Material.OAK_LOG, new Color(109, 85, 51));
        BlockColors.put(Material.NOTE_BLOCK, new Color(92, 60, 41));
        BlockColors.put(Material.NETHER_WART_BLOCK, new Color(114, 3, 2));
        BlockColors.put(Material.NETHER_QUARTZ_ORE, new Color(121, 70, 66));
        BlockColors.put(Material.NETHER_GOLD_ORE, new Color(118, 57, 43));
        BlockColors.put(Material.NETHER_BRICKS, new Color(33, 17, 20));
        BlockColors.put(Material.NETHERRACK, new Color(98, 38, 38));
        BlockColors.put(Material.NETHERITE_BLOCK, new Color(68, 63, 66));
        BlockColors.put(Material.MYCELIUM, new Color(131, 105, 106));
        BlockColors.put(Material.MUSHROOM_STEM, new Color(203, 196, 185));
        BlockColors.put(Material.MUD_BRICKS, new Color(171, 134, 97));
        BlockColors.put(Material.MUDDY_MANGROVE_ROOTS, new Color(68, 59, 49));
        BlockColors.put(Material.MUD, new Color(60, 58, 61));
        BlockColors.put(Material.MOSS_BLOCK, new Color(89, 110, 45));
        BlockColors.put(Material.MOSSY_STONE_BRICKS, new Color(116, 121, 106));
        BlockColors.put(Material.MOSSY_COBBLESTONE, new Color(109, 118, 94));
        BlockColors.put(Material.MELON, new Color(114, 146, 30));
        BlockColors.put(Material.MANGROVE_PLANKS, new Color(118, 54, 49));
        BlockColors.put(Material.MANGROVE_LOG, new Color(84, 67, 41));
        BlockColors.put(Material.MAGENTA_WOOL, new Color(189, 69, 180));
        BlockColors.put(Material.MAGENTA_TERRACOTTA, new Color(150, 88, 109));
        BlockColors.put(Material.MAGENTA_GLAZED_TERRACOTTA, new Color(207, 100, 190));
        BlockColors.put(Material.MAGENTA_CONCRETE, new Color(169, 48, 159));
        BlockColors.put(Material.LIME_WOOL, new Color(112, 185, 26));
        BlockColors.put(Material.LIME_TERRACOTTA, new Color(103, 118, 53));
        BlockColors.put(Material.LIME_GLAZED_TERRACOTTA, new Color(163, 197, 54));
        BlockColors.put(Material.LIME_CONCRETE, new Color(94, 169, 25));
        BlockColors.put(Material.LIGHT_GRAY_WOOL, new Color(142, 142, 135));
        BlockColors.put(Material.LIGHT_GRAY_TERRACOTTA, new Color(135, 107, 98));
        BlockColors.put(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, new Color(145, 167, 169));
        BlockColors.put(Material.LIGHT_GRAY_CONCRETE, new Color(125, 125, 115));
        BlockColors.put(Material.LIGHT_BLUE_WOOL, new Color(58, 175, 217));
        BlockColors.put(Material.LIGHT_BLUE_TERRACOTTA, new Color(114, 109, 138));
        BlockColors.put(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, new Color(96, 165, 209));
        BlockColors.put(Material.LIGHT_BLUE_CONCRETE, new Color(36, 137, 199));
        BlockColors.put(Material.LAPIS_ORE, new Color(105, 117, 143));
        BlockColors.put(Material.LAPIS_BLOCK, new Color(31, 67, 140));
        BlockColors.put(Material.JUNGLE_PLANKS, new Color(161, 115, 81));
        BlockColors.put(Material.JUNGLE_LOG, new Color(85, 68, 25));
        BlockColors.put(Material.IRON_ORE, new Color(138, 130, 123));
        BlockColors.put(Material.IRON_BLOCK, new Color(222, 222, 222));
        BlockColors.put(Material.HORN_CORAL_BLOCK, new Color(216, 199, 66));
        BlockColors.put(Material.HONEYCOMB_BLOCK, new Color(229, 149, 30));
        BlockColors.put(Material.HAY_BLOCK, new Color(167, 137, 38));
        BlockColors.put(Material.GREEN_WOOL, new Color(85, 110, 27));
        BlockColors.put(Material.GREEN_TERRACOTTA, new Color(76, 83, 42));
        BlockColors.put(Material.GREEN_GLAZED_TERRACOTTA, new Color(114, 139, 62));
        BlockColors.put(Material.GREEN_CONCRETE, new Color(73, 91, 36));
        BlockColors.put(Material.GRAY_WOOL, new Color(63, 68, 72));
        BlockColors.put(Material.GRAY_TERRACOTTA, new Color(58, 42, 36));
        BlockColors.put(Material.GRAY_GLAZED_TERRACOTTA, new Color(83, 91, 94));
        BlockColors.put(Material.GRAY_CONCRETE, new Color(55, 58, 62));
        BlockColors.put(Material.GRANITE, new Color(149, 103, 86));
        BlockColors.put(Material.GOLD_ORE, new Color(147, 135, 105));
        BlockColors.put(Material.GOLD_BLOCK, new Color(248, 211, 62));
        BlockColors.put(Material.GILDED_BLACKSTONE, new Color(56, 43, 39));
        BlockColors.put(Material.FIRE_CORAL_BLOCK, new Color(164, 35, 47));
        BlockColors.put(Material.EXPOSED_CUT_COPPER, new Color(155, 122, 101));
        BlockColors.put(Material.EXPOSED_COPPER, new Color(161, 126, 104));
        BlockColors.put(Material.END_STONE_BRICKS, new Color(219, 224, 162));
        BlockColors.put(Material.END_STONE, new Color(219, 223, 158));
        BlockColors.put(Material.EMERALD_ORE, new Color(106, 137, 114));
        BlockColors.put(Material.EMERALD_BLOCK, new Color(43, 205, 90));
        BlockColors.put(Material.DRIPSTONE_BLOCK, new Color(134, 107, 92));
        BlockColors.put(Material.DRIED_KELP_BLOCK, new Color(52, 60, 40));
        BlockColors.put(Material.DIRT_PATH, new Color(148, 122, 65));
        BlockColors.put(Material.DIRT, new Color(134, 96, 67));
        BlockColors.put(Material.DIORITE, new Color(189, 189, 189));
        BlockColors.put(Material.DIAMOND_ORE, new Color(120, 143, 143));
        BlockColors.put(Material.DIAMOND_BLOCK, new Color(101, 239, 229));
        BlockColors.put(Material.DEEPSLATE, new Color(87, 87, 89));
        BlockColors.put(Material.DEEPSLATE_TILES, new Color(55, 55, 56));
        BlockColors.put(Material.DEEPSLATE_REDSTONE_ORE, new Color(107, 72, 73));
        BlockColors.put(Material.DEEPSLATE_LAPIS_ORE, new Color(79, 91, 118));
        BlockColors.put(Material.DEEPSLATE_IRON_ORE, new Color(109, 101, 96));
        BlockColors.put(Material.DEEPSLATE_GOLD_ORE, new Color(118, 104, 77));
        BlockColors.put(Material.DEEPSLATE_EMERALD_ORE, new Color(77, 106, 87));
        BlockColors.put(Material.DEEPSLATE_DIAMOND_ORE, new Color(83, 109, 110));
        BlockColors.put(Material.DEEPSLATE_COPPER_ORE, new Color(93, 94, 89));
        BlockColors.put(Material.DEEPSLATE_COAL_ORE, new Color(73, 73, 75));
        BlockColors.put(Material.DEEPSLATE_BRICKS, new Color(71, 71, 71));
        BlockColors.put(Material.DEAD_TUBE_CORAL_BLOCK, new Color(131, 124, 120));
        BlockColors.put(Material.DEAD_HORN_CORAL_BLOCK, new Color(133, 126, 122));
        BlockColors.put(Material.DEAD_FIRE_CORAL_BLOCK, new Color(132, 124, 120));
        BlockColors.put(Material.DEAD_BUBBLE_CORAL_BLOCK, new Color(132, 124, 120));
        BlockColors.put(Material.DEAD_BRAIN_CORAL_BLOCK, new Color(125, 118, 115));
        BlockColors.put(Material.DARK_PRISMARINE, new Color(52, 92, 76));
        BlockColors.put(Material.DARK_OAK_PLANKS, new Color(67, 43, 20));
        BlockColors.put(Material.DARK_OAK_LOG, new Color(60, 47, 26));
        BlockColors.put(Material.CYAN_WOOL, new Color(21, 138, 145));
        BlockColors.put(Material.CYAN_TERRACOTTA, new Color(87, 91, 91));
        BlockColors.put(Material.CYAN_GLAZED_TERRACOTTA, new Color(52, 116, 122));
        BlockColors.put(Material.CYAN_CONCRETE, new Color(21, 119, 136));
        BlockColors.put(Material.CUT_SANDSTONE, new Color(218, 207, 160));
        BlockColors.put(Material.CUT_RED_SANDSTONE, new Color(190, 102, 32));
        BlockColors.put(Material.CUT_COPPER, new Color(191, 107, 81));
        BlockColors.put(Material.CRYING_OBSIDIAN, new Color(34, 10, 63));
        BlockColors.put(Material.CRIMSON_STEM, new Color(93, 26, 30));
        BlockColors.put(Material.CRIMSON_PLANKS, new Color(101, 49, 71));
        BlockColors.put(Material.CRIMSON_NYLIUM, new Color(98, 38, 38));
        BlockColors.put(Material.CRACKED_STONE_BRICKS, new Color(118, 118, 118));
        BlockColors.put(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, new Color(44, 38, 44));
        BlockColors.put(Material.CRACKED_NETHER_BRICKS, new Color(40, 20, 24));
        BlockColors.put(Material.CRACKED_DEEPSLATE_TILES, new Color(53, 53, 53));
        BlockColors.put(Material.CRACKED_DEEPSLATE_BRICKS, new Color(64, 64, 65));
        BlockColors.put(Material.COPPER_ORE, new Color(124, 125, 119));
        BlockColors.put(Material.COPPER_BLOCK, new Color(193, 108, 80));
        BlockColors.put(Material.COBBLESTONE, new Color(128, 127, 127));
        BlockColors.put(Material.COBBLED_DEEPSLATE, new Color(77, 77, 80));
        BlockColors.put(Material.COARSE_DIRT, new Color(119, 85, 59));
        BlockColors.put(Material.COAL_ORE, new Color(104, 104, 103));
        BlockColors.put(Material.COAL_BLOCK, new Color(16, 16, 16));
        BlockColors.put(Material.CLAY, new Color(161, 167, 179));
        BlockColors.put(Material.CHISELED_STONE_BRICKS, new Color(120, 119, 120));
        BlockColors.put(Material.CHISELED_SANDSTONE, new Color(216, 203, 155));
        BlockColors.put(Material.CHISELED_RED_SANDSTONE, new Color(183, 97, 27));
        BlockColors.put(Material.CHISELED_QUARTZ_BLOCK, new Color(232, 227, 218));
        BlockColors.put(Material.CHISELED_POLISHED_BLACKSTONE, new Color(54, 49, 57));
        BlockColors.put(Material.CHISELED_NETHER_BRICKS, new Color(48, 24, 28));
        BlockColors.put(Material.CHISELED_DEEPSLATE, new Color(55, 55, 56));
        BlockColors.put(Material.CHISELED_BOOKSHELF, new Color(177, 144, 88));
        BlockColors.put(Material.CHERRY_PLANKS, new Color(227, 179, 173));
        BlockColors.put(Material.CHERRY_LOG, new Color(55, 33, 44));
        BlockColors.put(Material.CHERRY_LEAVES, new Color(230, 173, 195));
        BlockColors.put(Material.CALCITE, new Color(224, 225, 221));
        BlockColors.put(Material.BUBBLE_CORAL_BLOCK, new Color(166, 27, 163));
        BlockColors.put(Material.BROWN_WOOL, new Color(114, 72, 41));
        BlockColors.put(Material.BROWN_TERRACOTTA, new Color(77, 51, 36));
        BlockColors.put(Material.BROWN_MUSHROOM_BLOCK, new Color(149, 112, 81));
        BlockColors.put(Material.BROWN_GLAZED_TERRACOTTA, new Color(125, 106, 83));
        BlockColors.put(Material.BROWN_CONCRETE, new Color(96, 60, 32));
        BlockColors.put(Material.BRICKS, new Color(151, 97, 83));
        BlockColors.put(Material.BRAIN_CORAL_BLOCK, new Color(208, 92, 160));
        BlockColors.put(Material.BOOKSHELF, new Color(115, 93, 58));
        BlockColors.put(Material.BONE_BLOCK, new Color(230, 227, 209));
        BlockColors.put(Material.BLUE_WOOL, new Color(53, 57, 157));
        BlockColors.put(Material.BLUE_TERRACOTTA, new Color(74, 60, 91));
        BlockColors.put(Material.BLUE_ICE, new Color(116, 168, 253));
        BlockColors.put(Material.BLUE_GLAZED_TERRACOTTA, new Color(48, 68, 144));
        BlockColors.put(Material.BLUE_CONCRETE, new Color(45, 47, 143));
        BlockColors.put(Material.BLACK_WOOL, new Color(21, 21, 26));
        BlockColors.put(Material.BLACK_TERRACOTTA, new Color(37, 23, 17));
        BlockColors.put(Material.BLACK_GLAZED_TERRACOTTA, new Color(69, 30, 32));
        BlockColors.put(Material.BLACK_CONCRETE, new Color(8, 10, 15));
        BlockColors.put(Material.BLACKSTONE, new Color(42, 35, 41));
        BlockColors.put(Material.BIRCH_PLANKS, new Color(193, 175, 121));
        BlockColors.put(Material.BIRCH_LOG, new Color(219, 217, 213));
        BlockColors.put(Material.BEE_NEST, new Color(197, 150, 77));
        BlockColors.put(Material.BEEHIVE, new Color(158, 127, 76));
        BlockColors.put(Material.BEDROCK, new Color(85, 85, 85));
        BlockColors.put(Material.BASALT, new Color(74, 73, 79));
        BlockColors.put(Material.BARREL, new Color(107, 81, 50));
        BlockColors.put(Material.BAMBOO_PLANKS, new Color(194, 173, 81));
        BlockColors.put(Material.BAMBOO_MOSAIC, new Color(190, 170, 78));
        BlockColors.put(Material.BAMBOO_BLOCK, new Color(127, 144, 58));
        BlockColors.put(Material.AZALEA_LEAVES, new Color(90, 115, 44));
        BlockColors.put(Material.ANDESITE, new Color(136, 136, 137));
        BlockColors.put(Material.AMETHYST_BLOCK, new Color(134, 98, 191));
        BlockColors.put(Material.ACACIA_PLANKS, new Color(168, 90, 50));
        BlockColors.put(Material.ACACIA_LOG, new Color(103, 97, 87));
        BlockColors.put(Material.PALE_OAK_PLANKS, new Color(223, 223, 207));
        BlockColors.put(Material.PALE_OAK_WOOD, new Color(44, 39, 37));
        BlockColors.put(Material.STRIPPED_PALE_OAK_WOOD, new Color(210, 210, 194));
    }

    private static final Supplier<BlockColorMap> instance = new Supplier<>() {
        private final BlockColorMap singletonInstance = new BlockColorMap();

        @Override
        public BlockColorMap get() {
            return singletonInstance;
        }
    };

    public static BlockColorMap getInstance() {
        return instance.get();
    }

    public Material findClosestColorBlock(Color targetColor) {
        Material closestBlock = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Material, Color> entry : BlockColors.entrySet()) {
            Color blockColor = entry.getValue();
            double distance = calculateColorDistance(targetColor, blockColor);

            if (distance < minDistance) {
                minDistance = distance;
                closestBlock = entry.getKey();
            }
        }

        return closestBlock;
    }

    public Color calculateColor(Map<Material, Integer> materialWeights) {
        if (materialWeights == null || materialWeights.isEmpty()) {
            return new Color(0, 0, 0);
        }

        float totalWeight = materialWeights.values().stream().mapToInt(Integer::intValue).sum();
        float totalRed = 0;
        float totalGreen = 0;
        float totalBlue = 0;

        for (Map.Entry<Material, Integer> entry : materialWeights.entrySet()) {
            Material material = entry.getKey();
            float weight = entry.getValue() / totalWeight;

            Color color = BlockColors.get(material);
            if (color != null) {
                totalRed += color.getRed() * weight;
                totalGreen += color.getGreen() * weight;
                totalBlue += color.getBlue() * weight;
            }
        }

        return new Color(
                Math.round(totalRed),
                Math.round(totalGreen),
                Math.round(totalBlue));
    }

    public Material calculateMaterial(Map<Material, Integer> materials) {
        Color averageColor = calculateColor(materials);
        return findClosestColorBlock(averageColor);
    }

    private double calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();

        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}

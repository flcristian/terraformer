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

        blockColors.put(Material.YELLOW_WOOL, new Color(249, 198, 40));
        blockColors.put(Material.YELLOW_TERRACOTTA, new Color(186, 133, 35));
        blockColors.put(Material.YELLOW_SHULKER_BOX, new Color(249, 190, 30));
        blockColors.put(Material.YELLOW_GLAZED_TERRACOTTA, new Color(236, 195, 91));
        blockColors.put(Material.YELLOW_CONCRETE, new Color(241, 175, 21));
        blockColors.put(Material.WHITE_WOOL, new Color(234, 236, 237));
        blockColors.put(Material.WHITE_TERRACOTTA, new Color(210, 178, 161));
        blockColors.put(Material.WHITE_SHULKER_BOX, new Color(218, 223, 223));
        blockColors.put(Material.WHITE_GLAZED_TERRACOTTA, new Color(186, 212, 206));
        blockColors.put(Material.WHITE_CONCRETE, new Color(207, 213, 214));
        blockColors.put(Material.WET_SPONGE, new Color(170, 180, 70));
        blockColors.put(Material.WEATHERED_CUT_COPPER, new Color(109, 145, 107));
        blockColors.put(Material.WEATHERED_COPPER, new Color(109, 154, 110));
        blockColors.put(Material.WARPED_WART_BLOCK, new Color(23, 120, 121));
        blockColors.put(Material.WARPED_STEM, new Color(58, 59, 78));
        blockColors.put(Material.WARPED_PLANKS, new Color(43, 105, 99));
        blockColors.put(Material.WARPED_NYLIUM, new Color(43, 115, 101));
        blockColors.put(Material.VERDANT_FROGLIGHT, new Color(217, 238, 215));
        blockColors.put(Material.TUFF, new Color(108, 110, 103));
        blockColors.put(Material.TUBE_CORAL_BLOCK, new Color(49, 88, 207));
        blockColors.put(Material.TNT, new Color(180, 93, 90));
        blockColors.put(Material.TERRACOTTA, new Color(152, 94, 68));
        blockColors.put(Material.STRIPPED_WARPED_STEM, new Color(58, 152, 149));
        blockColors.put(Material.STRIPPED_SPRUCE_LOG, new Color(116, 90, 53));
        blockColors.put(Material.STRIPPED_OAK_LOG, new Color(179, 145, 87));
        blockColors.put(Material.STRIPPED_MANGROVE_LOG, new Color(120, 55, 48));
        blockColors.put(Material.STRIPPED_JUNGLE_LOG, new Color(172, 133, 85));
        blockColors.put(Material.STRIPPED_DARK_OAK_LOG, new Color(73, 57, 36));
        blockColors.put(Material.STRIPPED_CRIMSON_STEM, new Color(138, 58, 91));
        blockColors.put(Material.STRIPPED_CHERRY_LOG, new Color(216, 146, 150));
        blockColors.put(Material.STRIPPED_BIRCH_LOG, new Color(198, 177, 119));
        blockColors.put(Material.STRIPPED_BAMBOO_BLOCK, new Color(194, 173, 81));
        blockColors.put(Material.STRIPPED_ACACIA_LOG, new Color(176, 93, 60));
        blockColors.put(Material.STONE_BRICKS, new Color(122, 122, 122));
        blockColors.put(Material.STONE, new Color(126, 126, 126));
        blockColors.put(Material.SPRUCE_PLANKS, new Color(115, 85, 49));
        blockColors.put(Material.SPRUCE_LOG, new Color(59, 38, 17));
        blockColors.put(Material.SPONGE, new Color(196, 193, 75));
        blockColors.put(Material.SOUL_SOIL, new Color(76, 58, 47));
        blockColors.put(Material.SOUL_SAND, new Color(81, 62, 51));
        blockColors.put(Material.SNOW_BLOCK, new Color(249, 254, 254));
        blockColors.put(Material.SMOOTH_STONE, new Color(161, 161, 161));
        blockColors.put(Material.SMOOTH_BASALT, new Color(72, 72, 78));
        blockColors.put(Material.SMITHING_TABLE, new Color(59, 38, 39));
        blockColors.put(Material.SHULKER_BOX, new Color(151, 103, 151));
        blockColors.put(Material.SHROOMLIGHT, new Color(242, 151, 75));
        blockColors.put(Material.SEA_LANTERN, new Color(182, 207, 198));
        blockColors.put(Material.SCULK_CATALYST, new Color(78, 95, 91));
        blockColors.put(Material.SCULK, new Color(13, 18, 23));
        blockColors.put(Material.SANDSTONE, new Color(216, 203, 156));
        blockColors.put(Material.ROOTED_DIRT, new Color(144, 103, 76));
        blockColors.put(Material.RESPAWN_ANCHOR, new Color(39, 24, 62));
        blockColors.put(Material.REINFORCED_DEEPSLATE, new Color(100, 106, 99));
        blockColors.put(Material.RED_WOOL, new Color(161, 39, 35));
        blockColors.put(Material.RED_TERRACOTTA, new Color(143, 61, 47));
        blockColors.put(Material.RED_SHULKER_BOX, new Color(143, 32, 31));
        blockColors.put(Material.RED_SANDSTONE, new Color(187, 99, 29));
        blockColors.put(Material.RED_NETHER_BRICKS, new Color(70, 7, 9));
        blockColors.put(Material.RED_MUSHROOM_BLOCK, new Color(201, 48, 46));
        blockColors.put(Material.RED_GLAZED_TERRACOTTA, new Color(182, 59, 52));
        blockColors.put(Material.RED_CONCRETE, new Color(142, 33, 33));
        blockColors.put(Material.REDSTONE_ORE, new Color(142, 108, 108));
        blockColors.put(Material.REDSTONE_LAMP, new Color(99, 57, 31));
        blockColors.put(Material.REDSTONE_BLOCK, new Color(115, 12, 0));
        blockColors.put(Material.RAW_IRON_BLOCK, new Color(166, 136, 107));
        blockColors.put(Material.RAW_GOLD_BLOCK, new Color(221, 168, 47));
        blockColors.put(Material.RAW_COPPER_BLOCK, new Color(156, 106, 79));
        blockColors.put(Material.QUARTZ_BLOCK, new Color(237, 230, 224));
        blockColors.put(Material.PURPUR_BLOCK, new Color(170, 126, 170));
        blockColors.put(Material.PURPLE_WOOL, new Color(122, 42, 173));
        blockColors.put(Material.PURPLE_TERRACOTTA, new Color(118, 70, 86));
        blockColors.put(Material.PURPLE_SHULKER_BOX, new Color(105, 33, 158));
        blockColors.put(Material.PURPLE_GLAZED_TERRACOTTA, new Color(109, 49, 152));
        blockColors.put(Material.PURPLE_CONCRETE, new Color(100, 32, 156));
        blockColors.put(Material.PUMPKIN, new Color(198, 117, 25));
        blockColors.put(Material.PRISMARINE_BRICKS, new Color(99, 172, 159));
        blockColors.put(Material.PRISMARINE, new Color(99, 162, 146));
        blockColors.put(Material.POWDER_SNOW, new Color(248, 253, 253));
        blockColors.put(Material.POLISHED_GRANITE, new Color(155, 107, 89));
        blockColors.put(Material.POLISHED_DIORITE, new Color(195, 195, 196));
        blockColors.put(Material.POLISHED_DEEPSLATE, new Color(72, 72, 73));
        blockColors.put(Material.POLISHED_BLACKSTONE_BRICKS, new Color(48, 43, 50));
        blockColors.put(Material.POLISHED_BLACKSTONE, new Color(53, 49, 57));
        blockColors.put(Material.POLISHED_BASALT, new Color(91, 91, 94));
        blockColors.put(Material.POLISHED_ANDESITE, new Color(132, 135, 134));
        blockColors.put(Material.PODZOL, new Color(92, 63, 24));
        blockColors.put(Material.PINK_WOOL, new Color(238, 141, 172));
        blockColors.put(Material.PINK_TERRACOTTA, new Color(162, 78, 79));
        blockColors.put(Material.PINK_SHULKER_BOX, new Color(232, 124, 160));
        blockColors.put(Material.PINK_GLAZED_TERRACOTTA, new Color(237, 156, 182));
        blockColors.put(Material.PINK_CONCRETE, new Color(214, 101, 143));
        blockColors.put(Material.PEARLESCENT_FROGLIGHT, new Color(239, 230, 232));
        blockColors.put(Material.PACKED_MUD, new Color(143, 107, 80));
        blockColors.put(Material.PACKED_ICE, new Color(141, 180, 251));
        blockColors.put(Material.OXIDIZED_CUT_COPPER, new Color(80, 154, 127));
        blockColors.put(Material.OXIDIZED_COPPER, new Color(83, 164, 134));
        blockColors.put(Material.ORANGE_WOOL, new Color(241, 118, 20));
        blockColors.put(Material.ORANGE_TERRACOTTA, new Color(162, 84, 38));
        blockColors.put(Material.ORANGE_SHULKER_BOX, new Color(236, 108, 10));
        blockColors.put(Material.ORANGE_GLAZED_TERRACOTTA, new Color(162, 146, 87));
        blockColors.put(Material.ORANGE_CONCRETE, new Color(224, 97, 1));
        blockColors.put(Material.OCHRE_FROGLIGHT, new Color(247, 238, 189));
        blockColors.put(Material.OBSIDIAN, new Color(15, 11, 25));
        blockColors.put(Material.OAK_PLANKS, new Color(162, 131, 79));
        blockColors.put(Material.OAK_LOG, new Color(109, 85, 51));
        blockColors.put(Material.NOTE_BLOCK, new Color(92, 60, 41));
        blockColors.put(Material.NETHER_WART_BLOCK, new Color(114, 3, 2));
        blockColors.put(Material.NETHER_QUARTZ_ORE, new Color(121, 70, 66));
        blockColors.put(Material.NETHER_GOLD_ORE, new Color(118, 57, 43));
        blockColors.put(Material.NETHER_BRICKS, new Color(33, 17, 20));
        blockColors.put(Material.NETHERRACK, new Color(98, 38, 38));
        blockColors.put(Material.NETHERITE_BLOCK, new Color(68, 63, 66));
        blockColors.put(Material.MYCELIUM, new Color(131, 105, 106));
        blockColors.put(Material.MUSHROOM_STEM, new Color(203, 196, 185));
        blockColors.put(Material.MUD_BRICKS, new Color(171, 134, 97));
        blockColors.put(Material.MUDDY_MANGROVE_ROOTS, new Color(68, 59, 49));
        blockColors.put(Material.MUD, new Color(60, 58, 61));
        blockColors.put(Material.MOSS_BLOCK, new Color(89, 110, 45));
        blockColors.put(Material.MOSSY_STONE_BRICKS, new Color(116, 121, 106));
        blockColors.put(Material.MOSSY_COBBLESTONE, new Color(109, 118, 94));
        blockColors.put(Material.MELON, new Color(114, 146, 30));
        blockColors.put(Material.MANGROVE_PLANKS, new Color(118, 54, 49));
        blockColors.put(Material.MANGROVE_LOG, new Color(84, 67, 41));
        blockColors.put(Material.MAGMA_BLOCK, new Color(141, 62, 31));
        blockColors.put(Material.MAGENTA_WOOL, new Color(189, 69, 180));
        blockColors.put(Material.MAGENTA_TERRACOTTA, new Color(150, 88, 109));
        blockColors.put(Material.MAGENTA_SHULKER_BOX, new Color(176, 55, 165));
        blockColors.put(Material.MAGENTA_GLAZED_TERRACOTTA, new Color(207, 100, 190));
        blockColors.put(Material.MAGENTA_CONCRETE, new Color(169, 48, 159));
        blockColors.put(Material.LIME_WOOL, new Color(112, 185, 26));
        blockColors.put(Material.LIME_TERRACOTTA, new Color(103, 118, 53));
        blockColors.put(Material.LIME_SHULKER_BOX, new Color(102, 175, 23));
        blockColors.put(Material.LIME_GLAZED_TERRACOTTA, new Color(163, 197, 54));
        blockColors.put(Material.LIME_CONCRETE, new Color(94, 169, 25));
        blockColors.put(Material.LIGHT_GRAY_WOOL, new Color(142, 142, 135));
        blockColors.put(Material.LIGHT_GRAY_TERRACOTTA, new Color(135, 107, 98));
        blockColors.put(Material.LIGHT_GRAY_SHULKER_BOX, new Color(127, 127, 118));
        blockColors.put(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, new Color(145, 167, 169));
        blockColors.put(Material.LIGHT_GRAY_CONCRETE, new Color(125, 125, 115));
        blockColors.put(Material.LIGHT_BLUE_WOOL, new Color(58, 175, 217));
        blockColors.put(Material.LIGHT_BLUE_TERRACOTTA, new Color(114, 109, 138));
        blockColors.put(Material.LIGHT_BLUE_SHULKER_BOX, new Color(51, 166, 213));
        blockColors.put(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, new Color(96, 165, 209));
        blockColors.put(Material.LIGHT_BLUE_CONCRETE, new Color(36, 137, 199));
        blockColors.put(Material.LAPIS_ORE, new Color(105, 117, 143));
        blockColors.put(Material.LAPIS_BLOCK, new Color(31, 67, 140));
        blockColors.put(Material.JUNGLE_PLANKS, new Color(161, 115, 81));
        blockColors.put(Material.JUNGLE_LOG, new Color(85, 68, 25));
        blockColors.put(Material.IRON_ORE, new Color(138, 130, 123));
        blockColors.put(Material.IRON_BLOCK, new Color(222, 222, 222));
        blockColors.put(Material.HORN_CORAL_BLOCK, new Color(216, 199, 66));
        blockColors.put(Material.HONEYCOMB_BLOCK, new Color(229, 149, 30));
        blockColors.put(Material.HAY_BLOCK, new Color(167, 137, 38));
        blockColors.put(Material.GREEN_WOOL, new Color(85, 110, 27));
        blockColors.put(Material.GREEN_TERRACOTTA, new Color(76, 83, 42));
        blockColors.put(Material.GREEN_SHULKER_BOX, new Color(80, 102, 31));
        blockColors.put(Material.GREEN_GLAZED_TERRACOTTA, new Color(114, 139, 62));
        blockColors.put(Material.GREEN_CONCRETE, new Color(73, 91, 36));
        blockColors.put(Material.GRAY_WOOL, new Color(63, 68, 72));
        blockColors.put(Material.GRAY_TERRACOTTA, new Color(58, 42, 36));
        blockColors.put(Material.GRAY_SHULKER_BOX, new Color(56, 60, 64));
        blockColors.put(Material.GRAY_GLAZED_TERRACOTTA, new Color(83, 91, 94));
        blockColors.put(Material.GRAY_CONCRETE, new Color(55, 58, 62));
        blockColors.put(Material.GRANITE, new Color(149, 103, 86));
        blockColors.put(Material.GOLD_ORE, new Color(147, 135, 105));
        blockColors.put(Material.GOLD_BLOCK, new Color(248, 211, 62));
        blockColors.put(Material.GLOWSTONE, new Color(173, 132, 85));
        blockColors.put(Material.GILDED_BLACKSTONE, new Color(56, 43, 39));
        blockColors.put(Material.FURNACE, new Color(125, 124, 124));
        blockColors.put(Material.FIRE_CORAL_BLOCK, new Color(164, 35, 47));
        blockColors.put(Material.EXPOSED_CUT_COPPER, new Color(155, 122, 101));
        blockColors.put(Material.EXPOSED_COPPER, new Color(161, 126, 104));
        blockColors.put(Material.END_STONE_BRICKS, new Color(219, 224, 162));
        blockColors.put(Material.END_STONE, new Color(219, 223, 158));
        blockColors.put(Material.EMERALD_ORE, new Color(106, 137, 114));
        blockColors.put(Material.EMERALD_BLOCK, new Color(43, 205, 90));
        blockColors.put(Material.DRIPSTONE_BLOCK, new Color(134, 107, 92));
        blockColors.put(Material.DRIED_KELP_BLOCK, new Color(52, 60, 40));
        blockColors.put(Material.DIRT_PATH, new Color(148, 122, 65));
        blockColors.put(Material.DIRT, new Color(134, 96, 67));
        blockColors.put(Material.DIORITE, new Color(189, 189, 189));
        blockColors.put(Material.DIAMOND_ORE, new Color(120, 143, 143));
        blockColors.put(Material.DIAMOND_BLOCK, new Color(101, 239, 229));
        blockColors.put(Material.DEEPSLATE, new Color(87, 87, 89));
        blockColors.put(Material.DEEPSLATE_TILES, new Color(55, 55, 56));
        blockColors.put(Material.DEEPSLATE_REDSTONE_ORE, new Color(107, 72, 73));
        blockColors.put(Material.DEEPSLATE_LAPIS_ORE, new Color(79, 91, 118));
        blockColors.put(Material.DEEPSLATE_IRON_ORE, new Color(109, 101, 96));
        blockColors.put(Material.DEEPSLATE_GOLD_ORE, new Color(118, 104, 77));
        blockColors.put(Material.DEEPSLATE_EMERALD_ORE, new Color(77, 106, 87));
        blockColors.put(Material.DEEPSLATE_DIAMOND_ORE, new Color(83, 109, 110));
        blockColors.put(Material.DEEPSLATE_COPPER_ORE, new Color(93, 94, 89));
        blockColors.put(Material.DEEPSLATE_COAL_ORE, new Color(73, 73, 75));
        blockColors.put(Material.DEEPSLATE_BRICKS, new Color(71, 71, 71));
        blockColors.put(Material.DEAD_TUBE_CORAL_BLOCK, new Color(131, 124, 120));
        blockColors.put(Material.DEAD_HORN_CORAL_BLOCK, new Color(133, 126, 122));
        blockColors.put(Material.DEAD_FIRE_CORAL_BLOCK, new Color(132, 124, 120));
        blockColors.put(Material.DEAD_BUBBLE_CORAL_BLOCK, new Color(132, 124, 120));
        blockColors.put(Material.DEAD_BRAIN_CORAL_BLOCK, new Color(125, 118, 115));
        blockColors.put(Material.DARK_PRISMARINE, new Color(52, 92, 76));
        blockColors.put(Material.DARK_OAK_PLANKS, new Color(67, 43, 20));
        blockColors.put(Material.DARK_OAK_LOG, new Color(60, 47, 26));
        blockColors.put(Material.CYAN_WOOL, new Color(21, 138, 145));
        blockColors.put(Material.CYAN_TERRACOTTA, new Color(87, 91, 91));
        blockColors.put(Material.CYAN_SHULKER_BOX, new Color(20, 123, 137));
        blockColors.put(Material.CYAN_GLAZED_TERRACOTTA, new Color(52, 116, 122));
        blockColors.put(Material.CYAN_CONCRETE, new Color(21, 119, 136));
        blockColors.put(Material.CUT_SANDSTONE, new Color(218, 207, 160));
        blockColors.put(Material.CUT_RED_SANDSTONE, new Color(190, 102, 32));
        blockColors.put(Material.CUT_COPPER, new Color(191, 107, 81));
        blockColors.put(Material.CRYING_OBSIDIAN, new Color(34, 10, 63));
        blockColors.put(Material.CRIMSON_STEM, new Color(93, 26, 30));
        blockColors.put(Material.CRIMSON_PLANKS, new Color(101, 49, 71));
        blockColors.put(Material.CRIMSON_NYLIUM, new Color(131, 32, 32));
        blockColors.put(Material.CRAFTING_TABLE, new Color(131, 104, 65));
        blockColors.put(Material.CRACKED_STONE_BRICKS, new Color(118, 118, 118));
        blockColors.put(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, new Color(44, 38, 44));
        blockColors.put(Material.CRACKED_NETHER_BRICKS, new Color(40, 20, 24));
        blockColors.put(Material.CRACKED_DEEPSLATE_TILES, new Color(53, 53, 53));
        blockColors.put(Material.CRACKED_DEEPSLATE_BRICKS, new Color(64, 64, 65));
        blockColors.put(Material.COPPER_ORE, new Color(124, 125, 119));
        blockColors.put(Material.COPPER_BLOCK, new Color(193, 108, 80));
        blockColors.put(Material.COMPOSTER, new Color(111, 70, 32));
        blockColors.put(Material.COBBLESTONE, new Color(128, 127, 127));
        blockColors.put(Material.COBBLED_DEEPSLATE, new Color(77, 77, 80));
        blockColors.put(Material.COARSE_DIRT, new Color(119, 85, 59));
        blockColors.put(Material.COAL_ORE, new Color(104, 104, 103));
        blockColors.put(Material.COAL_BLOCK, new Color(16, 16, 16));
        blockColors.put(Material.CLAY, new Color(161, 167, 179));
        blockColors.put(Material.CHISELED_STONE_BRICKS, new Color(120, 119, 120));
        blockColors.put(Material.CHISELED_SANDSTONE, new Color(216, 203, 155));
        blockColors.put(Material.CHISELED_RED_SANDSTONE, new Color(183, 97, 27));
        blockColors.put(Material.CHISELED_QUARTZ_BLOCK, new Color(232, 227, 218));
        blockColors.put(Material.CHISELED_POLISHED_BLACKSTONE, new Color(54, 49, 57));
        blockColors.put(Material.CHISELED_NETHER_BRICKS, new Color(48, 24, 28));
        blockColors.put(Material.CHISELED_DEEPSLATE, new Color(55, 55, 56));
        blockColors.put(Material.CHISELED_BOOKSHELF, new Color(177, 144, 88));
        blockColors.put(Material.CHERRY_PLANKS, new Color(227, 179, 173));
        blockColors.put(Material.CHERRY_LOG, new Color(55, 33, 44));
        blockColors.put(Material.CHERRY_LEAVES, new Color(230, 173, 195));
        blockColors.put(Material.CALCITE, new Color(224, 225, 221));
        blockColors.put(Material.BUBBLE_CORAL_BLOCK, new Color(166, 27, 163));
        blockColors.put(Material.BROWN_WOOL, new Color(114, 72, 41));
        blockColors.put(Material.BROWN_TERRACOTTA, new Color(77, 51, 36));
        blockColors.put(Material.BROWN_SHULKER_BOX, new Color(108, 67, 37));
        blockColors.put(Material.BROWN_MUSHROOM_BLOCK, new Color(149, 112, 81));
        blockColors.put(Material.BROWN_GLAZED_TERRACOTTA, new Color(125, 106, 83));
        blockColors.put(Material.BROWN_CONCRETE, new Color(96, 60, 32));
        blockColors.put(Material.BRICKS, new Color(151, 97, 83));
        blockColors.put(Material.BRAIN_CORAL_BLOCK, new Color(208, 92, 160));
        blockColors.put(Material.BOOKSHELF, new Color(115, 93, 58));
        blockColors.put(Material.BONE_BLOCK, new Color(230, 227, 209));
        blockColors.put(Material.BLUE_WOOL, new Color(53, 57, 157));
        blockColors.put(Material.BLUE_TERRACOTTA, new Color(74, 60, 91));
        blockColors.put(Material.BLUE_SHULKER_BOX, new Color(45, 47, 142));
        blockColors.put(Material.BLUE_ICE, new Color(116, 168, 253));
        blockColors.put(Material.BLUE_GLAZED_TERRACOTTA, new Color(48, 68, 144));
        blockColors.put(Material.BLUE_CONCRETE, new Color(45, 47, 143));
        blockColors.put(Material.BLAST_FURNACE, new Color(107, 107, 107));
        blockColors.put(Material.BLACK_WOOL, new Color(21, 21, 26));
        blockColors.put(Material.BLACK_TERRACOTTA, new Color(37, 23, 17));
        blockColors.put(Material.BLACK_SHULKER_BOX, new Color(26, 26, 31));
        blockColors.put(Material.BLACK_GLAZED_TERRACOTTA, new Color(69, 30, 32));
        blockColors.put(Material.BLACK_CONCRETE, new Color(8, 10, 15));
        blockColors.put(Material.BLACKSTONE, new Color(42, 35, 41));
        blockColors.put(Material.BIRCH_PLANKS, new Color(193, 175, 121));
        blockColors.put(Material.BIRCH_LOG, new Color(219, 217, 213));
        blockColors.put(Material.BEE_NEST, new Color(197, 150, 77));
        blockColors.put(Material.BEEHIVE, new Color(158, 127, 76));
        blockColors.put(Material.BEDROCK, new Color(85, 85, 85));
        blockColors.put(Material.BASALT, new Color(74, 73, 79));
        blockColors.put(Material.BARREL, new Color(107, 81, 50));
        blockColors.put(Material.BAMBOO_PLANKS, new Color(194, 173, 81));
        blockColors.put(Material.BAMBOO_MOSAIC, new Color(190, 170, 78));
        blockColors.put(Material.BAMBOO_BLOCK, new Color(127, 144, 58));
        blockColors.put(Material.AZALEA_LEAVES, new Color(90, 115, 44));
        blockColors.put(Material.ANDESITE, new Color(136, 136, 137));
        blockColors.put(Material.AMETHYST_BLOCK, new Color(134, 98, 191));
        blockColors.put(Material.ACACIA_PLANKS, new Color(168, 90, 50));
        blockColors.put(Material.ACACIA_LOG, new Color(103, 97, 87));
        blockColors.put(Material.PALE_OAK_PLANKS, new Color(223, 223, 207));
        blockColors.put(Material.PALE_OAK_WOOD, new Color(44, 39, 37));
        blockColors.put(Material.STRIPPED_PALE_OAK_WOOD, new Color(210, 210, 194));
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
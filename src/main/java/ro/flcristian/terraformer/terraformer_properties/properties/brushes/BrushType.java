package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;
import ro.flcristian.terraformer.terraformer_properties.properties.menus.BrushSettings;
import ro.flcristian.terraformer.terraformer_properties.properties.menus.MaterialSettings;
import ro.flcristian.terraformer.utility.SkullTexturesApplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BrushType {
    BALL, ERASE, SMOOTH, ERODE, EXTRUDE, PAINT_TOP, PAINT_SURFACE, PAINT_WALL, PAINT_BOTTOM, RISE, DIG, FOLIAGE;

    public Component getName() {
        return switch (this) {
            case BALL -> Component.text("Ball").color(NamedTextColor.DARK_GREEN);
            case ERASE -> Component.text("Erase").color(NamedTextColor.WHITE);
            case SMOOTH -> Component.text("Smooth").color(NamedTextColor.AQUA);
            case ERODE -> Component.text("Erode").color(NamedTextColor.RED);
            case EXTRUDE -> Component.text("Extrude").color(NamedTextColor.LIGHT_PURPLE);
            case PAINT_TOP -> Component.text("Paint Top").color(NamedTextColor.BLUE);
            case PAINT_SURFACE -> Component.text("Paint Surface").color(NamedTextColor.BLUE);
            case PAINT_WALL -> Component.text("Paint Wall").color(NamedTextColor.BLUE);
            case PAINT_BOTTOM -> Component.text("Paint Bottom").color(NamedTextColor.BLUE);
            case RISE -> Component.text("Rise").color(NamedTextColor.DARK_PURPLE);
            case DIG -> Component.text("Dig").color(NamedTextColor.DARK_RED);
            case FOLIAGE -> Component.text("Foliage").color(NamedTextColor.DARK_GREEN);
        };
    }

    public static BrushType getBrushType(String brushName) {
        return switch (brushName.toLowerCase()) {
            case "ball" -> BALL;
            case "erase" -> ERASE;
            case "smooth" -> SMOOTH;
            case "erode" -> ERODE;
            case "extrude" -> EXTRUDE;
            case "painttop" -> PAINT_TOP;
            case "paintsurface" -> PAINT_SURFACE;
            case "paintwall" -> PAINT_WALL;
            case "paintbottom" -> PAINT_BOTTOM;
            case "rise" -> RISE;
            case "dig" -> DIG;
            case "foliage" -> FOLIAGE;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case BALL -> "ball";
            case ERASE -> "erase";
            case SMOOTH -> "smooth";
            case ERODE -> "erode";
            case EXTRUDE -> "extrude";
            case PAINT_TOP -> "painttop";
            case PAINT_SURFACE -> "paintsurface";
            case PAINT_WALL -> "paintwall";
            case PAINT_BOTTOM -> "paintbottom";
            case RISE -> "rise";
            case DIG -> "dig";
            case FOLIAGE -> "foliage";
        };
    }

    public static ItemStack getBrushSettingsItem(BrushType brushType) {
        ItemStack item = switch (brushType) {
            case BALL -> new ItemStack(Material.SLIME_BALL);
            case ERASE -> new ItemStack(Material.WHITE_DYE);
            case SMOOTH -> new ItemStack(Material.SNOWBALL);
            case ERODE -> new ItemStack(Material.WIND_CHARGE);
            case EXTRUDE -> new ItemStack(Material.REDSTONE);
            case PAINT_TOP -> new ItemStack(Material.PLAYER_HEAD);
            case PAINT_SURFACE -> new ItemStack(Material.PLAYER_HEAD);
            case PAINT_WALL -> new ItemStack(Material.PLAYER_HEAD);
            case PAINT_BOTTOM -> new ItemStack(Material.PLAYER_HEAD);
            case RISE -> new ItemStack(Material.GUNPOWDER);
            case DIG -> new ItemStack(Material.FIRE_CHARGE);
            case FOLIAGE -> new ItemStack(Material.OAK_LEAVES);
        };

        ItemMeta meta = item.getItemMeta();
        meta.customName(brushType.getName());

        boolean hasMaterialSettings = List
                .of(BrushType.BALL, BrushType.PAINT_TOP, BrushType.PAINT_BOTTOM, BrushType.PAINT_WALL,
                        BrushType.PAINT_SURFACE)
                .contains(brushType);
        boolean hasMaskSettings = List
                .of(BrushType.BALL, BrushType.ERASE, BrushType.RISE, BrushType.DIG, BrushType.PAINT_TOP,
                        BrushType.PAINT_BOTTOM, BrushType.PAINT_WALL, BrushType.PAINT_SURFACE)
                .contains(brushType);
        meta.lore(List.of(
                Component.text("Set the brush type to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(brushType.getName().color(NamedTextColor.LIGHT_PURPLE)),
                Component.text("Materials ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(Component
                                .text(hasMaterialSettings ? "affect this brush"
                                        : "do not affect this brush")
                                .color(hasMaterialSettings ? NamedTextColor.GREEN : NamedTextColor.RED)),
                Component.text("Mask ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(Component
                                .text(hasMaskSettings ? "affects this brush" : "do not affect this brush")
                                .color(hasMaskSettings ? NamedTextColor.GREEN : NamedTextColor.RED)),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));

        switch (brushType) {
            case PAINT_TOP:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/cc5b9542ca46b00235d3dddada02993bc4d2f7e63a5bf45b04ae6e7259c73e48");
                break;
            case PAINT_WALL:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/18660691d1ca029f120a3ff0eabab93a2306b37a7d61119fcd141ff2f6fcd798");
                break;
            case PAINT_BOTTOM:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/960a5ab0eb3eaf4e276b8f763ee47d241c4af0091cc1b045d994cd511417af7c");
                break;
            case PAINT_SURFACE:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/44f7bc1fa8217b18b323af841372a3f7c602a435c828faa403d176c6b37b605b");
                break;
            default:
                break;
        }

        item.setItemMeta(meta);
        return item;
    }

    public static void applyBrush(Terraformer plugin, Player player, BrushProperties properties,
            Location targetLocation, boolean isRedo) {
        TerraformerProperties terraformerProperties = plugin.getTerraformer(player);

        boolean applied = switch (properties.Type) {
            case BALL -> BrushBall.brush(plugin, player, properties, targetLocation, isRedo);
            case ERASE -> BrushErase.brush(plugin, player, properties, targetLocation, isRedo);
            case SMOOTH -> BrushSmooth.brush(plugin, player, properties, targetLocation, isRedo);
            case ERODE -> BrushErode.brush(plugin, player, properties, targetLocation, isRedo);
            case EXTRUDE -> BrushExtrude.brush(plugin, player, properties, targetLocation, isRedo);
            case PAINT_TOP -> BrushPaint.brush(plugin, player, properties, targetLocation, isRedo);
            case PAINT_SURFACE -> BrushPaint.brush(plugin, player, properties, targetLocation, isRedo);
            case PAINT_WALL -> BrushPaint.brush(plugin, player, properties, targetLocation, isRedo);
            case PAINT_BOTTOM -> BrushPaint.brush(plugin, player, properties, targetLocation, isRedo);
            case RISE -> BrushRise.brush(plugin, player, properties, targetLocation, isRedo);
            case DIG -> BrushDig.brush(plugin, player, properties, targetLocation, isRedo);
            case FOLIAGE -> BrushFoliage.brush(plugin, player, properties, targetLocation, isRedo);
        };

        if (applied) {
            terraformerProperties.addBrushHistory(properties.clone());
        }
    }

    public void openBrushSettings(Terraformer plugin, Player player, TerraformerProperties properties) {
        BrushSettings settings = switch (this) {
            case BALL -> new BrushSettings(plugin, properties, false);
            case ERASE -> new BrushSettings(plugin, properties, false);
            case SMOOTH -> new BrushSettings(plugin, properties, false);
            case ERODE -> new BrushSettings(plugin, properties, false);
            case EXTRUDE -> new BrushSettings(plugin, properties, false);
            case PAINT_TOP -> new BrushSettings(plugin, properties, true);
            case PAINT_SURFACE -> new BrushSettings(plugin, properties, true);
            case PAINT_WALL -> new BrushSettings(plugin, properties, true);
            case PAINT_BOTTOM -> new BrushSettings(plugin, properties, true);
            case RISE -> new BrushSettings(plugin, properties, true);
            case DIG -> new BrushSettings(plugin, properties, true);
            case FOLIAGE -> new BrushSettings(plugin, properties, false);
        };

        player.openInventory(settings.getInventory());
    }

    public void openMaterialSettings(Terraformer plugin, Player player, TerraformerProperties properties) {
        MaterialSettings settings = switch (this) {
            case BALL -> new MaterialSettings(plugin, properties, true);
            case ERASE -> new MaterialSettings(plugin, properties, false);
            case SMOOTH -> new MaterialSettings(plugin, properties, false);
            case ERODE -> new MaterialSettings(plugin, properties, false);
            case EXTRUDE -> new MaterialSettings(plugin, properties, false);
            case PAINT_TOP -> new MaterialSettings(plugin, properties, true);
            case PAINT_SURFACE -> new MaterialSettings(plugin, properties, true);
            case PAINT_WALL -> new MaterialSettings(plugin, properties, true);
            case PAINT_BOTTOM -> new MaterialSettings(plugin, properties, true);
            case RISE -> new MaterialSettings(plugin, properties, false);
            case DIG -> new MaterialSettings(plugin, properties, false);
            case FOLIAGE -> new MaterialSettings(plugin, properties, true);
        };

        player.openInventory(settings.getInventory());
    }

    public void openMaterialSettings(Terraformer plugin, Player player, TerraformerProperties properties,
            int currentMaterialPage) {
        MaterialSettings settings = switch (this) {
            case BALL -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
            case ERASE -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case SMOOTH -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case ERODE -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case EXTRUDE -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case PAINT_TOP -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
            case PAINT_SURFACE -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
            case PAINT_WALL -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
            case PAINT_BOTTOM -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
            case RISE -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case DIG -> new MaterialSettings(plugin, properties, false, currentMaterialPage);
            case FOLIAGE -> new MaterialSettings(plugin, properties, true, currentMaterialPage);
        };

        player.openInventory(settings.getInventory());
    }
}

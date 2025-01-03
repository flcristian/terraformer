package io.papermc.terraformer.terraformer_properties.properties.brushes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BrushType {
    BALL, SMOOTH, ERODE, EXTRUDE, PAINT, RISE, DIG;

    public Component getName() {
        return switch (this) {
            case BALL -> Component.text("Ball").color(NamedTextColor.GREEN);
            case SMOOTH -> Component.text("Smooth").color(NamedTextColor.AQUA);
            case ERODE -> Component.text("Erode").color(NamedTextColor.RED);
            case EXTRUDE -> Component.text("Extrude").color(NamedTextColor.LIGHT_PURPLE);
            case PAINT -> Component.text("Paint").color(NamedTextColor.BLUE);
            case RISE -> Component.text("Rise").color(NamedTextColor.DARK_PURPLE);
            case DIG -> Component.text("Dig").color(NamedTextColor.DARK_RED);
        };
    }

    public static BrushType getBrushType(String brushName) {
        return switch (brushName.toLowerCase()) {
            case "ball" -> BALL;
            case "smooth" -> SMOOTH;
            case "erode" -> ERODE;
            case "extrude" -> EXTRUDE;
            default -> null;
        };
    }

    public static ItemStack getBrushSettingsItem(BrushType brushType) {
        ItemStack item = switch (brushType) {
            case BALL -> new ItemStack(Material.SLIME_BALL);
            case SMOOTH -> new ItemStack(Material.SNOWBALL);
            case ERODE -> new ItemStack(Material.WIND_CHARGE);
            case EXTRUDE -> new ItemStack(Material.REDSTONE);
            case PAINT -> new ItemStack(Material.BLUE_DYE);
            case RISE -> new ItemStack(Material.GUNPOWDER);
            case DIG -> new ItemStack(Material.FIRE_CHARGE);
        };

        ItemMeta meta = item.getItemMeta();
        meta.customName(brushType.getName());
        meta.lore(List.of(
                Component.text("Set the brush size to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(brushType.getName().color(NamedTextColor.LIGHT_PURPLE)),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));
        item.setItemMeta(meta);
        return item;
    }

    public static void applyBrush(Terraformer plugin, Player player, BrushProperties properties,
            Location targetLocation, boolean isRedo) {
        switch (properties.Type) {
            case BALL -> BrushBall.brush(plugin, player, properties, targetLocation, isRedo);
            case SMOOTH -> BrushSmooth.brush(plugin, player, properties, targetLocation, isRedo);
            case ERODE -> BrushErode.brush(plugin, player, properties, targetLocation, isRedo);
            case EXTRUDE -> BrushExtrude.brush(plugin, player, properties, targetLocation, isRedo);
        }
    }

    public void openBrushSettings(Terraformer plugin, Player player, TerraformerProperties properties) {
        BrushSettings settings = switch (this) {
            case BALL -> new BrushSettings(plugin, properties, true, false);
            case SMOOTH -> new BrushSettings(plugin, properties, false, false);
            case ERODE -> new BrushSettings(plugin, properties, false, false);
            case EXTRUDE -> new BrushSettings(plugin, properties, false, false);
            case PAINT -> new BrushSettings(plugin, properties, true, true);
            case RISE -> new BrushSettings(plugin, properties, false, true);
            case DIG -> new BrushSettings(plugin, properties, false, true);
        };

        player.openInventory(settings.getInventory());
    }
}

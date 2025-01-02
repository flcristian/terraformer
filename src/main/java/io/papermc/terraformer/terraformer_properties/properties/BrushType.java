package io.papermc.terraformer.terraformer_properties.properties;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.terraformer.Terraformer;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushBallSettings;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushErodeSettings;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushExtrudeSettings;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushSettings;
import io.papermc.terraformer.terraformer_properties.properties.brush_settings.BrushSmoothSettings;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushBall;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushErode;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushExtrude;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushSmooth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BrushType {
    BALL, SMOOTH, ERODE, EXTRUDE;

    public Component getName() {
        return switch (this) {
            case BALL -> Component.text("Ball").color(NamedTextColor.GREEN);
            case SMOOTH -> Component.text("Smooth").color(NamedTextColor.AQUA);
            case ERODE -> Component.text("Erode").color(NamedTextColor.RED);
            case EXTRUDE -> Component.text("Extrude").color(NamedTextColor.LIGHT_PURPLE);
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
        };

        ItemMeta meta = item.getItemMeta();
        meta.customName(brushType.getName());
        meta.lore(List.of(
                Component.text("Set the brush size to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(brushType.getName()),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));
        item.setItemMeta(meta);
        return item;
    }

    public void applyBrush(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        switch (this) {
            case BALL -> BrushBall.brush(properties, targetLocation, isRedo);
            case SMOOTH -> BrushSmooth.brush(properties, targetLocation, isRedo);
            case ERODE -> BrushErode.brush(properties, targetLocation, isRedo);
            case EXTRUDE -> BrushExtrude.brush(properties, targetLocation, isRedo);
        }
    }

    public void openBrushSettings(Terraformer plugin, Player player, int brushSize) {
        BrushSettings settings = switch (this) {
            case BALL -> new BrushBallSettings(plugin, this, brushSize);
            case SMOOTH -> new BrushSmoothSettings(plugin, this, brushSize);
            case ERODE -> new BrushErodeSettings(plugin, this, brushSize);
            case EXTRUDE -> new BrushExtrudeSettings(plugin, this, brushSize);
        };

        player.openInventory(settings.getInventory());
    }
}

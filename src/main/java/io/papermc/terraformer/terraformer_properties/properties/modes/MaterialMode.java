package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;
import io.papermc.terraformer.utility.SkullTexturesApplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum MaterialMode {
    RANDOM, LAYER, GRADIENT;

    public Component getName() {
        return switch (this) {
            case RANDOM -> Component.text("Random").color(TextColor.color(196, 39, 107));
            case LAYER -> Component.text("Layer").color(TextColor.color(59, 161, 235));
            case GRADIENT -> Component.text("Gradient").color(TextColor.color(247, 132, 74));
        };
    }

    public static MaterialMode getMaterialMode(String materialMode) {
        return switch (materialMode.toLowerCase()) {
            case "random" -> RANDOM;
            case "layer" -> LAYER;
            case "gradient" -> GRADIENT;
            default -> null;
        };
    }

    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        return switch (this) {
            case RANDOM -> RandomModeSingleton.getInstance().getMaterial(location, targetLocation, properties);
            case LAYER -> LayerModeSingleton.getInstance().getMaterial(location, targetLocation, properties);
            case GRADIENT -> GradientModeSingleton.getInstance().getMaterial(location, targetLocation, properties);
        };
    }

    public static ItemStack getBrushSettingsItem(MaterialMode materialMode) {
        ItemStack item = switch (materialMode) {
            case RANDOM -> new ItemStack(Material.PLAYER_HEAD);
            case LAYER -> new ItemStack(Material.PLAYER_HEAD);
            case GRADIENT -> new ItemStack(Material.PLAYER_HEAD);
        };

        ItemMeta meta = item.getItemMeta();
        meta.customName(materialMode.getName());
        meta.lore(List.of(
                Component.text("Set the material mode to ").color(NamedTextColor.LIGHT_PURPLE)
                        .append(materialMode.getName().color(NamedTextColor.LIGHT_PURPLE)),
                Component.text("Click to select").color(NamedTextColor.LIGHT_PURPLE)));

        switch (materialMode) {
            case RANDOM:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/91c581a8b597692b5b94d3b8beb9c52f56999d62f7395668fac57ac952fe4dc4");
                break;
            case LAYER:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/9818dde7bf4fc9f3d9df0592b07ca959a945e8f712c3bf67998af155ec3bd83b");
                break;
            case GRADIENT:
                SkullTexturesApplier.applyTextures(meta,
                        "http://textures.minecraft.net/texture/3fe1ce1fceafff25cddaf6772a975afe13d23f1bbb84bc62f0b7052310ce658a");
                break;
        }

        item.setItemMeta(meta);
        return item;
    }
}

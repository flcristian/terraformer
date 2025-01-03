package io.papermc.terraformer.terraformer_properties.properties;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.modes.GradientModeSingleton;
import io.papermc.terraformer.terraformer_properties.properties.modes.LayerModeSingleton;
import io.papermc.terraformer.terraformer_properties.properties.modes.RandomModeSingleton;
import net.kyori.adventure.text.Component;
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

    public boolean containsAllMaterials(BrushProperties properties) {
        return switch (this) {
            case RANDOM -> RandomModeSingleton.getInstance().containsAllMaterials(properties);
            case LAYER -> LayerModeSingleton.getInstance().containsAllMaterials(properties);
            case GRADIENT -> GradientModeSingleton.getInstance().containsAllMaterials(properties);
        };
    }
}

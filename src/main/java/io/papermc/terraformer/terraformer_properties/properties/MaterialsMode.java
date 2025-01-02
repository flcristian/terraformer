package io.papermc.terraformer.terraformer_properties.properties;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.modes.GradientMode;
import io.papermc.terraformer.terraformer_properties.properties.modes.LayerMode;
import io.papermc.terraformer.terraformer_properties.properties.modes.RandomMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public enum MaterialsMode {
    RANDOM, LAYER, GRADIENT;

    public Component getName() {
        return switch (this) {
            case RANDOM -> Component.text("Random").color(TextColor.color(196, 39, 107));
            case LAYER -> Component.text("Layer").color(TextColor.color(59, 161, 235));
            case GRADIENT -> Component.text("Gradient").color(TextColor.color(247, 132, 74));
        };
    }

    public static MaterialsMode getMaterialsMode(String materialsMode) {
        return switch (materialsMode.toLowerCase()) {
            case "random" -> RANDOM;
            case "layer" -> LAYER;
            case "gradient" -> GRADIENT;
            default -> null;
        };
    }

    public Material getMaterial(Location location, Location targetLocation, BrushProperties properties) {
        return switch (this) {
            case RANDOM -> new RandomMode().getMaterial(location, targetLocation, properties);
            case LAYER -> new LayerMode().getMaterial(location, targetLocation, properties);
            case GRADIENT -> new GradientMode().getMaterial(location, targetLocation, properties);
        };
    }
}

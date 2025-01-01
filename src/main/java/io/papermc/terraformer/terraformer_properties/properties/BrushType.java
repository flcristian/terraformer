package io.papermc.terraformer.terraformer_properties.properties;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BrushType {
    BALL, SMOOTH;

    public Component getName() {
        return switch (this) {
            case BALL -> Component.text("Ball").color(NamedTextColor.GREEN);
            case SMOOTH -> Component.text("Smooth").color(NamedTextColor.AQUA);
        };
    }

    public static BrushType getBrushType(String brushName) {
        return switch (brushName.toLowerCase()) {
            case "ball" -> BALL;
            case "smooth" -> SMOOTH;
            default -> null;
        };
    }
}

package io.papermc.terraformer.terraformer_properties.properties;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BrushType {
    BALL;

    public Component getName() {
        return switch (this) {
            case BALL -> Component.text("Ball").color(NamedTextColor.GREEN);
        };
    }
}

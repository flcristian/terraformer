package io.papermc.terraformer.constants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BrushSettingsItems {
    public static final Component SETTINGS_BRUSH_SIZE(int brushSize) {
        return Component.text("Brush Size - " + brushSize).color(NamedTextColor.GOLD);
    }
}

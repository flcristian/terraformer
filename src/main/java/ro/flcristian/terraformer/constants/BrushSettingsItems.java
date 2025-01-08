package ro.flcristian.terraformer.constants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BrushSettingsItems {
    private BrushSettingsItems() {}

    public static Component SETTINGS_BRUSH_SIZE(int brushSize) {
        return Component.text("Brush Size - " + brushSize).color(NamedTextColor.GOLD);
    }

    public static Component SETTINGS_BRUSH_DEPTH(int brushDepth) {
        return Component.text("Brush Depth - " + brushDepth).color(NamedTextColor.YELLOW);
    }
}

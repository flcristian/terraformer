package ro.flcristian.terraformer.constants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TerraformItems {
        private TerraformItems() {
        }

        public static final Component TERRAFORMER_BRUSH = Component.text("Brush").color(NamedTextColor.AQUA);
        public static final Component TERRAFORMER_UNDO = Component.text("Undo").color(NamedTextColor.LIGHT_PURPLE);
        public static final Component TERRAFORMER_REDO = Component.text("Redo").color(NamedTextColor.GREEN);
        public static final Component TERRAFORMER_LEAVE = Component.text("Leave Terraform Mode")
                        .color(NamedTextColor.DARK_RED);
        public static final Component TERRAFORMER_BRUSH_SETTINGS = Component.text("Open Brush Settings")
                        .color(NamedTextColor.DARK_AQUA);
        public static final Component TERRAFORMER_MATERIAL_SETTINGS = Component.text("Open Material Settings")
                        .color(NamedTextColor.DARK_RED);

        public static Component[] values() {
                return new Component[] { TERRAFORMER_BRUSH, TERRAFORMER_UNDO, TERRAFORMER_REDO, TERRAFORMER_LEAVE,
                                TERRAFORMER_MATERIAL_SETTINGS };
        }
}

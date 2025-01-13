package ro.flcristian.terraformer.constants;

import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Messages {
        private Messages() {
        }

        // ERRORS

        public static final Component SENDER_NOT_PLAYER = Component.text("This command can only be used by players!")
                        .color(NamedTextColor.DARK_RED);
        public static final Component UNKNOWN_COMMAND = Component
                        .text("Unknown subcommand! Use /terraform help for a list of commands.")
                        .color(NamedTextColor.RED);
        public static final Component NO_PERMISSION = Component.text("You don't have permission to use this command!")
                        .color(NamedTextColor.DARK_RED);

        public static final Component INVALID_BRUSH_TYPE = Component.text("Invalid brush type.")
                        .color(NamedTextColor.RED);
        public static final Component INVALID_BRUSH_SIZE = Component
                        .text("Invalid brush size. Size must be between 1 and 9.").color(NamedTextColor.RED);
        public static final Component INVALID_BRUSH_DEPTH = Component
                        .text("Invalid brush depth. depth must be between 1 and 20.").color(NamedTextColor.RED);
        public static final Component INVALID_MATERIAL_MODE = Component.text("Invalid material mode.")
                        .color(NamedTextColor.RED);

        public static final Component NOTHING_TO_UNDO = Component.text("Nothing to undo!")
                        .color(NamedTextColor.RED);
        public static final Component NOTHING_TO_REDO = Component.text("Nothing to redo!")
                        .color(NamedTextColor.RED);

        public static final Component USAGE_MATERIALS = Component
                        .text("Usage: '/terraform materials <materials>' or '/terraform m <materials>'")
                        .color(NamedTextColor.RED);
        public static final Component USAGE_MATERIAL_MODE = Component
                        .text("Usage: '/terraform materialmode <material mode>' or '/terraform mm <material mode>'. All material modes: Random, Layer, Gradient")
                        .color(NamedTextColor.RED);
        public static final Component USAGE_BRUSH = Component
                        .text("Usage: '/terraform brush <brush>' or '/terraform b <brush>'")
                        .color(NamedTextColor.RED);
        public static final Component USAGE_BRUSH_SIZE = Component
                        .text("Usage: '/terraform size <size>' or '/terraform s <size>'")
                        .color(NamedTextColor.RED);
        public static final Component USAGE_BRUSH_DEPTH = Component
                        .text("Usage: '/terraform depth <d>' or '/terraform d <depth>'")
                        .color(NamedTextColor.RED);
        public static final Component USAGE_MASK = Component
                        .text("Usage: '/terraform mask <mask>' or '/terraform mk <mask>'")
                        .color(NamedTextColor.RED);

        // WARNINGS

        public static final Component TERRAFORM_MODE_NECESSARY = Component.text("You must be in terraforming mode!")
                        .color(NamedTextColor.YELLOW);
        public static final Component TERRAFORM_MODE_ALREADY_STARTED = Component
                        .text("You are already in terraforming mode!")
                        .color(NamedTextColor.YELLOW);
        public static final Component NOT_ALL_MATERIALS_APPEAR = Component
                        .text("Warning! For the specified brush settings, not all materials will appear!")
                        .color(NamedTextColor.YELLOW);
        public static final Component EXTREME_BRUSH_SIZE = Component.text(
                        "WARNING! The specified brush size is very large, be careful how you use this, you server will crash if the size is too big!")
                        .color(NamedTextColor.GOLD);
        public static final Component EXTREME_BRUSH_DEPTH = Component.text(
                        "WARNING! The specified brush depth is very large, be careful how you use this, you server will crash if the depth is too big!")
                        .color(NamedTextColor.GOLD);

        // INFO

        public static final Component START_TERRAFORM = Component.text("Initiated terraforming mode!")
                        .color(NamedTextColor.DARK_GREEN);
        public static final Component STOP_TERRAFORM = Component.text("Left terraforming mode!")
                        .color(NamedTextColor.DARK_PURPLE);
        public static final Component UNDO_SUCCESSFUL = Component.text("Undone last modification!")
                        .color(NamedTextColor.GREEN);
        public static final Component REDO_SUCCESSFUL = Component.text("Redone last modification!")
                        .color(NamedTextColor.LIGHT_PURPLE);

        public static Component CHANGED_BRUSH_SIZE(int size) {
                return Component.text("Changed brush size to ").append(Component.text(size).color(NamedTextColor.GOLD));
        }

        public static Component CHANGED_BRUSH_DEPTH(int depth) {
                return Component.text("Changed brush depth to ")
                                .append(Component.text(depth).color(NamedTextColor.YELLOW));
        }

        public static Component CHANGED_BRUSH(BrushType brushType) {
                return Component.text("Changed brush type to ").append(brushType.getName());
        }

        public static final Component CHANGED_MATERIALS = Component.text("Changed materials list");

        public static Component CHANGED_MATERIAL_MODE(MaterialMode mode) {
                return Component.text("Changed material mode to ")
                                .append(mode.getName());
        }

        public static Component CHANGED_RANDOM_HEIGHT(boolean randomHeightFoliage) {
                return Component.text("Changed random height foliage to ")
                                .append(Component.text(randomHeightFoliage ? "enabled" : "disabled").color(
                                                randomHeightFoliage ? NamedTextColor.GREEN : NamedTextColor.RED));
        }

        public static final Component APPLIED_BRUSH_HISTORY = Component.text("Applied brush history");
        public static final Component APPLIED_MATERIAL_HISTORY = Component.text("Applied material history");

        public static final Component CLEARED_MATERIALS = Component.text("Materials reset!")
                        .color(NamedTextColor.GREEN);
        public static final Component CLEARED_MASK = Component.text("Mask materials cleared!")
                        .color(NamedTextColor.GREEN);
}

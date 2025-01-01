package io.papermc.terraformer.constants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Messages {
        public static final Component SENDER_NOT_PLAYER = Component.text("This command can only be used by players!")
                        .color(NamedTextColor.DARK_RED);
        public static final Component UNKNOWN_COMMAND = Component
                        .text("Unknown subcommand! Use /terraform help for a list of commands.")
                        .color(NamedTextColor.YELLOW);

        public static final Component NO_PERMISSION = Component.text("You don't have permission to use this command!")
                        .color(NamedTextColor.DARK_RED);
        public static final Component TERRAFORM_MODE_NECESSARY = Component.text("You must be in terraforming mode!")
                        .color(NamedTextColor.YELLOW);
        public static final Component TERRAFORM_MODE_ALREADY_STARTED = Component
                        .text("You are already in terraforming mode!")
                        .color(NamedTextColor.YELLOW);

        public static final Component START_TERRAFORM = Component.text("Initiated terraforming mode!")
                        .color(NamedTextColor.GREEN);
        public static final Component STOP_TERRAFORM = Component.text("Left terraforming mode!")
                        .color(NamedTextColor.RED);

        public static final Component UNDO_SUCCESSFUL = Component.text("Undone last modification!")
                        .color(NamedTextColor.GREEN);
        public static final Component REDO_SUCCESSFUL = Component.text("Redone last modification!")
                        .color(NamedTextColor.GREEN);
        public static final Component NOTHING_TO_UNDO = Component.text("Nothing to undo!")
                        .color(NamedTextColor.RED);
        public static final Component NOTHING_TO_REDO = Component.text("Nothing to redo!")
                        .color(NamedTextColor.RED);

        public static final Component INVALID_BRUSH_TYPE = Component.text("Invalid brush type.")
                        .color(NamedTextColor.RED);
        public static final Component INVALID_BRUSH_SIZE = Component
                        .text("Invalid brush size. Size must be between 1 and 9.").color(NamedTextColor.RED);
}

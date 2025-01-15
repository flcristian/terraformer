package ro.flcristian.terraformer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.block_history.BrushAction;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;
import ro.flcristian.terraformer.utility.MaterialObjectsParser;

class TerraformCommand implements CommandExecutor {
    private final Terraformer plugin;

    public TerraformCommand(Terraformer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.SENDER_NOT_PLAYER);
            return true;
        }

        if (args.length == 0) {
            showPluginInfo(player);
            return true;
        }

        TerraformerProperties properties = plugin.getTerraformer(player);

        switch (args[0].toLowerCase()) {
            case "help":
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                    }
                }

                page = Math.min(page, 4);

                showHelpInfo(player, page);
                return true;

            case "start":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties != null && properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_ALREADY_STARTED);
                    return true;
                }
                plugin.setTerraformer(player);
                player.sendMessage(Messages.START_TERRAFORM);
                return true;

            case "stop":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }
                plugin.removeTerraformer(player);
                player.sendMessage(Messages.STOP_TERRAFORM);
                return true;

            case "undo":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }
                BlockHistoryStates undoStates = properties.History.undo();
                if (undoStates == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.NOTHING_TO_UNDO);
                    return true;
                }
                plugin.undo(undoStates.states());
                player.sendMessage(Messages.UNDO_SUCCESSFUL);
                return true;

            case "redo":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }
                BrushAction redoAction = properties.History.redo();
                if (redoAction == null) {
                    player.sendMessage(Messages.NOTHING_TO_REDO);
                    return true;
                }
                properties.applyRedo(plugin, player, redoAction);
                player.sendMessage(Messages.REDO_SUCCESSFUL);
                return true;

            case "brush", "b":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_BRUSH);
                    return true;
                }
                BrushType brushType = BrushType.getBrushType(args[1]);
                if (brushType == null) {
                    player.sendMessage(Messages.INVALID_BRUSH_TYPE);
                    return true;
                }

                BrushType oldBrushType = properties.Brush.Type;
                properties.Brush.Type = brushType;
                if (oldBrushType != BrushType.FOLIAGE && brushType == BrushType.FOLIAGE) {
                    properties.Brush.setMode(MaterialMode.RANDOM);
                } else if (oldBrushType == BrushType.FOLIAGE && brushType != BrushType.FOLIAGE) {
                    properties.Brush.setMode(properties.Brush.Mode);
                }

                player.sendMessage(Messages.CHANGED_BRUSH(brushType));
                return true;

            case "brushes":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                Component brushes = Component.text("All brush types: ").appendNewline();
                for (BrushType brush : BrushType.values()) {
                    brushes = brushes.append(Component.text(brush.toString()
                            + (brush != BrushType.values()[BrushType.values().length - 1] ? ", " : "")));
                }
                brushes = brushes.color(NamedTextColor.LIGHT_PURPLE);

                player.sendMessage(brushes);

                return true;

            case "size", "s":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_BRUSH_SIZE);
                    return true;
                }

                int size;
                try {
                    size = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Messages.INVALID_BRUSH_SIZE);
                    return true;
                }
                if (size < 1) {
                    player.sendMessage(Messages.INVALID_BRUSH_SIZE);
                    return true;
                }

                if (size > 9) {
                    boolean hasForceFlag = args.length > 2 && args[2].equalsIgnoreCase("-f");
                    if (!hasForceFlag) {
                        player.sendMessage(Messages.INVALID_BRUSH_SIZE);
                        return true;
                    }
                    player.sendMessage(Messages.EXTREME_BRUSH_SIZE);
                }
                properties.Brush.BrushSize = size;
                player.sendMessage(Messages.CHANGED_BRUSH_SIZE(size));
                return true;

            case "depth", "d":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_BRUSH_DEPTH);
                    return true;
                }

                int depth;
                try {
                    depth = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Messages.INVALID_BRUSH_DEPTH);
                    return true;
                }

                if (depth < 1) {
                    player.sendMessage(Messages.INVALID_BRUSH_DEPTH);
                    return true;
                }

                if (depth > 20) {
                    boolean hasForceFlag = args.length > 2 && args[2].equalsIgnoreCase("-f");
                    if (!hasForceFlag) {
                        player.sendMessage(Messages.INVALID_BRUSH_DEPTH);
                        return true;
                    }
                    player.sendMessage(Messages.EXTREME_BRUSH_DEPTH);
                }
                properties.Brush.BrushDepth = depth;
                player.sendMessage(Messages.CHANGED_BRUSH_DEPTH(depth));
                return true;

            case "materials", "m":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    properties.Brush.setMode(properties.Brush.Mode);
                    player.sendMessage(Messages.CLEARED_MATERIALS);
                    return true;
                }

                if (args.length > 2) {
                    player.sendMessage(Messages.USAGE_MATERIALS);
                    return true;
                }

                try {
                    StringBuilder materialsString = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        materialsString.append(args[i]);
                    }
                    properties.Brush.Materials = MaterialObjectsParser.parseMaterialPercentages(
                            properties.Brush.Type,
                            materialsString.toString(),
                            properties.Brush.Mode);
                    player.sendMessage(Component.text("Materials updated successfully!").color(NamedTextColor.GREEN));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
                }
                return true;

            case "materialmode", "mm":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_MATERIAL_MODE);
                    return true;
                }
                MaterialMode materialMode = MaterialMode.getMaterialMode(args[1]);
                if (materialMode == null) {
                    player.sendMessage(Messages.INVALID_MATERIAL_MODE);
                    return true;
                }

                properties.Brush.setMode(materialMode);

                player.sendMessage(Messages.CHANGED_MATERIAL_MODE(materialMode));
                return true;

            case "materialmodes":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                Component modes = Component.text("All brush types: ").appendNewline();
                for (MaterialMode mode : MaterialMode.values()) {
                    modes = modes.append(Component.text(mode.toString()
                            + (mode != MaterialMode.values()[MaterialMode.values().length - 1] ? ", " : "")));
                }
                modes = modes.color(NamedTextColor.LIGHT_PURPLE);

                player.sendMessage(modes);

                return true;

            case "mask", "mk":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    properties.Brush.Mask = new ArrayList<>();
                    player.sendMessage(Messages.CLEARED_MASK);
                    return true;
                }

                if (args.length > 2) {
                    player.sendMessage(Messages.USAGE_MASK);
                    return true;
                }

                try {
                    StringBuilder maskString = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        maskString.append(args[i]);
                    }
                    properties.Brush.Mask = MaterialObjectsParser.parseMaskMaterials(maskString.toString());
                    player.sendMessage(Component.text("Mask materials updated successfully!")
                            .color(NamedTextColor.GREEN));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
                }
                return true;
            case "randomheight", "rh":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                properties.Brush.RandomHeightFoliage = !properties.Brush.RandomHeightFoliage;
                player.sendMessage(Messages.CHANGED_RANDOM_HEIGHT(properties.Brush.RandomHeightFoliage));
                return true;
            case "schematic", "schem":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_SCHEMATIC);
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "list", "li":
                        File schematicsFolder = new File(plugin.getDataFolder(), "Schematics");
                        if (!schematicsFolder.exists() || !schematicsFolder.isDirectory()) {
                            player.sendMessage(
                                    Component.text("Schematics folder not found!").color(NamedTextColor.RED));
                            return true;
                        }

                        File[] schemFiles = schematicsFolder
                                .listFiles((dir, name) -> name.toLowerCase().endsWith(".schem")
                                        || name.toLowerCase().endsWith(".schematic"));

                        if (schemFiles == null || schemFiles.length == 0) {
                            player.sendMessage(Component.text("No schematics found!").color(NamedTextColor.YELLOW));
                            return true;
                        }

                        Component message = Component.text("Available schematics:").color(NamedTextColor.GREEN)
                                .appendNewline();
                        for (int i = 0; i < schemFiles.length; i++) {
                            message = message.append(Component.text("- " + schemFiles[i].getName())
                                    .color(NamedTextColor.GRAY));
                            if (i != schemFiles.length - 1) {
                                message = message.appendNewline();
                            }
                        }
                        player.sendMessage(message);
                        return true;

                    case "load", "ld":
                        if (args.length < 3) {
                            player.sendMessage(Component.text("Usage: /terraform schematic load <filename>")
                                    .color(NamedTextColor.RED));
                            return true;
                        }

                        String fileName = args[2];
                        File schematicsDir = new File(plugin.getDataFolder(), "Schematics");
                        File schematicFile = new File(schematicsDir, fileName);

                        // Try with .schem extension if file doesn't exist
                        if (!schematicFile.exists()) {
                            schematicFile = new File(schematicsDir, fileName + ".schem");
                        }

                        // Try with .schematic extension if still doesn't exist
                        if (!schematicFile.exists()) {
                            schematicFile = new File(schematicsDir, fileName + ".schematic");
                        }

                        if (!schematicFile.exists()) {
                            player.sendMessage(Component.text("Schematic file not found: " + fileName)
                                    .color(NamedTextColor.RED));
                            return true;
                        }

                        properties.Brush.loadedSchematic = schematicFile;
                        player.sendMessage(Component.text("Loaded schematic: " + schematicFile.getName())
                                .color(NamedTextColor.GREEN));
                        return true;

                    default:
                        player.sendMessage(Component.text("Invalid schematic command. Use 'list' or 'load'.")
                                .color(NamedTextColor.RED));
                        return true;
                }
            default:
                player.sendMessage(Messages.UNKNOWN_COMMAND);
                return true;
        }
    }

    @SuppressWarnings("deprecation")
    private void showPluginInfo(Player player) {
        Component message = Component.text()
                .append(Component.text("=-=-=-=-=-=-=-=-=-="))
                .appendNewline()
                .append(Component.text(plugin.getDescription().getName())
                        .color(NamedTextColor.AQUA)
                        .appendNewline()
                        .append(Component.text("Version: ")
                                .color(NamedTextColor.GRAY))
                        .append(Component.text(plugin.getDescription().getVersion())
                                .color(NamedTextColor.WHITE))
                        .appendNewline()
                        .append(Component.text("Created by: ")
                                .color(NamedTextColor.GRAY))
                        .append(Component.text(plugin.getDescription().getAuthors().get(0))
                                .color(NamedTextColor.WHITE)))
                .appendNewline()
                .append(Component.text("=-=-=-=-=-=-=-=-=-="))
                .build();

        player.sendMessage(message);
    }

    private void showHelpInfo(Player player, int page) {
        Map<String, Component> commands = new LinkedHashMap<>();
        commands.put("help", Component.text("/terraform help ", NamedTextColor.YELLOW)
                .append(Component.text("(page) - Show help information for terraform command", NamedTextColor.WHITE)));
        commands.put("start", Component.text("/terraform start ", NamedTextColor.YELLOW)
                .append(Component.text("- Start terraforming mode", NamedTextColor.WHITE)));
        commands.put("stop", Component.text("/terraform stop ", NamedTextColor.YELLOW)
                .append(Component.text("- Stop terraforming mode", NamedTextColor.WHITE)));
        commands.put("undo", Component.text("/terraform undo ", NamedTextColor.YELLOW)
                .append(Component.text("- Undo last modification", NamedTextColor.WHITE)));
        commands.put("redo", Component.text("/terraform redo ", NamedTextColor.YELLOW)
                .append(Component.text("- Redo last modification", NamedTextColor.WHITE)));
        commands.put("brushes", Component.text("/terraform brushes ", NamedTextColor.YELLOW)
                .append(Component.text("- Show all brush types", NamedTextColor.WHITE)));
        commands.put("brush", Component.text("/terraform brush <brush> ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming brush type.", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf b <brush>", NamedTextColor.YELLOW)));
        commands.put("size", Component.text("/terraform size <size> ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming brush size.", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf s <size>", NamedTextColor.YELLOW)));
        commands.put("depth", Component.text("/terraform depth <depth> ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming brush size.", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf d <depth>", NamedTextColor.YELLOW)));
        commands.put("materials", Component.text("/terraform materials <materials> ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming materials.", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf m <materials>", NamedTextColor.YELLOW)));
        commands.put("materialmode", Component.text("/terraform materialmode <material mode> ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming material mode.", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf mm <material mode>", NamedTextColor.YELLOW)));
        commands.put("materialmodes", Component.text("/terraform materialmodes ", NamedTextColor.YELLOW)
                .append(Component.text("- Show all material modes", NamedTextColor.WHITE)));
        commands.put("mask", Component.text("/terraform mask ", NamedTextColor.YELLOW)
                .append(Component.text("- Set terraforming mask blocks", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf mk <mask>", NamedTextColor.YELLOW)));
        commands.put("randomheight", Component.text("/terraform randomheight ", NamedTextColor.YELLOW)
                .append(Component.text("- Toggle random height for foliage brush", NamedTextColor.WHITE))
                .appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf rh", NamedTextColor.YELLOW)));
        commands.put("schematic", Component.text("/terraform schematic <list/load>", NamedTextColor.YELLOW)
                .append(Component.text("- List all the schematics / Load a schematic", NamedTextColor.WHITE))
                .appendNewline()
                .append(Component.text("Alias: ", NamedTextColor.WHITE))
                .append(Component.text("/tf schem <li/ld>", NamedTextColor.YELLOW))
                .appendNewline()
                .append(Component.text(
                        "- You also need to provide the schematic name, ex: /tf schem ld tree (or tree.schem)",
                        NamedTextColor.WHITE)));

        Map<Integer, Component[]> pages = new LinkedHashMap<>();
        pages.put(1, new Component[] { commands.get("help"), commands.get("start"), commands.get("stop"),
                commands.get("undo"), commands.get("redo") });
        pages.put(2, new Component[] { commands.get("brushes"), commands.get("brush"), commands.get("size"),
                commands.get("depth") });
        pages.put(3, new Component[] { commands.get("materials"), commands.get("materialmode"),
                commands.get("materialmodes") });
        pages.put(4, new Component[] { commands.get("mask"), commands.get("randomheight"), commands.get("schematic") });

        Component message = Component.text("Terraformer Help").color(NamedTextColor.AQUA)
                .append(Component.text(" - ").color(NamedTextColor.WHITE))
                .append(Component.text("Page " + page).color(NamedTextColor.GOLD))
                .appendNewline()
                .append(Component.text("=-=-=-=-=-=-=-=-=-=-=-=").color(NamedTextColor.WHITE))
                .appendNewline();

        Component content = Component.empty();

        for (Component command : pages.get(page)) {
            content = content.append(command).appendNewline();
        }

        message = message.append(content);

        player.sendMessage(message);
    }
}

package io.papermc.terraformer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import io.papermc.terraformer.terraformer_properties.block_history.BrushAction;
import io.papermc.terraformer.terraformer_properties.properties.MaterialsMode;
import io.papermc.terraformer.terraformer_properties.properties.brushes.BrushType;

class TerraformCommand implements CommandExecutor {
    private final Terraformer plugin;

    public TerraformCommand(Terraformer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            String[] args) {
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
                showHelpInfo(player);
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
                properties.Brush.applyBrush(redoAction.targetLocation(), true);
                player.sendMessage(Messages.REDO_SUCCESSFUL);
                return true;

            case "brush":
            case "b":
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
                properties.Brush = brushType;
                player.sendMessage(Messages.CHANGED_BRUSH(brushType));
                return true;

            case "size":
            case "s":
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
                if (size < 1 || size > 9) {
                    player.sendMessage(Messages.INVALID_BRUSH_SIZE);
                    return true;
                }
                properties.BrushSize = size;
                player.sendMessage(Messages.CHANGED_BRUSH_SIZE(size));
                return true;

            case "depth":
            case "d":
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
                if (depth < 1 || depth > 9) {
                    player.sendMessage(Messages.INVALID_BRUSH_DEPTH);
                    return true;
                }
                properties.BrushDepth = depth;
                player.sendMessage(Messages.CHANGED_BRUSH_DEPTH(depth));
                return true;

            case "materials":
            case "m":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_MATERIALS);
                    return true;
                }

                try {
                    StringBuilder materialsString = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        materialsString.append(args[i]);
                    }
                    properties.Materials = parseMaterialPercentages(materialsString.toString());
                    player.sendMessage(Component.text("Materials updated successfully!").color(NamedTextColor.GREEN));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
                }
                return true;

            case "materialsmode":
            case "mm":
                if (!player.hasPermission("terraformer.mode")) {
                    player.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                if (properties == null || !properties.IsTerraformer) {
                    player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(Messages.USAGE_MATERIALS_MODE);
                    return true;
                }
                MaterialsMode materialsMode = MaterialsMode.getMaterialsMode(args[1]);
                if (materialsMode == null) {
                    player.sendMessage(Messages.INVALID_MATERIALS_MODE);
                    return true;
                }
                properties.Mode = materialsMode;
                player.sendMessage(Messages.CHANGED_MATERIALS_MODE(materialsMode));
                return true;

            default:
                player.sendMessage(Messages.UNKNOWN_COMMAND);
                return true;
        }
    }

    @SuppressWarnings("deprecation")
    private void showPluginInfo(Player player) {
        Component message = Component.text()
                .append(Component.text("=-=-=-=-=-=-=-=-=-="))
                .append(Component.newline())
                .append(Component.text(plugin.getDescription().getName())
                        .color(NamedTextColor.AQUA)
                        .append(Component.newline())
                        .append(Component.text("Version: ")
                                .color(NamedTextColor.GRAY))
                        .append(Component.text(plugin.getDescription().getVersion())
                                .color(NamedTextColor.WHITE))
                        .append(Component.newline())
                        .append(Component.text("Created by: ")
                                .color(NamedTextColor.GRAY))
                        .append(Component.text(plugin.getDescription().getAuthors().get(0))
                                .color(NamedTextColor.WHITE)))
                .append(Component.newline())
                .append(Component.text("=-=-=-=-=-=-=-=-=-="))
                .build();

        player.sendMessage(message);
    }

    private void showHelpInfo(Player player) {
        Component message = Component.text()
                .append(Component.text("Terraform Command Help").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("/terraform help - Show help information for terraform command")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("/terraform start - Start terraforming mode").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("/terraform stop - Stop terraforming mode").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("/terraform undo - Undo last modification")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("/terraform redo - Redo last modification")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text(
                        "/terraform brush <brush> - Set terraforming brush type. Alias: /terraform b <brush>")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text(
                        "/terraform size <size> - Set terraforming brush size. Alias: /terraform s <size>")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text(
                        "/terraform depth <depth> - Set terraforming brush size. Alias: /terraform d <depth>")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text(
                        "/terraform materials <materials> - Set terraforming materials. Alias: /terraform m <materials>")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(
                        "/terraform materialsmode <materials mode> - Set terraforming materials mode. Alias: /terraform mm <materials mode>. All material modes: Random, Layer, Gradient")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(message);
    }

    private Map<Material, Integer> parseMaterialPercentages(String materials) {
        Map<Material, Integer> materialMap = new HashMap<>();

        // Check if using percentage format
        if (materials.contains("%")) {
            // Existing percentage parsing logic
            int totalPercentage = 0;
            String[] materialEntries = materials.split(",");
            for (String entry : materialEntries) {
                String[] parts = entry.trim().split("%");
                if (parts.length != 2) {
                    throw new IllegalArgumentException(
                            "Invalid material format. Expected format: '70%material_name1,30%material_name2'");
                }

                int percentage = Integer.parseInt(parts[0]);
                String materialName = parts[1].toUpperCase();
                Material material = Material.getMaterial(materialName);

                if (material == null) {
                    throw new IllegalArgumentException("Invalid material: " + materialName);
                }

                materialMap.put(material, percentage);
                totalPercentage += percentage;
            }

            if (totalPercentage != 100) {
                throw new IllegalArgumentException("Material percentages must add up to 100%");
            }
        } else {
            // Equal distribution logic
            String[] materialNames = materials.split(",");
            int equalPercentage = 100 / materialNames.length;
            int remainder = 100 % materialNames.length;

            for (String materialName : materialNames) {
                Material material = Material.getMaterial(materialName.trim().toUpperCase());
                if (material == null) {
                    throw new IllegalArgumentException("Invalid material: " + materialName);
                }

                // Add extra 1% to first few materials if there's a remainder
                int percentage = equalPercentage + (remainder > 0 ? 1 : 0);
                remainder--;
                materialMap.put(material, percentage);
            }
        }

        return materialMap;
    }
}

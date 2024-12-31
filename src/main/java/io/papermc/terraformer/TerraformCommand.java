package io.papermc.terraformer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockStateHistory;
import io.papermc.terraformer.terraformer_properties.block_history.BrushAction;

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

        if (args.length == 1) {
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

                    break;

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
                    break;

                case "undo":
                    if (!player.hasPermission("terraformer.mode")) {
                        player.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (properties == null || !properties.IsTerraformer) {
                        player.sendMessage(Messages.TERRAFORM_MODE_NECESSARY);
                        return true;
                    }
                    Stack<BlockStateHistory> undoStates = properties.History.undo();
                    if (undoStates == null || !properties.IsTerraformer) {
                        player.sendMessage(Messages.NOTHING_TO_UNDO);
                        return true;
                    }
                    plugin.undo(undoStates);
                    player.sendMessage(Messages.UNDO_SUCCESSFUL);
                    break;

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
                    plugin.brush(properties, redoAction.targetLocation(), true);
                    player.sendMessage(Messages.REDO_SUCCESSFUL);
                    break;

                default:
                    player.sendMessage(Messages.UNKNOWN_COMMAND);
                    break;
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("materials") || args[0].equalsIgnoreCase("mat")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("Usage: /terraform materials <materials>")
                        .color(NamedTextColor.RED));
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
                player.sendMessage(Component.text("Error: " + e.getMessage()).color(NamedTextColor.RED));
            }
            return true;
        }

        return false;
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
                .append(Component.text("/terraform materials <percentages> - Set terraforming materials")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("/terraform mat <percentages> - Short version of materials command")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("/terraform help - Show help information for terraform command")
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

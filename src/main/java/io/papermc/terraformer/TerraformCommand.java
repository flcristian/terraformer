package io.papermc.terraformer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.constants.TerraformItems;

class TerraformCommand implements CommandExecutor {
    private final Terraformer plugin;

    public TerraformCommand(Terraformer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(Messages.SENDER_NOT_PLAYER)
                    .color(NamedTextColor.RED));
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
                    if (!player.hasPermission("terraformer.start")) {
                        player.sendMessage(Component.text(Messages.NO_PERMISSION)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    if (properties != null) {
                        player.sendMessage(Component.text(Messages.TERRAFORM_MODE_ALREADY_STARTED)
                                .color(NamedTextColor.YELLOW));
                        return true;
                    }
                    plugin.setTerraformer(player, new TerraformerProperties());
                    player.getInventory().clear();
                    player.sendMessage(Component.text(Messages.START_TERRAFORM)
                            .color(NamedTextColor.GREEN));

                    // Create the brush item
                    ItemStack brush = new ItemStack(Material.BRUSH);
                    ItemMeta brushMeta = brush.getItemMeta();
                    brushMeta.customName(TerraformItems.TERRAFORMER_BRUSH);
                    brush.setItemMeta(brushMeta);
                    player.getInventory().setItem(4, brush);

                    // Create the undo item
                    ItemStack undo = new ItemStack(Material.AMETHYST_BLOCK);
                    ItemMeta undoMeta = undo.getItemMeta();
                    undoMeta.customName(TerraformItems.TERRAFORMER_UNDO);
                    undo.setItemMeta(undoMeta);
                    player.getInventory().setItem(3, undo);

                    // Create the redo item
                    ItemStack redo = new ItemStack(Material.PRISMARINE);
                    ItemMeta redoMeta = redo.getItemMeta();
                    redoMeta.customName(TerraformItems.TERRAFORMER_REDO);
                    redo.setItemMeta(redoMeta);
                    player.getInventory().setItem(5, redo);

                    break;

                case "stop":
                    if (!player.hasPermission("terraformer.stop")) {
                        player.sendMessage(Component.text(Messages.STOP_TERRAFORM)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    plugin.removeTerraformer(player);
                    player.getInventory().clear();
                    player.sendMessage(Component.text()
                            .color(NamedTextColor.RED));
                    break;

                case "undo":
                    if (!player.hasPermission("terraformer.modify")) {
                        player.sendMessage(Component.text(Messages.NO_PERMISSION)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    if (properties == null) {
                        player.sendMessage(Component.text(Messages.TERRAFORM_MODE_NECESSARY)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    Stack<BlockState> undoStates = properties.History.undo();
                    if (undoStates == null) {
                        player.sendMessage(Component.text(Messages.NOTHING_TO_UNDO)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    plugin.undo(undoStates);
                    player.sendMessage(Component.text(Messages.UNDO_SUCCESSFUL)
                            .color(NamedTextColor.GREEN));
                    break;

                case "redo":
                    if (!player.hasPermission("terraformer.modify")) {
                        player.sendMessage(Component.text(Messages.NO_PERMISSION)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    if (properties == null) {
                        player.sendMessage(Component.text(Messages.TERRAFORM_MODE_NECESSARY)
                                .color(NamedTextColor.RED));
                        return true;
                    }
                    BrushAction redoAction = properties.History.redo();
                    if (redoAction == null) {
                        player.sendMessage(Component.text(Messages.NOTHING_TO_REDO).color(NamedTextColor.RED));
                        return true;
                    }
                    plugin.brush(properties, redoAction.targetLocation(), true);
                    player.sendMessage(Component.text(Messages.REDO_SUCCESSFUL)
                            .color(NamedTextColor.GREEN));
                    break;

                default:
                    player.sendMessage(Component.text("Unknown subcommand! Use /terraform start, stop, undo, or redo")
                            .color(NamedTextColor.RED));
                    break;
            }
            return true;
        }

        return false;
    }

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
                .append(Component.text("/terraform help - Show help information for terraform command")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(message);
    }
}

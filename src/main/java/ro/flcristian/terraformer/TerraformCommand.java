package ro.flcristian.terraformer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.BookMetaBuilder;
import org.jetbrains.annotations.NotNull;

import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.block_history.BrushAction;
import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

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

                page = Math.min(page, 3);

                showHelpInfo(player, page);
                return true;

            case "tutorial", "tut":
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookMeta = (BookMeta) book.getItemMeta();
                bookMeta.setAuthor("Author");
                bookMeta.setTitle("Title");
                BookMetaBuilder bookMetaBuilder = bookMeta.toBuilder();
                bookMetaBuilder.author(Component.text("flcristian").color(NamedTextColor.DARK_RED));
                bookMetaBuilder.title(
                        Component.text("Terraformer").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD));

                Component page0 = Component.text("Welcome to Terraformer!").color(NamedTextColor.DARK_RED)
                        .appendNewline().appendNewline()
                        .append(Component.text("This plugin allows you to terraform the world around you.")
                                .color(NamedTextColor.RED))
                        .appendNewline().appendNewline()
                        .append(Component.text("GitHub").color(NamedTextColor.BLACK)
                                .decorate(TextDecoration.UNDERLINED)
                                .clickEvent(ClickEvent.openUrl("https://github.com/flcristian/terraformer"))
                                .hoverEvent(HoverEvent.showText(Component.text("Open GitHub repository"))))
                        .appendNewline().appendNewline()
                        .append(Component.text("Modrinth").color(NamedTextColor.DARK_GREEN)
                                .decorate(TextDecoration.UNDERLINED)
                                .clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/terraformer"))
                                .hoverEvent(HoverEvent.showText(Component.text("Open Modrinth page"))));
                bookMetaBuilder.addPage(page0);

                Component page1 = Component.empty()
                        .append(Component.text("Page 1").color(NamedTextColor.BLACK)
                                .decorate(TextDecoration.UNDERLINED))
                        .appendNewline().appendNewline()
                        .append(Component.text("To open ").color(NamedTextColor.BLACK))
                        .append(Component.text("BRUSH SETTINGS").color(NamedTextColor.DARK_RED))
                        .append(Component.text(", ").color(NamedTextColor.BLACK))
                        .append(Component.text("USE").color(NamedTextColor.DARK_RED))
                        .append(Component.text(" the ").color(NamedTextColor.BLACK))
                        .append(Component.text("ITEM").color(NamedTextColor.DARK_RED))
                        .append(Component.text(" in your inventory.").color(NamedTextColor.BLACK))
                        .appendNewline().appendNewline()
                        .append(Component.text("To open ").color(NamedTextColor.BLACK))
                        .append(Component.text("BRUSH MENU").color(NamedTextColor.DARK_RED))
                        .append(Component.text(", ").color(NamedTextColor.BLACK))
                        .append(Component.text("DROP").color(NamedTextColor.DARK_RED))
                        .append(Component.text(" your ").color(NamedTextColor.BLACK))
                        .append(Component.text("BRUSH").color(NamedTextColor.DARK_RED))
                        .append(Component.text(".").color(NamedTextColor.BLACK));
                bookMetaBuilder.addPage(page1);

                Component page2 = Component.empty()
                        .append(Component.text("Page 2").color(NamedTextColor.BLACK)
                                .decorate(TextDecoration.UNDERLINED))
                        .appendNewline().appendNewline()
                        .append(Component.text("For more information, use the following command: /terraform help.")
                                .color(NamedTextColor.BLACK));
                bookMetaBuilder.addPage(page2);

                book.setItemMeta(bookMetaBuilder.build());
                player.openBook(book);
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
                properties.Brush.Type = brushType;
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
                    player.sendMessage(Messages.USAGE_MATERIALS);
                    return true;
                }

                try {
                    StringBuilder materialsString = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        materialsString.append(args[i]);
                    }
                    properties.Brush.Materials = parseMaterialPercentages(materialsString.toString(),
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

                properties.Brush.Mode = materialMode;

                properties.Brush.Materials = new LinkedHashMap<>();
                switch (materialMode) {
                    case RANDOM:
                        properties.Brush.Materials.put(Material.STONE, 100);
                        break;
                    case LAYER:
                        properties.Brush.Materials.put(Material.STONE, 100);
                        break;
                    case GRADIENT:
                        properties.Brush.Materials.put(Material.WHITE_CONCRETE, 0);
                        properties.Brush.Materials.put(Material.BLACK_CONCRETE, 100);
                        break;
                }

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
        commands.put("tutorial", Component.text("/terraform tutorial ", NamedTextColor.YELLOW)
                .append(Component.text("- Show tutorial for Terraform", NamedTextColor.WHITE)));
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

        Map<Integer, Component[]> pages = new LinkedHashMap<>();
        pages.put(1, new Component[] { commands.get("help"), commands.get("start"), commands.get("stop"),
                commands.get("undo"), commands.get("redo") });
        pages.put(2, new Component[] { commands.get("brushes"), commands.get("brush"), commands.get("size"),
                commands.get("depth") });
        pages.put(3, new Component[] { commands.get("materials"), commands.get("materialmode"),
                commands.get("materialmodes") });

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

    private Map<Material, Integer> parseMaterialPercentages(String materials, MaterialMode materialMode) {
        Map<Material, Integer> materialMap = new LinkedHashMap<>();

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

            if (totalPercentage != 100 && materialMode != MaterialMode.GRADIENT) {
                throw new IllegalArgumentException("Material percentages must add up to 100%");
            }
        } else {
            // Equal distribution logic
            String[] materialNames = materials.split(",");

            if (materialMode == MaterialMode.GRADIENT) {
                int numMaterials = materialNames.length;

                if (numMaterials == 1) {
                    Material material = Material.getMaterial(materialNames[0].trim().toUpperCase());
                    if (material == null) {
                        throw new IllegalArgumentException("Invalid material: " + materialNames[0]);
                    }

                    if (!material.isSolid()) {
                        throw new IllegalArgumentException("Material must be solid: " + materialNames[0]);
                    }
                    materialMap.put(material, 50);
                } else {
                    int interval = 100 / (numMaterials - 1);
                    for (int i = 0; i < materialNames.length; i++) {
                        Material material = Material.getMaterial(materialNames[i].trim().toUpperCase());
                        if (material == null) {
                            throw new IllegalArgumentException("Invalid material: " + materialNames[i]);
                        }

                        if (!material.isSolid()) {
                            throw new IllegalArgumentException("Material must be solid: " + materialNames[i]);
                        }

                        int percentage = (i == materialNames.length - 1) ? 100 : i * interval;
                        materialMap.put(material, percentage);
                    }
                }
            } else {
                // Original equal distribution logic for non-GRADIENT mode
                int equalPercentage = 100 / materialNames.length;
                int remainder = 100 % materialNames.length;

                for (String materialName : materialNames) {
                    Material material = Material.getMaterial(materialName.trim().toUpperCase());
                    if (material == null) {
                        throw new IllegalArgumentException("Invalid material: " + materialName);
                    }

                    if (!material.isSolid()) {
                        throw new IllegalArgumentException("Material must be solid: " + materialName);
                    }

                    // Add extra 1% to first few materials if there's a remainder
                    int percentage = equalPercentage + (remainder > 0 ? 1 : 0);
                    remainder--;
                    materialMap.put(material, percentage);
                }
            }
        }

        return materialMap;
    }
}

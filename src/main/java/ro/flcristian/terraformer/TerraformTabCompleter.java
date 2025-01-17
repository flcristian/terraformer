package ro.flcristian.terraformer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.terraformer_properties.properties.brushes.BrushType;
import ro.flcristian.terraformer.terraformer_properties.properties.modes.MaterialMode;

public class TerraformTabCompleter implements TabCompleter {
    private final Terraformer plugin;

    public TerraformTabCompleter(Terraformer plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        if (!player.hasPermission("terraformer.use")) {
            return null;
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("help", "start", "stop", "undo", "redo",
                    "brush", "brushes", "size", "depth", "materials", "materialmode", "materialmodes", "mask",
                    "randomheight", "randomrotation", "schematic", "blockupdates"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "brush", "b" -> {
                    if (player.hasPermission("terraformer.mode")) {
                        completions.addAll(Arrays.stream(BrushType.values())
                                .map(type -> type.toString().toLowerCase())
                                .toList());
                    }
                }
                case "materialmode", "mm" -> {
                    if (player.hasPermission("terraformer.mode")) {
                        completions.addAll(Arrays.stream(MaterialMode.values())
                                .map(mode -> mode.toString().toLowerCase())
                                .toList());
                    }
                }
                case "help" -> completions.addAll(Arrays.asList("1", "2", "3", "4"));
                case "size", "s" -> {
                    if (player.hasPermission("terraformer.mode")) {
                        completions.addAll(IntStream.rangeClosed(1, 9)
                                .mapToObj(Integer::toString)
                                .toList());
                    }
                }
                case "depth", "d" -> {
                    if (player.hasPermission("terraformer.mode")) {
                        completions.addAll(IntStream.rangeClosed(1, 20)
                                .mapToObj(Integer::toString)
                                .toList());
                    }
                }
                case "schematic", "schem" -> {
                    if (player.hasPermission("terraformer.mode")) {
                        completions.addAll(Arrays.asList("list", "load"));
                    }
                }
            }
        } else if (args.length == 3) {
            if ((args[0].equalsIgnoreCase("schematic") || args[0].equalsIgnoreCase("schem"))
                    && args[1].equalsIgnoreCase("load")) {
                File schematicsFolder = new File(plugin.getDataFolder(), "Schematics");
                if (schematicsFolder.exists() && schematicsFolder.isDirectory()) {
                    File[] schemFiles = schematicsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".schem")
                            || name.toLowerCase().endsWith(".schematic"));

                    if (schemFiles != null) {
                        String currentArg = args[2];
                        String[] parts = currentArg.split(",");
                        String prefix = String.join(",", Arrays.copyOfRange(parts, 0, parts.length - 1));
                        String lastPart = parts[parts.length - 1].trim();

                        List<String> matchingFiles = Arrays.stream(schemFiles)
                                .map(File::getName)
                                .map(name -> name.replaceAll("\\.(schem|schematic)$", ""))
                                .filter(name -> name.toLowerCase().startsWith(lastPart.toLowerCase()))
                                .map(match -> prefix.isEmpty() ? match : prefix + "," + match)
                                .toList();

                        completions.addAll(matchingFiles);
                    }
                }
            }
        }

        return completions.isEmpty() ? null
                : completions.stream()
                        .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        .sorted()
                        .toList();
    }
}
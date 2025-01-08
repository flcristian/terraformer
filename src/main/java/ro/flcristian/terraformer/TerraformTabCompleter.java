package ro.flcristian.terraformer;

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
                    "brush", "brushes", "size", "depth", "materials", "materialmode", "materialmodes"));
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
                case "help" -> completions.addAll(Arrays.asList("1", "2", "3"));
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
            }
        }

        return completions.isEmpty() ? null
                : completions.stream()
                        .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        .sorted()
                        .toList();
    }
}
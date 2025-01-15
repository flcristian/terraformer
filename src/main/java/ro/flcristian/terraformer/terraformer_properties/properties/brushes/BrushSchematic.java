package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushSchematic extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        if (brushProperties.loadedSchematic == null) {
            player.sendMessage(Component.text("No schematic loaded").color(NamedTextColor.RED));
            return false;
        }

        return true;
    }
}

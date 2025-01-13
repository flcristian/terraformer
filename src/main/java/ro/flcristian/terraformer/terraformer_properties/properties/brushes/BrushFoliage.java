package ro.flcristian.terraformer.terraformer_properties.properties.brushes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ro.flcristian.terraformer.Terraformer;
import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public class BrushFoliage extends Brush {
    public static boolean brush(Terraformer plugin, Player player, BrushProperties brushProperties,
            Location targetLocation, boolean isRedo) {
        if (checkMaterialPercentages(brushProperties, player))
            return false;

        return true;
    }
}

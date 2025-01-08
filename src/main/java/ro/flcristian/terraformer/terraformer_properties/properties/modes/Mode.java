package ro.flcristian.terraformer.terraformer_properties.properties.modes;

import org.bukkit.Location;
import org.bukkit.Material;

import ro.flcristian.terraformer.terraformer_properties.properties.BrushProperties;

public interface Mode {
    public Material getMaterial(Location location, Location targeLocation, BrushProperties properties);
}

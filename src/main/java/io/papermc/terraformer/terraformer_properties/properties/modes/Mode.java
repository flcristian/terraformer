package io.papermc.terraformer.terraformer_properties.properties.modes;

import org.bukkit.Location;
import org.bukkit.Material;

import io.papermc.terraformer.terraformer_properties.properties.BrushProperties;

public interface Mode {
    public Material getMaterial(Location location, Location targeLocation, BrushProperties properties);
}

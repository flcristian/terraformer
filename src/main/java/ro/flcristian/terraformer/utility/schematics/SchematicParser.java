package ro.flcristian.terraformer.utility.schematics;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;

import ro.flcristian.terraformer.utility.schematics.records.SchematicData;

public interface SchematicParser {
    public SchematicData readSchematicFile(Player player, File file) throws IOException;
}

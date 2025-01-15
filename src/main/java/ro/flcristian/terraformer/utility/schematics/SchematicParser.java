package ro.flcristian.terraformer.utility.schematics;

import java.io.File;
import java.io.IOException;

import ro.flcristian.terraformer.utility.schematics.records.SchematicData;

public interface SchematicParser {
    public SchematicData readSchematicFile(File file) throws IOException;
}

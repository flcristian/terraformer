package ro.flcristian.terraformer.utility.schematics.records;

import java.util.List;

public record SchematicData(int width, int height, int length, List<Pair<SchematicBlockPos, String>> blocks) {

}

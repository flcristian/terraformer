package ro.flcristian.terraformer.utility.schematics.records;

import java.util.ArrayList;
import java.util.List;

public record SchematicData(int width, int height, int length, List<Pair<SchematicBlockPos, String>> blocks)
        implements Cloneable {

    @Override
    public SchematicData clone() {
        return new SchematicData(width, height, length, new ArrayList<>(blocks));
    }
}

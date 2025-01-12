package ro.flcristian.terraformer.utility;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MaterialNameFormatter {
    private MaterialNameFormatter() {
    }

    public static String format(String materialName) {
        String transformed = Arrays.stream(materialName.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        return transformed;
    }
}

package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.function.Supplier;

public class LayerModeSingleton {
    private static final Supplier<LayerMode> instance = new Supplier<>() {
        private final LayerMode singletonInstance = new LayerMode();

        @Override
        public LayerMode get() {
            return singletonInstance;
        }
    };

    private LayerModeSingleton() {
    }

    public static LayerMode getInstance() {
        return instance.get();
    }
}
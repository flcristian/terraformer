package io.papermc.terraformer.terraformer_properties.properties.modes;

import java.util.function.Supplier;

public class GradientModeSingleton {
    private static final Supplier<GradientMode> instance = new Supplier<>() {
        private final GradientMode singletonInstance = new GradientMode();

        @Override
        public GradientMode get() {
            return singletonInstance;
        }
    };

    private GradientModeSingleton() {
    }

    public static GradientMode getInstance() {
        return instance.get();
    }
}

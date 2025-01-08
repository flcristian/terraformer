package ro.flcristian.terraformer.terraformer_properties.properties.modes;

import java.util.function.Supplier;

public class RandomModeSingleton {
    private static final Supplier<RandomMode> instance = new Supplier<>() {
        private final RandomMode singletonInstance = new RandomMode();

        @Override
        public RandomMode get() {
            return singletonInstance;
        }
    };

    private RandomModeSingleton() {
    }

    public static RandomMode getInstance() {
        return instance.get();
    }
}
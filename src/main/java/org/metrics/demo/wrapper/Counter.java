package org.metrics.demo.wrapper;

import static io.micrometer.core.instrument.Counter.builder;
import static io.micrometer.core.instrument.Metrics.globalRegistry;

// Wrap the micrometer counter so that you could switch out the implementation behind this any time
public class Counter {

    private final io.micrometer.core.instrument.Counter microCounter;

    public Counter(String name) {
        this.microCounter = builder(name).register(globalRegistry);
    }

    public Counter(String name, String tags) {
        io.micrometer.core.instrument.Counter.Builder builder = builder(name).tags(tags);
        this.microCounter = builder.register(globalRegistry);
    }

    public void increment() {
        microCounter.increment();
    }

    public void increment(double amount) {
        microCounter.increment(amount);
    }

    public double count() {
        return microCounter.count();
    }

}

package org.metrics.demo.wrapper;

import java.util.Map;

import static io.micrometer.core.instrument.DistributionSummary.builder;
import static io.micrometer.core.instrument.Metrics.globalRegistry;

// Wrap the micrometer distribution summary so that you could switch out the implementation behind this any time
public class DistributionSummary {

    private final io.micrometer.core.instrument.DistributionSummary microDistributionSummary;

    private static final double[] DEFAULT_PERCENTILES = {0.3, 0.5, 0.99};

    private static final String DEFAULT_BASE_UNIT = "nanoseconds";

    public DistributionSummary(String name) {
        this.microDistributionSummary = builder(name).publishPercentiles(DEFAULT_PERCENTILES).baseUnit(DEFAULT_BASE_UNIT).register(globalRegistry);
    }

    public DistributionSummary(String name, String tags) {
        io.micrometer.core.instrument.DistributionSummary.Builder builder = builder(name).tags(tags);
        this.microDistributionSummary = builder.publishPercentiles(DEFAULT_PERCENTILES).baseUnit(DEFAULT_BASE_UNIT).register(globalRegistry);
    }

    public DistributionSummary(String name, double[] percentiles) {
        this.microDistributionSummary = builder(name).publishPercentiles(percentiles).baseUnit(DEFAULT_BASE_UNIT).register(globalRegistry);
    }

    public DistributionSummary(String name, double[] percentiles, String tags) {
        io.micrometer.core.instrument.DistributionSummary.Builder builder = builder(name).tags(tags);
        this.microDistributionSummary = builder.publishPercentiles(percentiles).baseUnit(DEFAULT_BASE_UNIT).register(globalRegistry);
    }

    public void record(double amount) {
        microDistributionSummary.record(amount);
    }

}

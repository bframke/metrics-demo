package org.metrics.demo.services;

import org.metrics.demo.wrapper.Counter;
import org.metrics.demo.wrapper.DistributionSummary;
import org.metrics.demo.wrapper.Timer;

import java.util.function.Function;

// Define what you actually want to call and that could be reused for multiple implementations
public interface MicroMeterService {

    enum MicroMeterType {
        STANDARD,
        PROMETHEUS;
    }

    MicroMeterType getMicroMeterType();

    Counter counter(String name);

    Counter counter(String name, String tags);

    <T> Counter counter(Class<T> clazz);

    <T> Counter counter(Function<T, ?> function);

    Timer timer(String name);

    Timer timer(String name, String tags);

    <T> Timer timer(Class<T> clazz);

    <T> Timer timer(Function<T, ?> function);

    Timer.Sample startSample();

    long stopSample(Timer.Sample sample, Timer timer);

    long stopSample(Timer.Sample sample, String name);

    <T> long stopSample(Timer.Sample sample, Class<T> clazz);

    <T> long stopSample(Timer.Sample sample, Function<T, ?> function);

    DistributionSummary summary(String name);

    DistributionSummary summary(String name, String tags);

    DistributionSummary summary(String name, double[] percentiles);

    DistributionSummary summary(String name, double[] percentiles, String tags);

    <T> DistributionSummary summary(Class<T> clazz);

    <T> DistributionSummary summary(Function<T, ?> function);

}

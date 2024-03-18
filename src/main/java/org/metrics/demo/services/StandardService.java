package org.metrics.demo.services;

import org.metrics.demo.wrapper.Counter;
import org.metrics.demo.wrapper.DistributionSummary;
import org.metrics.demo.wrapper.Timer;

import java.util.function.Function;

import static java.lang.String.valueOf;

// Create a set of implementations to handle meter creation in many ways as possible
public class StandardService implements MicroMeterService {

    private static final String COUNTER_SUFFIX = ".counter";
    private static final String TIMER_SUFFIX = ".timer";
    private static final String SUMMARY_PREFIX = "duration.";

    @Override
    public MicroMeterType getMicroMeterType() {
        return MicroMeterType.STANDARD;
    }

    @Override
    public Counter counter(String name) {
        return new Counter(name.concat(COUNTER_SUFFIX));
    }

    @Override
    public Counter counter(String name, String tags) {
        return new Counter(name.concat(COUNTER_SUFFIX), tags);
    }

    @Override
    public <T> Counter counter(Class<T> clazz) {
        return counter(clazz.getName());
    }

    @Override
    public <T> Counter counter(Function<T, ?> function) {
        return counter(valueOf(function));
    }

    @Override
    public Timer timer(String name) {
        return new Timer(name.concat(TIMER_SUFFIX));
    }

    @Override
    public Timer timer(String name, String tags) {
        return new Timer(name.concat(TIMER_SUFFIX), tags);
    }

    @Override
    public <T> Timer timer(Class<T> clazz) {
        return timer(clazz.getName());
    }

    @Override
    public <T> Timer timer(Function<T, ?> function) {
        return timer(valueOf(function));
    }

    @Override
    public Timer.Sample startSample() {
        return Timer.start();
    }

    @Override
    public long stopSample(Timer.Sample sample, Timer timer) {
        return sample.stop(timer);
    }

    @Override
    public long stopSample(Timer.Sample sample, String name) {
        return sample.stop(timer(name));
    }

    @Override
    public <T> long stopSample(Timer.Sample sample, Class<T> clazz) {
        return sample.stop(timer(clazz.getName()));
    }

    @Override
    public <T> long stopSample(Timer.Sample sample, Function<T, ?> function) {
        return sample.stop(timer(valueOf(function)));
    }

    @Override
    public DistributionSummary summary(String name) {
        return new DistributionSummary(SUMMARY_PREFIX.concat(name));
    }

    @Override
    public DistributionSummary summary(String name, String tags) {
        return new DistributionSummary(SUMMARY_PREFIX.concat(name), tags);
    }

    @Override
    public DistributionSummary summary(String name, double[] percentiles) {
        return new DistributionSummary(SUMMARY_PREFIX.concat(name), percentiles);
    }

    @Override
    public DistributionSummary summary(String name, double[] percentiles, String tags) {
        return new DistributionSummary(SUMMARY_PREFIX.concat(name), percentiles, tags);
    }

    @Override
    public <T> DistributionSummary summary(Class<T> clazz) {
        return summary(clazz.getName());
    }

    @Override
    public <T> DistributionSummary summary(Function<T, ?> function) {
        return summary(valueOf(function));
    }
}

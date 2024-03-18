package org.metrics.demo.handler;

import io.micrometer.core.instrument.Gauge;
import lombok.extern.slf4j.Slf4j;
import org.metrics.demo.services.MicroMeterService;
import org.metrics.demo.services.PrometheusService;
import org.metrics.demo.wrapper.Counter;
import org.metrics.demo.wrapper.DistributionSummary;
import org.metrics.demo.wrapper.Timer;

import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import static java.lang.String.format;
import static java.util.Objects.*;
import static org.metrics.demo.StandardBinder.*;
import static org.metrics.demo.services.MicroMeterService.MicroMeterType.PROMETHEUS;
import static org.metrics.demo.services.PrometheusService.prometheusMeterRegistry;

// Use this to create the service you want to use and define functions to create and record your meters
@Slf4j
public class MetricHandler {

    private static PrometheusService prometheusService;

    public static PrometheusService getPrometheusService() {
        return prometheusService;
    }

    private MetricHandler() {

    }

    public static void initMetrics() {
        if (isNull(prometheusService)) {
            init();
        }
    }

    // Use counter when you just want to count up
    public static Counter counter(String name) {
        return requireNonNull(prometheusService).counter(name);
    }

    public static Counter counter(String name, String tags) {
        return requireNonNull(prometheusService).counter(name, tags);
    }

    // Use timers when you want to count and measure the duration of a call
    public static Timer.Sample startSample() {
        return requireNonNull(prometheusService).startSample();
    }

    public static long stopSample(Timer.Sample sample, Timer timer) {
        return requireNonNull(prometheusService).stopSample(sample, timer);
    }

    public static void stopSample(Timer.Sample sample, Timer timer, String summaryName) {
        summary(summaryName).record(requireNonNull(prometheusService).stopSample(sample, timer));
    }

    public static void stopSample(Timer.Sample sample, Timer timer, DistributionSummary distributionSummary) {
        distributionSummary.record(requireNonNull(prometheusService).stopSample(sample, timer));
    }

    public static long stopSample(Timer.Sample sample, String name) {
        return requireNonNull(prometheusService).stopSample(sample, name);
    }

    // TODO: Has problems with its counterpart in line 85, when R is String
    public static void stopSample(Timer.Sample sample, String timerName, String summaryName) {
        summary(summaryName).record(requireNonNull(prometheusService).stopSample(sample, timerName));
    }

    public static void stopSample(Timer.Sample sample, String timerName, DistributionSummary distributionSummary) {
        distributionSummary.record(requireNonNull(prometheusService).stopSample(sample, timerName));
    }

    public static <R> R stopSample(Timer.Sample sample, Timer timer, R result) {
        requireNonNull(prometheusService).stopSample(sample, timer);
        return result;
    }

    // TODO: Has problems with its counterpart in line 71, when R is String
    public static <R> R stopSample(Timer.Sample sample, String timerName, R result) {
        requireNonNull(prometheusService).stopSample(sample, timerName);
        return result;
    }

    public static <R> R stopSample(Timer.Sample sample, Timer timer, String summaryName, R result) {
        summary(summaryName).record(requireNonNull(prometheusService).stopSample(sample, timer));
        return result;
    }

    public static <R> R stopSample(Timer.Sample sample, Timer timer, DistributionSummary distributionSummary, R result) {
        distributionSummary.record(requireNonNull(prometheusService).stopSample(sample, timer));
        return result;
    }

    public static <R> R stopSample(Timer.Sample sample, String timerName, String summaryName, R result) {
        summary(summaryName).record(requireNonNull(prometheusService).stopSample(sample, timerName));
        return result;
    }

    public static <R> R stopSample(Timer.Sample sample, String timerName, DistributionSummary distributionSummary, R result) {
        distributionSummary.record(requireNonNull(prometheusService).stopSample(sample, timerName));
        return result;
    }

    // Use summaries to create a histogram or to show better different durations for calls on functions
    public static DistributionSummary summary(String name) {
        return requireNonNull(prometheusService).summary(name);
    }

    public static DistributionSummary summary(String name, String tags) {
        return requireNonNull(prometheusService).summary(name, tags);
    }

    public static DistributionSummary summary(String name, double[] percentiles) {
        return requireNonNull(prometheusService).summary(name, percentiles);
    }

    public static DistributionSummary summary(String name, double[] percentiles, String tags) {
        return requireNonNull(prometheusService).summary(name, percentiles, tags);
    }

    // Use gauges when you want to show something that should have data when it is active like in cache
    public static <T> void registerGauge(String name, T obj, ToDoubleFunction<T> func) {
        Gauge.builder(name, obj, func).register(prometheusMeterRegistry);
    }

    public static void registerGauge(String name, Supplier<Number> supplier) {
        Gauge.builder(name, supplier).register(prometheusMeterRegistry);
    }

    private static MicroMeterService loadService(MicroMeterService.MicroMeterType type) {
        // This is for the usage of a service loader pattern
        // The metrics lib part needs to be split from this demo to work that way
        /*try {
            ServiceLoader<MicroMeterService> loader = ServiceLoader.load(MicroMeterService.class);
            return stream(loader.spliterator(), true).filter(t -> type.equals(t.getMicroMeterType())).findFirst()
                    .orElseThrow(() -> new IOException("Service could not be loaded!"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;*/

        // as an example for when not using the service loader pattern
        return new PrometheusService();
    }

    private static void init() {
        prometheusService = (PrometheusService) loadService(PROMETHEUS);

        if (nonNull(prometheusService)) {
            // Load the default metrics into the meter registry
            get(JVM_GC_METRICS, JVM_MEMORY_METRICS, JVM_THREAD_METRICS, CLASS_LOADER_METRICS, PROCESSOR_METRICS);
            // register the service to the meter registry
            prometheusService.register();
            log.info(format("Loaded Service %s!", PROMETHEUS));
        }
    }

}

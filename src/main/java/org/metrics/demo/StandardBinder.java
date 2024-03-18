package org.metrics.demo;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

import java.lang.reflect.InvocationTargetException;

import static io.micrometer.core.instrument.Metrics.globalRegistry;

// Bind the default metrics to something that can be easily called upon anywhere
public enum StandardBinder {

    CLASS_LOADER_METRICS(ClassLoaderMetrics.class),

    JVM_MEMORY_METRICS(JvmMemoryMetrics.class),

    JVM_GC_METRICS(JvmGcMetrics.class),

    JVM_THREAD_METRICS(JvmThreadMetrics.class),

    PROCESSOR_METRICS(ProcessorMetrics.class);

    private final Class<? extends MeterBinder> metricsBinder;

    <T extends MeterBinder> StandardBinder(Class<T> metricsBinder) {
        this.metricsBinder = metricsBinder;
    }

    public static void get(StandardBinder... standards) {
        for (StandardBinder standard : standards) {
            standard.init();
        }
    }

    private void init() {
        try {
            metricsBinder.getDeclaredConstructor().newInstance().bindTo(globalRegistry);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}

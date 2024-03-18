package org.metrics.demo.wrapper;

import io.micrometer.core.instrument.Clock;

import java.util.concurrent.TimeUnit;

import static io.micrometer.core.instrument.Metrics.globalRegistry;
import static io.micrometer.core.instrument.Timer.builder;

// Wrap the micrometer timer so that you could switch out the implementation behind this any time
public class Timer {

    private final io.micrometer.core.instrument.Timer microTimer;

    public Timer(String name) {
        this.microTimer = builder(name).register(globalRegistry);
    }

    public Timer(String name, String tags) {
        io.micrometer.core.instrument.Timer.Builder builder = builder(name).tags(tags);
        this.microTimer = builder.register(globalRegistry);
    }

    public void record(long amount, TimeUnit unit) {
        microTimer.record(amount, unit);
    }

    public static Sample start() {
        return start(Clock.SYSTEM);
    }

    public static Sample start(Clock clock) {
        return new Sample(clock);
    }

    public static class Sample {
        private final long startTime;
        private final Clock clock;

        Sample(Clock clock) {
            this.clock = clock;
            this.startTime = clock.monotonicTime();
        }

        public long stop(Timer timer) {
            long durationNs = clock.monotonicTime() - startTime;
            timer.record(durationNs, TimeUnit.NANOSECONDS);
            return durationNs;
        }
    }

}

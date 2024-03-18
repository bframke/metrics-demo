package org.metrics.demo.services;

import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.Writer;

import static io.micrometer.core.instrument.Metrics.addRegistry;
import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;
import static io.prometheus.client.exporter.common.TextFormat.CONTENT_TYPE_004;
import static org.metrics.demo.services.MicroMeterService.MicroMeterType.PROMETHEUS;

// Create a service to implement the needed function to call and create meters more easily
public class PrometheusService extends StandardService {

    public static final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(DEFAULT);

    @Override
    public MicroMeterType getMicroMeterType() {
        return PROMETHEUS;
    }

    public void register() {
        addRegistry(prometheusMeterRegistry);
    }

    public String scrape(PrometheusMeterRegistry prometheusMeterRegistry) {
        return prometheusMeterRegistry.scrape(CONTENT_TYPE_004);
    }

    public void scrape(PrometheusMeterRegistry prometheusMeterRegistry, Writer writer) throws IOException {
        prometheusMeterRegistry.scrape(writer, CONTENT_TYPE_004);
    }

}

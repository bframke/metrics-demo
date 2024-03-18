package org.metrics.demo.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static org.metrics.demo.handler.MetricHandler.getPrometheusService;
import static org.metrics.demo.services.PrometheusService.prometheusMeterRegistry;

// Use a endpoint like this to make the metrics available to any tool that could call /metrics
@Path("/metrics")
public class MetricsResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String metrics() {
        return getPrometheusService().scrape(prometheusMeterRegistry);
    }

}

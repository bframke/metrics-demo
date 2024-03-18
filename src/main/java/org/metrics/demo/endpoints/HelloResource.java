package org.metrics.demo.endpoints;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.metrics.demo.wrapper.Timer;

import static java.lang.String.valueOf;
import static org.metrics.demo.handler.MetricHandler.*;

// This is an example endpoint where timers and summaries are used
@Path("/hello")
public class HelloResource {

    private static final String METRIC_NAME = "hello";

    // Initialize the metrics and prometheus service as soon as possible
    void onStart(@Observes StartupEvent event) {
        initMetrics();
    }

    // Uses a timer to count and measure the duration of the call time to load the page
    // called via /hello
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloTimer() {
        // Start the sample to measure time
        Timer.Sample sample = startSample();

        // Stop the sample, create and record the metric and return any value
        return valueOf(stopSample(sample, METRIC_NAME, 1));
    }

    // Uses a summary to count and more precisely measure the duration of the call time to load the page
    // called via /hello/summary
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/summary")
    public String helloSummary() {
        // Start the sample to measure time
        Timer.Sample sample = startSample();

        // Stop the sample, create and record the timer, register and record the summary
        stopSample(sample, METRIC_NAME, METRIC_NAME);

        // Return anything to the caller
        return "You called the summary";
    }
}

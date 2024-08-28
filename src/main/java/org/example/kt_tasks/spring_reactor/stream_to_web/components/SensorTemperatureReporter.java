package org.example.kt_tasks.spring_reactor.stream_to_web.components;

import org.example.kt_tasks.spring_reactor.stream_to_web.warmup.SensorTemperatureGenerator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A Utility Component. Redirects data from {@link SensorTemperatureGenerator} to {@link MiniBroker}
 * so consumers of the latter are not forced to work with reactive API.
 */
@Component
public class SensorTemperatureReporter {
  private static final int DEF_TEMPERATURE_AGGREGATION_DURATION = 2;

  private final MiniBroker miniBroker;
  private final SensorTemperatureGenerator generator;

  public SensorTemperatureReporter(MiniBroker miniBroker) {
    this.miniBroker = miniBroker;
    this.generator = new SensorTemperatureGenerator(OptionalInt.of(DEF_TEMPERATURE_AGGREGATION_DURATION));
  }

  @EventListener(ApplicationReadyEvent.class) // need to wait for the application context to fully initialize, without it app will hang on the infinite number generation
  public void startGeneration() {
    try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
      executor.schedule(() -> generator.sensorTemperature().doOnNext(miniBroker::publish).subscribe(), 0, TimeUnit.SECONDS);
    }
  }

}

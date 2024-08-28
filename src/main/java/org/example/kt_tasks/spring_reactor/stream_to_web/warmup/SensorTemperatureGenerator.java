package org.example.kt_tasks.spring_reactor.stream_to_web.warmup;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

/**
 * Simple Reactor-based generator of random double numbers.
 *
 */
public class SensorTemperatureGenerator {
  private static final int DEF_TEMPERATURE_AGGREGATION_DURATION = 2;

  private final int temperatureAggregationDuration;

  public SensorTemperatureGenerator(OptionalInt temperatureAggregationDuration) {
    this.temperatureAggregationDuration = temperatureAggregationDuration.orElse(DEF_TEMPERATURE_AGGREGATION_DURATION);
  }

  public Flux<Double> sensorTemperature() {
    Random random = new Random(42L);
    double sensorMinTemperature = -25.0;
    double sensorMaxTemperature = 120.0;
    return Flux.<Double>generate(sink -> sink.next(random.nextDouble(sensorMinTemperature, sensorMaxTemperature + 1)))
      .buffer(Duration.ofSeconds(temperatureAggregationDuration))
      .map(this::getAggregationMean);
  }

  private Double getAggregationMean(List<Double> aggregation) {
    return aggregation.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
  }

  public static void main(String[] args) {
    System.out.println("SensorTemperatureGenerator Demonstration");
    SensorTemperatureGenerator generator = new SensorTemperatureGenerator(OptionalInt.of(2));
    generator.sensorTemperature()
      .take(10)
      .doOnNext(System.out::println)
      .subscribe();
  }
}

package org.example.kt_tasks.spring_reactor.stream_to_web.controller;

import org.example.kt_tasks.spring_reactor.stream_to_web.components.MiniBroker;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller containing endpoints to produce and consume data streams.
 * It utilizes {@link MiniBroker} in order to produce data stream.
 */
@RestController
public class SensorTemperatureController {

  private final MiniBroker miniBroker;

  public SensorTemperatureController(MiniBroker miniBroker) {
    this.miniBroker = miniBroker;
  }


  //TODO: implement `/temperature` GET-endpoint to stream data from the MiniBroker

  //TODO*: implement endpoint that reads first 10 messages from the `/temperature` endpoint
}


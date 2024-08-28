package org.example.kt_tasks.spring_reactor.stream_to_web.controller;

import org.example.kt_tasks.spring_reactor.stream_to_web.components.MiniBroker;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

  @GetMapping(path = "/temperature", produces = MediaType.TEXT_EVENT_STREAM_VALUE) //TODO: to make it work simultaneously with /temperature-flux, MiniBroker would need some tweaks
  public SseEmitter streamTemperature() {
    SseEmitter emitter = new SseEmitter();
    ExecutorService executor = Executors.newSingleThreadExecutor();

    executor.execute(() -> {
      try {
        while (true) {
          if (miniBroker.hasData()) emitter.send(miniBroker.consume());
        }
      } catch (Exception ex) {
        System.out.println("Error in SSE: " + ex);
        emitter.completeWithError(ex);
      }
    });
    return emitter;
  }

  @GetMapping(path = "/temperature-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Double> streamTemperatureFlux() {
    return Flux.generate(sink -> {
      try {
        sink.next(miniBroker.blockingConsume());
      } catch (InterruptedException e) {
        sink.error(e);
      }
    });
  }






  /***************** EXTRA ******************/

  @GetMapping(path = "/read-sse/web-client")
  public Mono<String> readSseUsingWebClient() throws URISyntaxException {
    var uri = new URI("http://localhost:8080/temperature-flux"); //TODO: use ServletRequest to get host name
    var client = WebClient.create();

    return client.get()
      .uri(uri)
      .retrieve()
      .bodyToFlux(String.class)
      .take(10)
      .collectList()
      .map(list -> String.join(", ", list));
  }

  @GetMapping(path = "/read-sse/java-http")
  public String readSseUsingJavaHttpClient() throws IOException, InterruptedException, URISyntaxException {
    var uri = new URI("http://localhost:8080/temperature-flux");
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder(uri).GET().build();
    var lines = client.send(request, HttpResponse.BodyHandlers.ofLines()).body();

    List<String> collect = lines.filter(line -> !line.isEmpty()).limit(10).toList();
    return String.join(", ", collect);
  }

  @GetMapping(path = "/read-sse/rest-template")
  public String readStreamFluxV2() {
    RestTemplate restTemplate = new RestTemplate();

    Instant startTime = Instant.now();
    Duration readDuration = Duration.ofSeconds(10);

    ConcurrentLinkedQueue<String> events = restTemplate.execute("http://localhost:8080/temperature-flux", HttpMethod.GET,
      request -> {},
      response -> readStringStream(response.getBody(), startTime, readDuration));

    return String.join(", ", events);
  }

  @GetMapping(path = "/read-sse/rest-client")
  public String readSseUsingRestClient() {
    RestClient restClient = RestClient.create();
    ConcurrentLinkedQueue<String> events = new ConcurrentLinkedQueue<>();

    try {
      URI uri = new URI("http://localhost:8080/temperature-flux");
      Instant startTime = Instant.now();
      Duration readDuration = Duration.ofSeconds(10);

      events = restClient
        .get()
        .uri(uri)
        .exchange((request, response) -> readStringStream(response.getBody(), startTime, readDuration));
    } catch (RestClientException | URISyntaxException e) {
      e.printStackTrace();
    }

    return String.join(", ", events);
  }

  //TODO: review this, it causing IllegalStateException
  private ConcurrentLinkedQueue<String> readStringStream(InputStream inputStream, Instant startTime, Duration readDuration) {
    ConcurrentLinkedQueue<String> events = new ConcurrentLinkedQueue<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      // Read each line (event) from the response body and break on null or if 10 seconds have passed
      while ((line = reader.readLine()) != null && isReadTimeUp(startTime, readDuration)) {
        System.out.println("Received event: " + line);
        events.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return events;
  }

  private boolean isReadTimeUp(Instant startTime, Duration readDuration) {
    return Duration.between(startTime, Instant.now()).getSeconds() >= readDuration.getSeconds();
  }

}


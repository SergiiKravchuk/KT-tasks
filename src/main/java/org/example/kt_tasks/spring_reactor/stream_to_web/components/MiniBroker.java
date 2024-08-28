package org.example.kt_tasks.spring_reactor.stream_to_web.components;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Mini message broker with one topic - Double numbers.
 * Data storage based on {@link BlockingQueue}, so it can try to handle multiple clients.
 */
@Component
public class MiniBroker {

  private final BlockingQueue<Double> queue = new LinkedBlockingQueue<>();

  void publish(Double number) {
    queue.offer(number);
  }

  public Double consume() {
    return queue.poll();
  }

  public Double blockingConsume() throws InterruptedException {
    return queue.take();
  }

  public boolean hasData() {
    return !queue.isEmpty();
  }

}

package org.example.kt_tasks.spring_reactor.warmup;

import org.example.kt_tasks.spring_reactor.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentProcessorTest {

  private PaymentProcessor processor;

  @BeforeEach
  public void setup() {
    double paymentAmountLimit = 100.0;
    processor = new PaymentProcessor(paymentAmountLimit);
  }

  @Test
  public void testProcessPayment() {
    Payment payment = new Payment("1234", 100.50, "Alice", "Bob");

    Mono<String> resultMono = processor.processPayment(Mono.just(payment));

    StepVerifier.create(resultMono)
      .expectNext("Alice")
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessBatchPayments() {
    Flux<Payment> paymentsFlux = Flux.just(
      new Payment("1236", 50.00, "Eve", "Frank"),
      new Payment("1237", 75.25, "Grace", "Hank"),
      new Payment("1238", 200.00, "Ivy", "Jack")
    );

    Flux<String> resultFlux = processor.processBatchPayments(paymentsFlux);

    StepVerifier.create(resultFlux)
      .expectNext("Eve", "Grace", "Ivy")
      .expectComplete()
      .verify();
  }

  @Test
  public void testFilterPaymentsWithinLimit_FilterOutOne() {
    Payment payment1 = new Payment("1234", 50.00, "Alice", "Bob");
    Payment payment2 = new Payment("5678", 150.00, "Charlie", "Dave");
    Payment payment3 = new Payment("6789", 100.00, "Andre", "Alice");
    Flux<Payment> paymentFlux = Flux.just(payment1, payment2, payment3);

    Flux<Payment> resultFlux = processor.filterPaymentsWithinLimit(paymentFlux);

    StepVerifier.create(resultFlux)
      .expectNext(payment1, payment3)
      .as("Expecting the payments in the result to be %s and %s".formatted(payment1, payment3))
      .expectComplete()
      .verify();
  }

  @Test
  public void testFilterPaymentsWithinLimit_FilterOutAll() {
    Payment payment1 = new Payment("1234", 100.50, "Alice", "Bob");
    Payment payment2 = new Payment("1238", 200.00, "Ivy", "Jack");
    Payment payment3 = new Payment("1241", 120.00, "Oscar", "Pam");
    Flux<Payment> paymentFlux = Flux.just(payment1, payment2, payment3);

    Flux<Payment> resultFlux = processor.filterPaymentsWithinLimit(paymentFlux);

    StepVerifier.create(resultFlux)
      .expectNextCount(0)
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessPaymentWithDetails() {
    Payment payment = new Payment("1234", 100.50, "Alice", "Bob");

    Mono<String> resultMono = processor.processPaymentWithDetails(Mono.just(payment));

    StepVerifier.create(resultMono)
      .expectNext("Alice: Details for payment 1234")
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessPaymentWithErrorHandling_InvalidPayment() {
    Payment payment = new Payment("1235", -150.75, "Charlie", "Dave");

    Mono<String> resultMono = processor.processPaymentWithErrorHandling(Mono.just(payment));

    StepVerifier.create(resultMono)
      .expectError(IllegalArgumentException.class)
      .verify();
  }

  @Test
  public void testProcessPaymentWithErrorHandling_ValidPayment() {
    Payment payment = new Payment("1236", 50.00, "Eve", "Frank");

    Mono<String> resultMono = processor.processPaymentWithErrorHandling(Mono.just(payment));

    StepVerifier.create(resultMono)
      .expectNext("Eve")
      .expectComplete()
      .verify();
  }


  @Test
  public void testProcessBatchPaymentsWithErrorHanding_InvalidPayment() {
    Payment payment1 = new Payment("1239", 30.00, "Ken", "Leo");
    Payment payment2 = new Payment("1240", -20.00, "Mia", "Nina");
    Payment payment3 = new Payment("1241", 120.00, "Oscar", "Pam");
    Flux<Payment> paymentsFlux = Flux.just(
      payment1,
      payment2, // This will cause an error
      payment3
    );

    Flux<String> resultFlux = processor.processBatchPaymentsWithErrorHanding(paymentsFlux);

    StepVerifier.create(resultFlux)
      .expectNext("Ken")
      .as(("Expecting only the first payment=%s to be present in the result. " +
        "All other payments should be dropped because payment=%s caused an error and stopped the processing.").formatted(payment1, payment2))
      .expectError(IllegalArgumentException.class)
      .verify();
  }

  @Test
  public void testProcessBatchPaymentsWithErrorHanding_AllValidPayments() {
    Flux<Payment> paymentsFlux = Flux.just(
      new Payment("1236", 50.00, "Eve", "Frank"),
      new Payment("1237", 75.25, "Grace", "Hank"),
      new Payment("1238", 200.00, "Ivy", "Jack")
    );

    Flux<String> resultFlux = processor.processBatchPaymentsWithErrorHanding(paymentsFlux);

    StepVerifier.create(resultFlux)
      .expectNext("Eve", "Grace", "Ivy")
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessBatchPaymentsWithErrorSuppression() {
    Payment payment1 = new Payment("1239", 30.00, "Ken", "Leo");
    Payment payment2 = new Payment("1240", -20.00, "Mia", "Nina");
    Payment payment3 = new Payment("1241", 120.00, "Oscar", "Pam");
    Flux<Payment> paymentsFlux = Flux.just(
      payment1,
      payment2, // This will cause an error but the error will be suppressed
      payment3
    );

    Flux<String> resultFlux = processor.processBatchPaymentsWithErrorSuppression(paymentsFlux);

    StepVerifier.create(resultFlux)
      .expectNext("Ken", "Oscar")
      .as(("Expecting all valid payments in the result: %s and %s. " +
        "Error on payment=%s should be suppressed and the payment should be dropped.").formatted(payment1, payment3, payment2))
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessCombinedPayment() {
    Payment payment1 = new Payment("1234", 100.50, "Alice", "Bob");
    Payment payment2 = new Payment("5678", 200.75, "Charlie", "Dave");

    Mono<String> resultMono = processor.processCombinedPayment(Mono.just(payment1), Mono.just(payment2));

    StepVerifier.create(resultMono)
      .expectNext("Combined payment from Alice and Charlie")
      .expectComplete()
      .verify();
  }


  @Test
  public void testCountPayment() {
    Payment payment = new Payment("1234", 100.50, "Alice", "Bob");

    Mono<Payment> resultMono = processor.countPayment(Mono.just(payment));

    StepVerifier.create(resultMono)
      .expectNext(payment)
      .as("Expecting that given payment is not changed.")
      .expectComplete()
      .verify();
    assertEquals(processor.getPaymentCount(), 1);
  }

  @Test
  public void testNotifyOnSuccessSignal() {
    Mono<Void> paymentSuccessSignalMono = Mono.create(s -> {
      System.out.println("Payment successfully processed");
      s.success();
    });

    Mono<String> resultMono = processor.notifyOnSuccessSignal(paymentSuccessSignalMono);

    StepVerifier.create(resultMono)
      .expectNext("Payment processed and notification sent.")
      .expectComplete()
      .verify();
  }

  @Test
  public void testProcessBatchPaymentsWithNullInput() {
    Flux<Payment> paymentsFlux = Flux.just(
      new Payment("1236", 50.00, "Eve", "Frank"),
      null,
      new Payment("1238", 200.00, "Ivy", "Jack")
    );

    Flux<String> resultFlux = processor.processBatchPaymentsWithNullInput(paymentsFlux);

    StepVerifier.create(resultFlux)
      .expectNext("Eve")
      .expectErrorMatches(throwable -> throwable instanceof Exception &&
        throwable.getMessage().equals("Got an error: The 1th array element was null"))
      .verify();
  }
}

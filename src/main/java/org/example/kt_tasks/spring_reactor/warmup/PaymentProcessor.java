package org.example.kt_tasks.spring_reactor.warmup;

import org.example.kt_tasks.exception.ExerciseNotCompletedException;
import org.example.kt_tasks.spring_reactor.entity.Payment;
import org.example.kt_tasks.spring_reactor.entity.PaymentDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class contains different examples of manipulations on Project Reactor's main Mono/Flux types.
 */
class PaymentProcessor {

  private final double paymentAmountLimit;

  PaymentProcessor(double paymentAmountLimit) {
    this.paymentAmountLimit = paymentAmountLimit;
  }

  /**
   * Example of mapping operation.<p>
   * This method returns a {@link Payment#sender} of the given payment.
   */
  public Mono<String> processPayment(Mono<Payment> paymentMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of mapping operation in stream.<p>
   * This method returns a {@link Payment#sender} of all the given payments.
   */
  public Flux<String> processBatchPayments(Flux<Payment> paymentsFlux) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of filtering operation.<p>
   * This method filters in (keeps) only those payments whose amount is less than or equal
   * to the {@link PaymentProcessor#paymentAmountLimit}.
   */
  public Flux<Payment> filterPaymentsWithinLimit(Flux<Payment> paymentMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of external operation signal (Mono) processing during another signal(Mono) processing.<p>
   * This method retrieves {@link PaymentDetails} for the given {@link Payment}
   * and generates a formatted message using {@link PaymentProcessor#formatPaymentDetails}
   */
  public Mono<String> processPaymentWithDetails(Mono<Payment> paymentMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  private String formatPaymentDetails(Payment payment, PaymentDetails paymentDetails) {
    System.out.printf("Fetching data from payment: %s payment%n", payment);
    return payment.getSender() + ": " + paymentDetails.details();
  }

  // Simulate fetching payment details
  private Mono<PaymentDetails> fetchPaymentDetails(String paymentId) {
    return Mono.just(new PaymentDetails("Details for payment " + paymentId));
  }


  /**
   * Example of error handling.<p>
   * This method returns a {@link Payment#sender} of the given payment.
   * Additionally, it raises an error (as {@link IllegalArgumentException}) if {@link Payment#amount} value is less than 0.
   * Otherwise, passes the given payment through.
   */
  public Mono<String> processPaymentWithErrorHandling(Mono<Payment> paymentMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of error handling.<p>
   * This method returns a {@link Payment#sender} of all the given payments.
   * Additionally, it raises an error(as {@link IllegalArgumentException}) if {@link Payment#amount} value is less than 0
   * and stops the processing of the given payments keeping only those whose were processed before the error.
   * Otherwise, passes any other payment through.
   */
  public Flux<String> processBatchPaymentsWithErrorHanding(Flux<Payment> paymentsFlux) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of error suppression.<p>
   * This method returns the same result as the {@link PaymentProcessor#processBatchPaymentsWithErrorHanding} but
   * suppresses any error that may occur during payments processing.
   */
  public Flux<String> processBatchPaymentsWithErrorSuppression(Flux<Payment> paymentsFlux) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  /**
   * Example of combining operation.<p>
   * This method combines two given payments and generates a formatted message using {@link PaymentProcessor#formatCombinedPayment}
   */
  public Mono<String> processCombinedPayment(Mono<Payment> paymentMono1, Mono<Payment> paymentMono2) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  private String formatCombinedPayment(Payment payment1, Payment payment2) {
    return "Combined payment from " + payment1.getSender() + " and " + payment2.getSender();
  }


  /**
   * Example of a side operation.<p>
   * This method increments the {@link PaymentProcessor#counter} on each payment passed to this function
   * and keep the payment unchanged.
   */
  public Mono<Payment> countPayment(Mono<Payment> paymentMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  private final AtomicInteger counter = new AtomicInteger(0);

  public Integer getPaymentCount() {
    return counter.getAcquire();
  }


  /**
   * Example of chaining.<p>
   * This method returns the result of {@link PaymentProcessor#sentNotification} which is a next step of the given signal.
   */
  public Mono<String> notifyOnSuccessSignal(Mono<Void> paymentSuccessSignalMono) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

  // Simulate sending notification
  private Mono<String> sentNotification() {
    return Mono.just("Payment processed and notification sent.");
  }


  /**
   * BONUS
   * Example of how Reactor handles `null` values and how to catch the exception thrown by Reactor.<p>
   * This method returns a {@link Payment#sender} of all the given payments. In case of any error,
   * the thrown exception must be repackaged into plain {@link Exception} with the following message:<p>
   * {@code Got an error: < message from upstream exception > }
   */
  public Flux<String> processBatchPaymentsWithNullInput(Flux<Payment> paymentsFlux) {
    //TODO: Add implementation according to the method's description.
    throw new ExerciseNotCompletedException();
  }

}

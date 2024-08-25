package org.example.kt_tasks.spring_reactor.entity;

public class Payment {
  private String id;
  private double amount;
  private String sender;
  private String receiver;

  public Payment(String id, double amount, String sender, String receiver) {
    this.id = id;
    this.amount = amount;
    this.sender = sender;
    this.receiver = receiver;
  }

  public String getId() {
    return id;
  }

  public double getAmount() {
    return amount;
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  @Override
  public String toString() {
    return "Payment{id='" + id + "', amount=" + amount + ", sender='" + sender + "', receiver='" + receiver + "'}";
  }
}
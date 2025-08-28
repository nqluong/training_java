package DesignPattern.CreationalPattern.FactoryMethod;

// Concrete Product
public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS Notification: " + message);
    }
}

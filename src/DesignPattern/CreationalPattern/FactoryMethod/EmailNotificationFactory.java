package DesignPattern.CreationalPattern.FactoryMethod;

// Concrete Creator
public class EmailNotificationFactory extends NotificationFactory{
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}

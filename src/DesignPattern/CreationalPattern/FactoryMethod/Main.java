package DesignPattern.CreationalPattern.FactoryMethod;

public class Main {
    public static void main(String[] args) {

        NotificationFactory emailFactory = new EmailNotificationFactory();
        Notification emailNotification = emailFactory.createNotification();
        emailNotification.send("Hello via Email!");

        NotificationFactory smsFactory = new SMSNotificationFactory();
        Notification smsNotification = smsFactory.createNotification();
        smsNotification.send("Hello via SMS!");
    }
}

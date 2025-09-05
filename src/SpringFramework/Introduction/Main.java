package SpringFramework.Introduction;

public class Main {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        SMSService smsService = new SMSService();

       // Client client = new Client(emailService);

        Client client = new Client();
      // client.setMessageService(smsService);
        client.setService(emailService);
        client.processMessage("Hello World");
    }
}

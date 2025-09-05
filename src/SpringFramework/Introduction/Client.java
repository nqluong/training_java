package SpringFramework.Introduction;

public class Client implements InjectionMessage {
    private MessageService messageService;

//    public Client(MessageService messageService) {
//        this.messageService = messageService;
//    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void processMessage(String message) {
        messageService.sendMessage(message);
    }

    @Override
    public void setService(MessageService service) {
        this.messageService = service;
    }
}

package ch.fhnw.ws6.chat;

import java.util.List;

import ch.fhnw.ws6.chat.actorsystem.ClientApplication;
import ch.fhnw.ws6.chat.actorsystem.Consumer;

public class ChatClient implements Consumer<String> {

    private Consumer<String> consumer;
    private ClientApplication client;

    public ChatClient(List<String> configs) {
        client = new ClientApplication("10.0.2.2", 14711, this, configs);
    }

    public boolean login(String userName, Consumer<String> consumer) {
        this.consumer = consumer;
        return client.login(userName);
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    @Override
    public void accept(String s) {
        consumer.accept(s);
    }
}

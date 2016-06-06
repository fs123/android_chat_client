package ch.fhnw.ws6.chat;

import java.util.List;

import ch.fhnw.ws6.chat.actorsystem.ClientApplication;
import ch.fhnw.ws6.chat.actorsystem.Consumer;

public class ChatClient implements Consumer<String> {

    private Consumer<String> consumer;
    private ClientApplication client;

    public ChatClient(String ip, String port, List<String> configs) {
        client = new ClientApplication(ip, Integer.parseInt(port), this, configs);
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

    public void stop() {
        client.logout();
        client.stop();
    }
}

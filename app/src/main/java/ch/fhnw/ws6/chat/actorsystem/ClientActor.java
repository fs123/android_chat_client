package ch.fhnw.ws6.chat.actorsystem;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import sadeghi.chat.events.ChatLoginRequest;
import sadeghi.chat.events.ChatMessageFromServer;
import sadeghi.chat.events.ChatMessageToServer;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ClientActor extends UntypedActor {
	private static String REMOTE_PATTERN = "akka.tcp://%s@%s:%d/user/serverActor";
	
	private static final Timeout TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));
	
	// selection, even the actor is restarted, this selection is still valid
	// but this requires some performance
	private final ActorSelection server;

	private Consumer<String> answerHandler;

	public ClientActor(String host, int port, Consumer<String> answerHandler) {
		this.answerHandler = answerHandler;
		if (host == null || host.isEmpty()) {
			throw new IllegalArgumentException("HOST and/or PORT missing.");
		}

		String serverRemotePath = String.format(REMOTE_PATTERN, 
				getContext().system().settings().config().getString("chat.server.actor.name"), 
				host, port);
		server = getContext().actorSelection(serverRemotePath);
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof ChatLoginRequest) {
			Future<Object> future = Patterns.ask(server, message, TIMEOUT);
			// we forward the answer/response to the inbox (actor-ref), which has send us the login-request initial
			Patterns.pipe(future, getContext().dispatcher()).to(getSender());
		} else if (message instanceof ChatMessageFromServer) {
			answerHandler.accept(((ChatMessageFromServer) message).from + ": " + ((ChatMessageFromServer) message).message);
		} else if (message instanceof ChatMessageToServer) {
			server.tell(message, self());
			answerHandler.accept( "ICH: " + ((ChatMessageToServer) message).message);
		} else {
			unhandled(message);
		}
	}

}
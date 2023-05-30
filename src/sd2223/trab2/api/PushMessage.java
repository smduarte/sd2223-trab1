package sd2223.trab2.api;

import java.util.Set;

public class PushMessage {
	private Message message;
	private Set<String> subscribers;
	
	public PushMessage() {
	}

	public PushMessage(Set<String> subs, Message msg ) {
		this.subscribers = subs;
		this.message = msg;
	}
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Set<String> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<String> subscribers) {
		this.subscribers = subscribers;
	}
	
}

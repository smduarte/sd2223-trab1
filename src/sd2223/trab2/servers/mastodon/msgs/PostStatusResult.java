package sd2223.trab2.servers.mastodon.msgs;

import sd2223.trab2.api.Message;
import sd2223.trab2.servers.Domain;

import java.time.Instant;

public record PostStatusResult(String id, String content, String created_at, MastodonAccount account) {

	private static final int HTML_PARAGRAPH_START_SIZE = 3; // <p> has 3 chars
	private static final int HTML_PARAGRAPH_END_SIZE = 4;   // </p> has 4 chars

	public long getId() {
		return Long.parseLong(id);
	}

	long getCreationTime() {
		Instant instant = Instant.parse(created_at);
		return instant.toEpochMilli();
	}

	public String getText() {
		int l = content.length();
		return content.substring(HTML_PARAGRAPH_START_SIZE, l - HTML_PARAGRAPH_END_SIZE);
	}

	public Message toMessage() {
		var m = new Message( getId(), account.getUsername(), Domain.get(), getText());
		m.setCreationTime( getCreationTime() );
		return m;
	}
}
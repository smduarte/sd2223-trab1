package sd2223.trab2.api;

/**
 * Represents a message in the system.
 */
public class Message {

	private long id;
	private String user;
	private String domain;
	private long creationTime;
	private String text;

	public Message() {
		this.id = -1;
		this.user = null;
		this.domain = null;
		this.creationTime = -1;
		this.text = null;
	}
	public Message(long id, String user, String domain, String text) {
		this.id = id;
		this.user = user;
		this.domain = domain;
		this.creationTime = System.currentTimeMillis();
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

//	@Override
//	public String toString() {
//		return "Message [id=" + id + ", user=" + user + ", domain=" + domain + ", creationTime=" + creationTime
//				+ ", text=" + text + "]";
//	}
	
	@Override
	public String toString() {
		return "Msg[" + id + "]";
	}

}

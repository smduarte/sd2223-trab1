package sd2223.trab1.api.java;

import java.util.List;

import sd2223.trab1.api.Message;

public interface FeedsPull extends Feeds {
		Result<List<Message>> pull_getTimeFilteredPersonalFeed(String user, long time);
}

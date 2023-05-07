package sd2223.trab1.api.java;

import sd2223.trab1.api.PushMessage;

public interface FeedsPush extends Feeds {
		Result<Void> push_updateFollowers(String user, String follower, boolean following);

		Result<Void> push_PushMessage(PushMessage msg);
}

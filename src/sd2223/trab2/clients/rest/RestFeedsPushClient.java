package sd2223.trab2.clients.rest;


import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sd2223.trab2.api.PushMessage;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.api.java.FeedsPush;

public class RestFeedsPushClient extends RestFeedsClient implements FeedsPush {

	public RestFeedsPushClient(String serverURI) {
		super(serverURI);
	}

	@Override
	public Result<Void> push_PushMessage(PushMessage msg) {
		return super.reTry(() -> clt_pushMessage(msg));
	}
	
	@Override
	public Result<Void> push_updateFollowers(String user, String follower, boolean following) {
		return super.reTry(() -> clt_updateFollowers(user, follower, following));
	}
	
	private Result<Void> clt_pushMessage(PushMessage pm) {
		Response r = target.request()
				.post(Entity.entity(pm, MediaType.APPLICATION_JSON));

		return super.toJavaResult(r, Void.class);
	}
	
	private Result<Void> clt_updateFollowers(String user, String follower, boolean following) {
		Response r = target.path("followers").path(user).path(follower)
				.request()
				.put(Entity.entity(following, MediaType.APPLICATION_JSON));

		return super.toJavaResult(r, Void.class);
	}
}

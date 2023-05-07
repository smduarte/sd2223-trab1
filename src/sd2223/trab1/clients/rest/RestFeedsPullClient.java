package sd2223.trab1.clients.rest;

import java.util.List;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.FeedsPull;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.rest.FeedsService;

public class RestFeedsPullClient extends RestFeedsClient implements FeedsPull {
	
	public RestFeedsPullClient(String serverURI) {
		super(serverURI);
	}

	@Override
	public Result<List<Message>> pull_getTimeFilteredPersonalFeed(String user, long time) {
		return super.reTry(() -> clt_getTimeFilteredPersonalFeed(user, time));
	}
	
	public Result<List<Message>> clt_getTimeFilteredPersonalFeed(String user, long time) {
		Response r = target.path(PERSONAL).path( user )
				.queryParam(FeedsService.TIME, time)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		return super.toJavaResult(r, new GenericType<List<Message>>() {});
	}
}

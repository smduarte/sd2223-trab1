
package sd2223.trab1.servers.java;

import static sd2223.trab1.api.java.Result.error;
import static sd2223.trab1.api.java.Result.ok;
import static sd2223.trab1.api.java.Result.ErrorCode.BAD_REQUEST;
import static sd2223.trab1.api.java.Result.ErrorCode.NOT_FOUND;
import static sd2223.trab1.api.java.Result.ErrorCode.TIMEOUT;
import static sd2223.trab1.clients.Clients.FeedsPullClients;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.FeedsPull;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.servers.Domain;

public class JavaFeedsPull extends JavaFeedsCommon<FeedsPull> implements FeedsPull {
	private static final long FEEDS_CACHE_EXPIRATION = 3000;

	public JavaFeedsPull( ){
		super( new JavaFeedsPullPreconditions());
	}
	
	final LoadingCache<FeedInfoKey, Result<List<Message>>> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(Duration.ofMillis(FEEDS_CACHE_EXPIRATION)).removalListener((e) -> {
			}).build(new CacheLoader<>() {
				@Override
				public Result<List<Message>> load(FeedInfoKey info) throws Exception {
					var res = FeedsPullClients.get(info.domain()).pull_getTimeFilteredPersonalFeed(info.user(), info.time());
					if (res.error() == TIMEOUT)
						return error(BAD_REQUEST);

					return res;
				}
			});

	@Override
	public Result<Message> getMessage(String user, long mid) {
		
		var preconditionsResult = preconditions.getMessage(user, mid);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		FeedInfo ufi = feeds.get(user);
		if( ufi == null )
			return error( NOT_FOUND );
		
		synchronized (ufi.user()) {
			if (ufi.messages().contains(mid))
				return ok(messages.get(mid));

			var list = getMessages(user, -1L);
			if (!list.isOK())
				return error(list.error());

			var res = list.value().stream().filter(m -> m.getId() == mid).findFirst();
			return res.isPresent() ? ok(res.get()) : error(NOT_FOUND);
		}
	}

	@Override
	public Result<List<Message>> getMessages(String user, long time) {

		var preconditionsResult = preconditions.getMessages(user, time);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		FeedInfo ufi = feeds.get(user);
		if( ufi == null )
			return ok( Collections.emptyList() );
		
		synchronized (ufi.user()) {
			var msgs = new ArrayList<Message>();
			msgs.addAll( ufi.messages().stream().map( messages::get).filter(m -> m.getCreationTime() > time).toList());

			for (var s : ufi.following())
				msgs.addAll(getCachedPersonalFeed(s, time));

			return ok(msgs);
		}
	}
	
	public Result<List<Message>> pull_getTimeFilteredPersonalFeed(String user, long time) {
		
		var preconditionsResult = preconditions.pull_getTimeFilteredPersonalFeed(user, time);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		return ok(super.getTimeFilteredPersonalFeed(user, time));
	}

	private List<Message> getCachedPersonalFeed(String name, long time) {
		try {
			if (FeedUser.from(name).isRemoteUser())
				return cache.get( FeedInfoKey.from(name, time) ).value();
			else
				return super.getTimeFilteredPersonalFeed(name, time);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	@Override
	protected void deleteFromUserFeed( String user, Set<Long> mids ) {
		messages.keySet().removeAll( mids );
	}

	
	static record FeedInfoKey(String user, String domain, long time) {
		static FeedInfoKey from(String name, long time) {
			var idx = name.indexOf('@');
			var domain = idx < 0 ? Domain.get() : name.substring(idx + 1);
			return new FeedInfoKey(name, domain, time);
		}
	}
}

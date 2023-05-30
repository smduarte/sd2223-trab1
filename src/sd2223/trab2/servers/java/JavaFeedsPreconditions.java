package sd2223.trab2.servers.java;
import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.redirected;
import static sd2223.trab2.api.java.Result.ErrorCode.BAD_REQUEST;
import static sd2223.trab2.api.java.Result.ErrorCode.FORBIDDEN;
import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;
import static sd2223.trab2.api.java.Result.ErrorCode.TIMEOUT;
import static sd2223.trab2.clients.Clients.FeedsClients;
import static sd2223.trab2.clients.Clients.UsersClients;

import java.time.Duration;
import java.util.List;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.User;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.api.java.Result.ErrorCode;
import sd2223.trab2.servers.java.JavaFeedsCommon.FeedUser;

public abstract class JavaFeedsPreconditions implements Feeds {

	private static final long USER_CACHE_EXPIRATION = 3000;
	
	protected final LoadingCache<FeedUser, Result<User>> users = CacheBuilder.newBuilder()
			.expireAfterWrite(Duration.ofMillis(USER_CACHE_EXPIRATION)).removalListener((e) -> {
			}).build(new CacheLoader<>() {
				@Override
				public Result<User> load(FeedUser info) throws Exception {
					var res = UsersClients.get(info.domain()).getUser(info.name(), info.pwd());
					if (res.error() == TIMEOUT)
						return error(BAD_REQUEST);
					return res;
				}
			});
	
	@Override
	public Result<Long> postMessage(String user, String pwd, Message msg) {
		if (badParams(user, pwd, msg) )
			return error(BAD_REQUEST);

		var u = FeedUser.from( user, pwd );
		if( u.isRemoteUser() )
			return error(BAD_REQUEST);
			
		var res = getUser( u );
		if (!res.isOK())
			return error(res.error());
		return ok();
	}

	@Override
	public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
		if (badParams(user, pwd))
			return error(BAD_REQUEST);

		var u = FeedUser.from( user, pwd);
		if( u.isRemoteUser() )
			return error(BAD_REQUEST);
		
		var res = getUser( u );
		if (!res.isOK())
			return error(res.error());
		
		return ok();
	}

	@Override
	public Result<Message> getMessage(String user, long mid) {
		if (user == null)
			return error(BAD_REQUEST);
		
		var ui = FeedUser.from(user);
		if (ui.isRemoteUser())
			return redirected(FeedsClients.get(ui.domain()).getMessage(user, mid));

		var ures = getUser(ui);
		if (ures.error() == NOT_FOUND )
			return error(NOT_FOUND);
		
		return ok();
	}

	@Override
	public Result<List<Message>> getMessages(String user, long time) {
		if (user == null)
			return error(BAD_REQUEST);
		
		var ui = FeedUser.from(user);
		if (ui.isRemoteUser())
			return redirected(FeedsClients.get(ui.domain()).getMessages(user, time));

		var ures = getUser(ui);
		if (ures.error() == NOT_FOUND )
			return error(NOT_FOUND);
		
		return ok();
	}
	
	@Override
	public Result<List<String>> listSubs(String user) {
		if( user == null )
			return error(BAD_REQUEST);
		
		var ures = getUser( FeedUser.from(user)).error();
		if (ures == NOT_FOUND)
			return error(NOT_FOUND);

		if (ures != FORBIDDEN)
			return error(BAD_REQUEST);
		
		return ok();
	}

	@Override
	public Result<Void> deleteUserFeed(String user) {
		if( user == null )
			return error(BAD_REQUEST);
				
		return ok();
	}
	
	protected boolean badParams(Object... params) {
		for (var p : params)
			if (p == null)
				return true;
		return false;
	}
	
	protected Result<User> getUser( FeedUser info) {
		try {
			return users.get(info);
		} catch (Exception x) {
			x.printStackTrace();
			return error(ErrorCode.INTERNAL_ERROR);
		}
	}
}

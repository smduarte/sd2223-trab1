package sd2223.trab2.servers.java;
import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.ErrorCode.BAD_REQUEST;
import static sd2223.trab2.api.java.Result.ErrorCode.FORBIDDEN;
import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;
import static sd2223.trab2.clients.Clients.FeedsPushClients;

import sd2223.trab2.api.PushMessage;
import sd2223.trab2.api.java.FeedsPush;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.servers.java.JavaFeedsCommon.FeedUser;

public class JavaFeedsPushPreconditions extends JavaFeedsPreconditions implements FeedsPush {
	
	@Override
	public Result<Void> subUser(String user, String userSub, String pwd) {
		var u = FeedUser.from( user, pwd );
		var ures = getUser( u ).error();
		if (ures == NOT_FOUND || ures == FORBIDDEN)
			return error(ures);

		var u2 = FeedUser.from( userSub );
		var ures2 = FeedsPushClients.get(u2.domain()).push_updateFollowers(userSub, user, true);
		if( ures2.error() == NOT_FOUND)
			return error(NOT_FOUND);	

		return ok();
	}

	@Override
	public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
		var u = FeedUser.from( user, pwd );
		var ures = getUser( u ).error();
		if (ures == NOT_FOUND || ures == FORBIDDEN)
			return error(ures);

		var u2 = FeedUser.from( userSub );
		var ures2 = FeedsPushClients.get(u2.domain()).push_updateFollowers(userSub, user, false);
		if( ures2.error() == NOT_FOUND)
			return error(NOT_FOUND);	

		return ok();
	}


	@Override
	public Result<Void> push_updateFollowers(String user, String follower, boolean following) {
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
	public Result<Void> push_PushMessage(PushMessage msg) {
		return ok();
	}
}

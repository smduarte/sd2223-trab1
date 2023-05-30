package sd2223.trab2.servers.java;

import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;
import static sd2223.trab2.clients.Clients.FeedsPushClients;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.PushMessage;
import sd2223.trab2.api.java.FeedsPush;
import sd2223.trab2.api.java.Result;

public class JavaFeedsPush extends JavaFeedsCommon<FeedsPush> implements FeedsPush {

	private static final long PERMANENT_REMOVAL_DELAY = 30;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	final Map<Long, Set<String>> msgs2users = new ConcurrentHashMap<>();

	public JavaFeedsPush( ){
		super( new JavaFeedsPushPreconditions());
	}
	
	@Override
	public Result<Long> postMessage(String user, String pwd, Message msg) {
		var res = super.postMessage(user, pwd, msg);
		if (res.isOK()) {
			var followees = feeds.get(user).followees();

			var subscribers = followees.stream()
					.map(FeedUser::from)
					.collect(Collectors.groupingBy(FeedUser::domain, Collectors.mapping(FeedUser::user, Collectors.toSet())));

			scheduler.execute( () -> {
				for( var e : subscribers.entrySet()) {
					var domain = e.getKey();
					var users = e.getValue();
					while(! FeedsPushClients.get(domain).push_PushMessage(new PushMessage(users, msg)).isOK());						
				}
			});
		}
		return res;
	}
	
	@Override
	public Result<Message> getMessage(String user, long mid) {
		var preconditionsResult = preconditions.getMessage(user, mid);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		var ufi = feeds.get(user);
		if (ufi == null)
			return error(NOT_FOUND);
		
		synchronized (ufi.user()) {
			if (!ufi.messages().contains(mid))
				return error(NOT_FOUND);

			return ok(messages.get(mid));
		}
	}

	@Override
	public Result<List<Message>> getMessages(String user, long time) {
		var preconditionsResult = preconditions.getMessages(user, time);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		return ok(super.getTimeFilteredPersonalFeed(user, time)) ;
	}
	
	@Override
	public Result<Void> push_updateFollowers(String user, String follower, boolean following) {
		
		var preconditionsResult = preconditions.push_updateFollowers(user, follower, following);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		var followees = feeds.computeIfAbsent(user, FeedInfo::new ).followees();
		
		if( following )
			followees.add(follower);
		else
			followees.remove(follower);
		
		return ok();
	}

	@Override
	public Result<Void> push_PushMessage(PushMessage pm) {
		var msg = pm.getMessage();
		super.messages.put(msg.getId(), msg);

		for( var s : pm.getSubscribers())
			feeds.computeIfAbsent(s, FeedInfo::new).messages().add(msg.getId());
		
		msgs2users.computeIfAbsent(msg.getId(), (k) -> ConcurrentHashMap.newKeySet()).addAll(pm.getSubscribers());
		return ok();
	}
	
	@Override
	protected void deleteFromUserFeed( String user, Set<Long> mids ) {
		for( var mid : mids ) {
			var references = msgs2users.get(mid);
			if (references != null && references.remove(user) && references.isEmpty()) {
				scheduler.schedule(() -> {
					super.messages.remove(mid);					
				}, PERMANENT_REMOVAL_DELAY, TimeUnit.SECONDS);
			}
		}
	}
}

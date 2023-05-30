package sd2223.trab2.servers.java;

import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.servers.Domain;

public abstract class JavaFeedsCommon<T extends Feeds>  implements Feeds {
	private static final long FEEDS_MID_PREFIX= 1_000_000_000;

	protected AtomicLong serial = new AtomicLong(Domain.uuid() * FEEDS_MID_PREFIX);

	final protected T preconditions;

	protected JavaFeedsCommon( T preconditions){
		this.preconditions = preconditions;
	}
	
	protected Map<Long, Message> messages = new ConcurrentHashMap<>();
	protected Map<String, FeedInfo> feeds = new ConcurrentHashMap<>();

	static protected record FeedInfo(String user, Set<Long> messages, Set<String> following, Set<String> followees) {
		public FeedInfo(String user) {
			this(user, new HashSet<>(), new HashSet<>(), ConcurrentHashMap.newKeySet());
		}
	}
	
	@Override
	public Result<Long> postMessage(String user, String pwd, Message msg) {
		
		var preconditionsResult = preconditions.postMessage(user, pwd, msg);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;
					
		Long mid = serial.incrementAndGet();
		msg.setId(mid);
		msg.setCreationTime(System.currentTimeMillis());

		FeedInfo ufi = feeds.computeIfAbsent(user, FeedInfo::new );
		synchronized (ufi.user()) {
			ufi.messages().add(mid);
			messages.putIfAbsent(mid, msg);
		}
		return Result.ok(mid);
	}

	@Override
	public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {

		var preconditionsResult = preconditions.removeFromPersonalFeed(user, mid, pwd);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		var ufi = feeds.get(user);
		if( ufi == null )
			return error(NOT_FOUND);
			
		synchronized (ufi.user()) {
			if (!ufi.messages().remove(mid))
				return error(NOT_FOUND);
		}
		
		deleteFromUserFeed( user, Set.of(mid) );

		return ok();
	}
	
	
	protected List<Message> getTimeFilteredPersonalFeed(String user, long time) {
		var ufi = feeds.computeIfAbsent(user, FeedInfo::new );
		synchronized (ufi.user()) {
			return ufi.messages().stream().map(messages::get).filter(m -> m.getCreationTime() > time).toList();
		}
	}
	
	@Override
	public Result<Void> subUser(String user, String userSub, String pwd) {
		
		var preconditionsResult = preconditions.subUser(user, userSub, pwd);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;


		var ufi = feeds.computeIfAbsent(user, FeedInfo::new );
		synchronized (ufi.user()) {
			ufi.following().add(userSub);
		}
		return ok();
	}

	@Override
	public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
		
		var preconditionsResult = preconditions.unsubscribeUser(user, userSub, pwd);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		FeedInfo ufi = feeds.computeIfAbsent(user, FeedInfo::new);
		synchronized (ufi.user()) {
			ufi.following().remove(userSub);
		}
		return ok();
	}

	@Override
	public Result<List<String>> listSubs(String user) {
		
		var preconditionsResult = preconditions.listSubs(user);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		FeedInfo ufi = feeds.computeIfAbsent(user, FeedInfo::new);
		synchronized (ufi.user()) {
			return ok(new ArrayList<>(ufi.following()));
		}
	}
	
	@Override
	public Result<Void> deleteUserFeed(String user) {
		
		var preconditionsResult = preconditions.deleteUserFeed(user);
		if( ! preconditionsResult.isOK() )
			return preconditionsResult;

		FeedInfo ufi = feeds.remove(user);
		if (ufi == null)
			return error(NOT_FOUND);

		synchronized (ufi.user()) {
			deleteFromUserFeed(user, ufi.messages());
			for (var u : ufi.followees())
				ufi.following().remove(u);
		}
		return ok();
	}
	
	
	static public record FeedUser(String user, String name, String pwd, String domain) {
		private static final String EMPTY_PASSWORD = "";

		public static FeedUser from(String name, String pwd) {
			var idx = name.indexOf('@');
			var n = idx < 0 ? name : name.substring(0, idx);
			var d = idx < 0 ? Domain.get() : name.substring(idx + 1);
			return new FeedUser(name, n, pwd, d);
		}

		public static FeedUser from(String name) {
			return FeedUser.from(name, EMPTY_PASSWORD);
		}
				
		boolean isLocalUser() {
			return domain.equals(Domain.get());			
		}
		
		public boolean isRemoteUser() {
			return ! isLocalUser();
		}

	};
	
	abstract protected void deleteFromUserFeed( String user, Set<Long> mids );
}

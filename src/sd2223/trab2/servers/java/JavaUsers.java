package sd2223.trab2.servers.java;

import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.ErrorCode.BAD_REQUEST;
import static sd2223.trab2.api.java.Result.ErrorCode.CONFLICT;
import static sd2223.trab2.api.java.Result.ErrorCode.FORBIDDEN;
import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;
import static sd2223.trab2.clients.Clients.FeedsClients;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sd2223.trab2.api.User;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.api.java.Users;
import sd2223.trab2.servers.Domain;


public class JavaUsers implements Users {
	final protected Map<String, User> users = new ConcurrentHashMap<>();
	final ExecutorService executor = Executors.newCachedThreadPool();
	
	@Override
	public Result<String> createUser(User user) {
		if( user.hasNullFields())
			return error( BAD_REQUEST );
		
		var res = users.putIfAbsent(user.getName(), user);
		if (res != null)
			return error(CONFLICT);
		
		user.setDomain( Domain.get() );
		
		return ok(user.getName() + "@" + Domain.get());
	}

	@Override
	public Result<User> getUser(String name, String pwd) {
		if (badParams(name, pwd))
			return error(BAD_REQUEST);
		
		var user = users.get(name);
		if (user == null)
			return error(NOT_FOUND);
		
		if (wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else
			return ok(user);
	}

	@Override
	public Result<User> updateUser(String name, String pwd, User data) {
		if (badParams(name, pwd))
			return error(BAD_REQUEST);
		
		var user = users.get(name);		
		if (user == null)
			return error(NOT_FOUND);
		
		if (wrongPassword(user, pwd))
			return error(FORBIDDEN);
		
		if( User.badData(name, data))
			return error(BAD_REQUEST );
		
		user.updateUser(data);
		return ok(user);
	
	}

	@Override
	public Result<User> deleteUser(String name, String pwd) {
		if (badParams(name, pwd))
			return error(BAD_REQUEST);
		
		var user = users.get(name);
		if (user == null)
			return error(NOT_FOUND);
		
		if (wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else {
			users.remove(name);
			FeedsClients.get( Domain.get()).deleteUserFeed(name + "@" + Domain.get()); // synchronous op to avoid 7c and 11c SUSPECT...
			executor.execute(()->{
//				FeedsClients.get( Domain.get()).deleteUserFeed(name + "@" + Domain.get()); // asynchronous op is preferable, but produces 7c and 11c SUSPECT...
			});
			return ok(user);
		}
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		if( badParam( pattern))
			return error(BAD_REQUEST);
					
		var hits = users.values()
			.stream()
			.filter( u -> u.getName().contains(pattern) )
			.map( User::secureCopy )
			.toList();
		
		return ok(hits);
	}

	private boolean badParam( String str ) {
		return str == null;
	}

	private boolean badParams( String ... values ) {
		for( var str : values )
			if( str == null )
				return true;
		return false;
	}
	
	private boolean wrongPassword(User user, String pwd) {
		return !user.getPwd().equals( pwd);
	}
}

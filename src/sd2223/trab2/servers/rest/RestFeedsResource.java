package sd2223.trab2.servers.rest;

import java.util.List;

import jakarta.inject.Singleton;
import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.rest.FeedsService;

@Singleton
public abstract class RestFeedsResource<T extends Feeds> extends RestResource implements FeedsService {

	public RestFeedsResource(T impl ) {
		this.impl = impl;
	}

	final protected T impl;
	
	@Override
	public long postMessage(String user, String pwd, Message msg) {
		return super.fromJavaResult( impl.postMessage(user, pwd, msg));
	}
	
	@Override
	public void removeFromPersonalFeed(String user, long mid, String pwd) {
		super.fromJavaResult( impl.removeFromPersonalFeed(user, mid, pwd));
	}
	
	@Override
	public Message getMessage(String user, long mid) {
		return super.fromJavaResult( impl.getMessage(user, mid));
	}
	
	@Override
	public List<Message> getMessages(String user, long time) {
		return super.fromJavaResult( impl.getMessages(user, time));
	}
	@Override
	public void subUser(String user, String userSub, String pwd) {
		super.fromJavaResult( impl.subUser(user, userSub, pwd));
	}
	
	@Override
	public void unsubscribeUser(String user, String userSub, String pwd) {
		super.fromJavaResult( impl.unsubscribeUser(user, userSub, pwd));
	}
	
	@Override
	public List<String> listSubs(String user) {
		return super.fromJavaResult( impl.listSubs(user));
	}

	@Override
	public void deleteUserFeed(String user) {
		super.fromJavaResult( impl.deleteUserFeed(user));
	}
}

package sd2223.trab2.servers.soap;

import java.util.List;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.soap.FeedsException;
import sd2223.trab2.api.soap.FeedsService;

public class SoapFeedsWebService<T extends Feeds> extends SoapWebService<FeedsException> implements FeedsService {

	final protected T impl;
	protected SoapFeedsWebService(T impl) {
		super((result)-> new FeedsException( result.error().toString()));
		this.impl = impl;
	}

	@Override
	public long postMessage(String user, String pwd, Message msg) throws FeedsException {
		return super.fromJavaResult( impl.postMessage(user, pwd, msg));
	}

	@Override
	public void removeFromPersonalFeed(String user, long mid, String pwd) throws FeedsException {
		super.fromJavaResult( impl.removeFromPersonalFeed(user, mid, pwd));
	}

	@Override
	public Message getMessage(String user, long mid) throws FeedsException {
		return super.fromJavaResult( impl.getMessage(user, mid));
	}

	@Override
	public List<Message> getMessages(String user, long time) throws FeedsException {
		return super.fromJavaResult( impl.getMessages(user, time));
	}

	@Override
	public void subUser(String user, String userSub, String pwd) throws FeedsException {
		super.fromJavaResult( impl.subUser(user, userSub, pwd));
	}

	@Override
	public void unsubscribeUser(String user, String userSub, String pwd) throws FeedsException {
		super.fromJavaResult( impl.unsubscribeUser(user, userSub, pwd));
	}

	@Override
	public List<String> listSubs(String user) throws FeedsException {
		return super.fromJavaResult( impl.listSubs(user));
	}

	@Override
	public void deleteUserFeed(String user) throws FeedsException {
		super.fromJavaResult( impl.deleteUserFeed(user));
	}
}

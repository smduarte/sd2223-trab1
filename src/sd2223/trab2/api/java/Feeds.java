package sd2223.trab2.api.java;

import java.util.List;

import sd2223.trab2.api.Message;

public interface Feeds {
	static String SERVICENAME = "feeds";

	Result<Long> postMessage(String user, String pwd, Message msg);

	Result<Void> removeFromPersonalFeed(String user, long mid, String pwd);

	Result<Message> getMessage(String user, long mid);

	Result<List<Message>> getMessages(String user, long time);

	Result<Void> subUser(String user, String userSub, String pwd);

	Result<Void> unsubscribeUser(String user, String userSub, String pwd);

	Result<List<String>> listSubs(String user);

	Result<Void> deleteUserFeed(String user);

}

package sd2223.trab1.clients;

import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.FeedsPull;
import sd2223.trab1.api.java.FeedsPush;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.clients.rest.RestFeedsClient;
import sd2223.trab1.clients.rest.RestFeedsPullClient;
import sd2223.trab1.clients.rest.RestFeedsPushClient;
import sd2223.trab1.clients.rest.RestUsersClient;
import sd2223.trab1.clients.soap.SoapFeedsClient;
import sd2223.trab1.clients.soap.SoapFeedsPullClient;
import sd2223.trab1.clients.soap.SoapFeedsPushClient;
import sd2223.trab1.clients.soap.SoapUsersClient;

public class Clients {
	public static final ClientFactory<Users> UsersClients = new ClientFactory<>(Users.SERVICENAME, RestUsersClient::new, SoapUsersClient::new);
	
	public static final ClientFactory<FeedsPull> FeedsPullClients = new ClientFactory<>(Feeds.SERVICENAME, RestFeedsPullClient::new, SoapFeedsPullClient::new);

	public static final ClientFactory<FeedsPush> FeedsPushClients = new ClientFactory<>(Feeds.SERVICENAME, RestFeedsPushClient::new, SoapFeedsPushClient::new);	
	
	public static final ClientFactory<Feeds> FeedsClients = new ClientFactory<>(Feeds.SERVICENAME, RestFeedsClient::new, SoapFeedsClient::new) ; 
}

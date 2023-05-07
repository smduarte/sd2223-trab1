package sd2223.trab1.servers.soap;

import jakarta.jws.WebService;
import sd2223.trab1.api.PushMessage;
import sd2223.trab1.api.java.FeedsPush;
import sd2223.trab1.api.soap.FeedsException;
import sd2223.trab1.api.soap.push.FeedsService;
import sd2223.trab1.servers.java.JavaFeedsPush;

@WebService(serviceName=FeedsService.NAME, targetNamespace=FeedsService.NAMESPACE, endpointInterface=FeedsService.INTERFACE, portName="caca")
public class SoapFeedsPushWebService extends SoapFeedsWebService<FeedsPush> implements FeedsService {

	public SoapFeedsPushWebService() {
		super( new JavaFeedsPush() );
	}

	@Override
	public void push_PushMessage(PushMessage msg) throws FeedsException {
		super.fromJavaResult( impl.push_PushMessage(msg));
	}

	@Override
	public void push_updateFollowers(String user, String follower, boolean following) throws FeedsException{
		super.fromJavaResult( impl.push_updateFollowers(user, follower, following));
	}

}
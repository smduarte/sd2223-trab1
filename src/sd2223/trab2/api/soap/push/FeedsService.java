package sd2223.trab2.api.soap.push;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import sd2223.trab2.api.PushMessage;
import sd2223.trab2.api.soap.FeedsException;

@WebService(serviceName=FeedsService.NAME, targetNamespace=FeedsService.NAMESPACE, endpointInterface=FeedsService.INTERFACE)
public interface FeedsService extends sd2223.trab2.api.soap.FeedsService {
	static final String INTERFACE = "sd2223.trab2.api.soap.push.FeedsService";
	
	@WebMethod
	void push_PushMessage( PushMessage msg ) throws FeedsException ; 
	
	@WebMethod
	void push_updateFollowers(String user, String follower, boolean following) throws FeedsException;		
}
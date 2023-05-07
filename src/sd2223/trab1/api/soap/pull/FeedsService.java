package sd2223.trab1.api.soap.pull;

import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.soap.FeedsException;

@WebService(serviceName=FeedsService.NAME, targetNamespace=FeedsService.NAMESPACE, endpointInterface=FeedsService.INTERFACE)
public interface FeedsService extends sd2223.trab1.api.soap.FeedsService {
	static final String INTERFACE = "sd2223.trab1.api.soap.pull.FeedsService";

	@WebMethod
	List<Message> pull_getTimeFilteredPersonalFeed(String user, long time) throws FeedsException;		
}

package sd2223.trab1.clients.soap;

import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.FeedsPull;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.soap.pull.FeedsService;

public class SoapFeedsPullClient extends SoapFeedsClient implements FeedsPull {

	public SoapFeedsPullClient(String serverURI) {
		super(serverURI);
	}

	private FeedsService stub;
	synchronized protected FeedsService stub() {
		if (stub == null) {
			QName QNAME = new QName(FeedsService.NAMESPACE, FeedsService.NAME);
			Service service = Service.create(super.toURL(super.uri + WSDL), QNAME);			
			this.stub = service.getPort(sd2223.trab1.api.soap.pull.FeedsService.class);
			super.setTimeouts( (BindingProvider) stub);
		}
		Thread.dumpStack();
		return stub;
	}
	
	@Override
	public Result<List<Message>> pull_getTimeFilteredPersonalFeed(String user, long time) {
		return super.reTry( () -> super.toJavaResult( () -> stub().pull_getTimeFilteredPersonalFeed(user, time) ) );
	}
}

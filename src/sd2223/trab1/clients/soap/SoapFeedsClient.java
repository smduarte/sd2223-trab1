package sd2223.trab1.clients.soap;

import static sd2223.trab1.api.java.Result.error;
import static sd2223.trab1.api.java.Result.ErrorCode.NOT_IMPLEMENTED;

import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.soap.FeedsService;

public class SoapFeedsClient<T extends FeedsService> extends SoapClient implements Feeds {
	
	public SoapFeedsClient( String serverURI ) {
		super( serverURI );
	}

	private FeedsService stub;
	synchronized protected FeedsService stub() {
		if (stub == null) {
			QName QNAME = new QName(FeedsService.NAMESPACE, FeedsService.NAME);
			Service service = Service.create(super.toURL(super.uri + WSDL), QNAME);			
			this.stub = service.getPort(sd2223.trab1.api.soap.FeedsService.class);
			super.setTimeouts( (BindingProvider) stub);
		}
		return stub;
	}
	
	@Override
	public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
		return super.reTry( () -> super.toJavaResult( () -> stub().removeFromPersonalFeed(user, mid, pwd)) );
	}

	@Override
	public Result<Message> getMessage(String user, long mid) {
		return super.reTry( () -> super.toJavaResult( () -> stub().getMessage(user, mid) ) );
	}

	@Override
	public Result<List<Message>> getMessages(String user, long time) {
		return super.reTry( () -> super.toJavaResult( () -> stub().getMessages(user, time) ) );
	}	

	@Override
	public Result<Void> deleteUserFeed(String user) {
		return super.reTry( () -> super.toJavaResult( () -> stub().deleteUserFeed(user) ) );
	}

	@Override
	public Result<Long> postMessage(String user, String pwd, Message msg) {
		return error(NOT_IMPLEMENTED);
	}

	@Override
	public Result<Void> subUser(String user, String userSub, String pwd) {
		return error(NOT_IMPLEMENTED);
	}

	@Override
	public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
		return error(NOT_IMPLEMENTED);
	}

	@Override
	public Result<List<String>> listSubs(String user) {
		return error(NOT_IMPLEMENTED);
	}
}

package sd2223.trab1.servers.soap;


import java.util.logging.Logger;

import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.servers.Domain;
import utils.Args;

public class SoapFeedsServer extends AbstractSoapServer<SoapFeedsWebService<?>> {

	public static final int PORT = 14567;
	private static Logger Log = Logger.getLogger(SoapFeedsServer.class.getName());

	protected SoapFeedsServer() {
		super(false, Log, Feeds.SERVICENAME, PORT,  Args.valueOf("-push", true) ? new SoapFeedsPushWebService() : new SoapFeedsPullWebService() );
	}

	public static void main(String[] args) throws Exception {
		Args.use(args);		
		Domain.set( args[0], Long.valueOf(args[1]));
		new SoapFeedsServer().start();
	}
}

package sd2223.trab1.servers.rest;

import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.servers.Domain;
import utils.Args;


public class RestFeedsServer extends AbstractRestServer {
	public static final int PORT = 4567;
	
	private static Logger Log = Logger.getLogger(RestFeedsServer.class.getName());

	RestFeedsServer() {
		super( Log, Feeds.SERVICENAME, PORT);
	}
	
	@Override
	void registerResources(ResourceConfig config) {
		config.register( Args.valueOf("-push", true) ? RestFeedsPushResource.class : RestFeedsPullResource.class ); 
	}
	
	public static void main(String[] args) throws Exception {
		Args.use( args );
		Domain.set( args[0], Long.valueOf(args[1]));
		new RestFeedsServer().start();
	}	
}
package sd2223.trab2.servers.rest;

import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

import sd2223.trab2.api.java.Users;
import sd2223.trab2.servers.Domain;


public class RestUsersServer extends AbstractRestServer {
	public static final int PORT = 3456;
	
	private static Logger Log = Logger.getLogger(RestUsersServer.class.getName());

	RestUsersServer() {
		super( Log, Users.SERVICENAME, PORT);
	}
	
	
	@Override
	void registerResources(ResourceConfig config) {
		config.register( RestUsersResource.class ); 
//		config.register(new GenericExceptionMapper());
//		config.register(new CustomLoggingFilter());
	}
	
	
	public static void main(String[] args) throws Exception {
		Domain.set( args[0], Long.valueOf(args[1]));
		new RestUsersServer().start();
	}	
}
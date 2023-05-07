package sd2223.trab1.servers.java;

import java.util.logging.Logger;

import sd2223.trab1.servers.Domain;


public abstract class AbstractServer {
	protected static String SERVER_BASE_URI = "http://%s:%s%s";
	protected static final String INETADDR_ANY = "0.0.0.0";

	final protected Logger Log;
	final protected String service;
	final protected String serverURI;
	
	protected AbstractServer(Logger log, String service, String serverURI) {
		this.Log = log;
		this.serverURI = serverURI;
		this.service = Domain.get() + ":" + service;
	}
		
	abstract protected void start();
	
	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
	}
}

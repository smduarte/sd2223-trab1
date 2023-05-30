package sd2223.trab2.servers.soap;


import java.util.logging.Level;
import java.util.logging.Logger;

import sd2223.trab2.api.java.Users;
import sd2223.trab2.servers.Domain;

public class SoapUsersServer extends AbstractSoapServer<SoapUsersWebService> {

	public static final int PORT = 13456;
	private static Logger Log = Logger.getLogger(SoapUsersServer.class.getName());

	protected SoapUsersServer() {
		super(false, Log, Users.SERVICENAME, PORT,  new SoapUsersWebService() );
	}

	public static void main(String[] args) throws Exception {		
		Domain.set( args[0], 0);
		Log.setLevel(Level.INFO);
		new SoapUsersServer().start();
	}
}

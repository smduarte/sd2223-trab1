package sd2223.trab2.servers.rest;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import sd2223.trab2.discovery.Discovery;
import sd2223.trab2.servers.Domain;
import sd2223.trab2.servers.java.AbstractServer;
import utils.IP;

import javax.net.ssl.SSLContext;


public abstract class AbstractRestServer extends AbstractServer {

    protected static String SERVER_BASE_URI = "https://%s:%s/rest";
    protected Logger log;
    final protected String service;
    final protected String serverURI;


    private static final String REST_CTX = "/rest";

    protected AbstractRestServer(Logger log, String service, int port) {
        super(log, service, String.format(SERVER_BASE_URI, IP.hostname(), port, REST_CTX));
        this.log = log;
        this.service = Domain.get() + ":" + service;
        serverURI = String.format(SERVER_BASE_URI, IP.hostname(), port);

    }


    protected void start() throws NoSuchAlgorithmException {


        var uri = URI.create(serverURI.replace(IP.hostAddress(), "0.0.0.0"));

        ResourceConfig config = new ResourceConfig();

        registerResources(config);

        JdkHttpServerFactory.createHttpServer(uri, config, SSLContext.getDefault());

        Discovery.getInstance().announce(service, serverURI);
        Log.info(String.format("%s Server ready @ %s\n", service, serverURI));
    }

    abstract void registerResources(ResourceConfig config);
}

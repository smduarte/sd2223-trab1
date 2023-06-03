package sd2223.trab2.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.servers.Domain;
import utils.Args;

import java.util.logging.Logger;

public class RestProxyFeedsServer extends AbstractRestServer {
    public static final int PORT = 4568;

    private static Logger Log = Logger.getLogger(RestProxyFeedsServer.class.getName());

    protected RestProxyFeedsServer() {
        super(Log, Feeds.SERVICENAME, PORT);
    }

    @Override
    void registerResources(ResourceConfig config) {
        config.register(RestProxyFeedsResource.class);
    }

    public static void main(String[] args) throws Exception {

        Args.use(args);
        Domain.set(args[0], Long.parseLong(args[1]));
        new RestProxyFeedsServer().start();
    }
}
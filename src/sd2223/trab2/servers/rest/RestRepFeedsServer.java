package sd2223.trab2.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.servers.Domain;
import sd2223.trab2.servers.java.JavaFeedsPushPreconditions;
import sd2223.trab2.servers.kafka.sync.SyncPoint;
import utils.Args;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RestRepFeedsServer extends AbstractRestServer {

    public static final int PORT = 8080;

    private static Logger Log = Logger.getLogger(RestRepFeedsServer.class.getName());

    RestRepFeedsServer() {
        super(Log, Feeds.SERVICENAME, PORT);
    }

    @Override
    protected void registerResources(ResourceConfig config) {

        SyncPoint<String> sync = new SyncPoint<>();
        config.register(new RestRepFeedsResource(sync, new RepFeeds(new JavaFeedsPushPreconditions(), sync)));
        config.register(GenericExceptionMapper.class);

    }

    public static void main(String[] args) throws Exception {

        Args.use(args);
        Domain.set(args[0], Long.parseLong(args[1]));
        new RestRepFeedsServer().start();
    }
}



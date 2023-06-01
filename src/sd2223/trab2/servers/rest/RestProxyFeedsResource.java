package sd2223.trab2.servers.rest;

import jakarta.inject.Singleton;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.servers.mastodon.Mastodon;

@Singleton
public class RestProxyFeedsResource extends RestFeedsResource<Feeds>{
    public RestProxyFeedsResource() {
        super(Mastodon.getInstance());
    }
}
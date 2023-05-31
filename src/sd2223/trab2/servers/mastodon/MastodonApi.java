package sd2223.trab2.servers.mastodon;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class MastodonApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://mastodon.social/oauth/authorize";
    private static final String ACCESS_TOKEN_RESOURCE = "https://mastodon.social/api/v1/oauth/token";

    protected MastodonApi() {
    }

    private static class InstanceHolder {
        private static final MastodonApi INSTANCE = new MastodonApi();
    }

    public static MastodonApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}

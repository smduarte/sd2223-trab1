package sd2223.trab2.servers.mastodon;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.google.gson.reflect.TypeToken;
import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.servers.java.JavaFeedsPushPreconditions;
import sd2223.trab2.servers.mastodon.msgs.MastodonAccount;
import sd2223.trab2.servers.mastodon.msgs.PostStatusArgs;
import sd2223.trab2.servers.mastodon.msgs.PostStatusResult;
import utils.JSON;

import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;
import static sd2223.trab2.api.java.Result.ErrorCode.*;

public class Mastodon implements Feeds {


    static String MASTODON_NOVA_SERVER_URI = "http://10.170.138.52:3000";
    static String MASTODON_SOCIAL_SERVER_URI = "https://mastodon.social";

    static String MASTODON_SERVER_URI = MASTODON_NOVA_SERVER_URI;

    private static final String clientKey = "7sfncEuTWxzLCnwQ1QKSaDKCvW4TCm5r-WsRHXcPJrM";
    private static final String clientSecret = "OOwaxMqAN0KV-pgLvZSxhUR0Qdf7_RrxcWto7XSNaA4";
    private static final String accessTokenStr = "CalNnUfyLu_0QlKxAhjXpqRS1nL7iF-3hOqxDw5gHmE";


    static final String STATUSES_PATH = "/api/v1/statuses";
    static final String STATUSES_PATH_ID = "/api/v1/statuses/%s";
    static final String TIMELINES_PATH = "/api/v1/timelines/home";
    static final String ACCOUNT_FOLLOWING_PATH = "/api/v1/accounts/%s/following";
    static final String VERIFY_CREDENTIALS_PATH = "/api/v1/accounts/verify_credentials";
    static final String SEARCH_ACCOUNTS_PATH = "/api/v1/accounts/search";
    static final String ACCOUNT_FOLLOW_PATH = "/api/v1/accounts/%s/follow";
    static final String ACCOUNT_UNFOLLOW_PATH = "/api/v1/accounts/%s/unfollow";
    static final String ACCOUNT_GET_PATH = "/api/v1/accounts/%s";

    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 404;

    protected OAuth20Service service;
    protected OAuth2AccessToken accessToken;

    private JavaFeedsPushPreconditions preconditions;

    private static Mastodon impl;

    protected Mastodon() {
        try {
            service = new ServiceBuilder(clientKey).apiSecret(clientSecret).build(MastodonApi.instance());
            accessToken = new OAuth2AccessToken(accessTokenStr);
            preconditions = new JavaFeedsPushPreconditions();
        } catch (Exception x) {
            x.printStackTrace();
            System.exit(0);
        }
    }

    synchronized public static Mastodon getInstance() {
        if (impl == null)
            impl = new Mastodon();
        return impl;
    }

    private String getEndpoint(String path, Object... args) {
        var fmt = MASTODON_SERVER_URI + path;
        return String.format(fmt, args);
    }

    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {
        try {
            Result<Long> result = preconditions.postMessage(user, pwd, msg);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.POST, getEndpoint(STATUSES_PATH));

            JSON.toMap(new PostStatusArgs(msg.getText())).forEach((k, v) -> {
                request.addBodyParameter(k, v.toString());
            });

            service.signRequest(accessToken, request);

            Response response = service.execute(request);
            if (response.getCode() == HTTP_OK) {
                var res = JSON.decode(response.getBody(), PostStatusResult.class);
                return ok(res.getId());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) {
        try {
            Result<List<Message>> result = preconditions.getMessages(user, time);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.GET, getEndpoint(TIMELINES_PATH));

            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if (response.getCode() == HTTP_OK) {
                List<PostStatusResult> res = JSON.decode(response.getBody(), new TypeToken<List<PostStatusResult>>() {
                });

                List<Message> list = res.stream().map(PostStatusResult::toMessage).toList();
                List<Message> filteredList = new ArrayList<>();
                for (Message msg : list) {
                    if (msg.getCreationTime() > time) {
                        filteredList.add(msg);
                    }
                }
                return ok(filteredList);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }


    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {

        try {
            Result<Void> result = preconditions.removeFromPersonalFeed(user, mid, pwd);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.DELETE, getEndpoint(STATUSES_PATH_ID, mid));
            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if (response.getCode() == HTTP_OK) {
                return ok();
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);

    }

    @Override
    public Result<Message> getMessage(String user, long mid) {
        try {
            Result<Message> result = preconditions.getMessage(user, mid);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.GET, getEndpoint(STATUSES_PATH_ID, mid));

            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            System.out.println(response.getCode());
            if (response.getCode() == HTTP_OK) {
                var res = JSON.decode(response.getBody(), PostStatusResult.class);

                return ok(res.toMessage());
            } else if (response.getCode() == HTTP_NOT_FOUND) {
                return error(NOT_FOUND);
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) {

        try {
            Result<Void> result = preconditions.subUser(user, userSub, pwd);
            if (!result.isOK()) {
                return error(result.error());
            }
            List<MastodonAccount> accs = getUsersList(userSub);
            if (!accs.isEmpty()) {
                MastodonAccount userSubAcc = accs.get(0);
                String userSubId = String.valueOf(userSubAcc.getId());

                final OAuthRequest request = new OAuthRequest(Verb.POST, getEndpoint(ACCOUNT_FOLLOW_PATH, userSubId));

                service.signRequest(accessToken, request);

                Response response = service.execute(request);

                if (response.getCode() == HTTP_OK) {
                    return ok();
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
        try {
            Result<Void> result = preconditions.unsubscribeUser(user, userSub, pwd);
            if (!result.isOK()) {
                return error(result.error());
            }
            List<MastodonAccount> accs = getUsersList(userSub);
            if (!accs.isEmpty()) {
                MastodonAccount userSubAcc = accs.get(0);
                String userSubId = String.valueOf(userSubAcc.getId());
                final OAuthRequest request = new OAuthRequest(Verb.POST, getEndpoint(ACCOUNT_UNFOLLOW_PATH, userSubId));

                service.signRequest(accessToken, request);

                Response response = service.execute(request);
                System.out.println(response.getCode());
                if (response.getCode() == HTTP_OK) {

                    return ok();
                }
            }

        } catch (Exception x) {
            x.printStackTrace();
        }

        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<List<String>> listSubs(String user) {
        try {
            Result<List<String>> result = preconditions.listSubs(user);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.GET, getEndpoint(ACCOUNT_FOLLOWING_PATH, user));

            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if (response.getCode() == HTTP_OK) {
                List<String> res = JSON.decode(response.getBody(), new TypeToken<List<String>>() {
                });

                return ok(res);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<Void> deleteUserFeed(String user) {

        try {
            Result<Void> result = preconditions.deleteUserFeed(user);
            if (!result.isOK()) {
                return error(result.error());
            }
            final OAuthRequest request = new OAuthRequest(Verb.DELETE, getEndpoint(ACCOUNT_GET_PATH, user));
            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if (response.getCode() == HTTP_OK) {
                return ok();
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    private List<MastodonAccount> getUsersList(String user) throws IOException, ExecutionException, InterruptedException {

        final OAuthRequest searchRequest = new OAuthRequest(Verb.GET, getEndpoint(SEARCH_ACCOUNTS_PATH) + "?q=" + user);
        service.signRequest(accessToken, searchRequest);
        Response searchResponse = service.execute(searchRequest);

        List<MastodonAccount> accounts = new ArrayList<>();
        if (searchResponse.getCode() == HTTP_OK) {
            accounts = JSON.decode(searchResponse.getBody(), new TypeToken<List<MastodonAccount>>() {
            });
        }
        return accounts;
    }
}

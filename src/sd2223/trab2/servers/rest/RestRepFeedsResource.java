package sd2223.trab2.servers.rest;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.rest.FeedsService;
import sd2223.trab2.servers.kafka.sync.SyncPoint;

import java.util.List;

public class RestRepFeedsResource extends RestResource implements FeedsService {

    final SyncPoint versionManager;

    public RestRepFeedsResource(SyncPoint versionManager){
        this.versionManager = versionManager;

    }


    @Override
    public long postMessage(String user, String pwd, Message msg) {
        return 0;
    }

    @Override
    public void removeFromPersonalFeed(String user, long mid, String pwd) {

    }

    @Override
    public Message getMessage(String user, long mid) {
        return null;
    }

    @Override
    public List<Message> getMessages(String user, long time) {
        return null;
    }

    @Override
    public void subUser(String user, String userSub, String pwd) {

    }

    @Override
    public void unsubscribeUser(String user, String userSub, String pwd) {

    }

    @Override
    public List<String> listSubs(String user) {
        return null;
    }

    @Override
    public void deleteUserFeed(String user) {

    }
}

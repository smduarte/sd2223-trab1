package sd2223.trab2.servers.rest;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.servers.Domain;
import sd2223.trab2.servers.java.JavaFeedsCommon;
import sd2223.trab2.servers.kafka.KafkaPublisher;
import sd2223.trab2.servers.kafka.KafkaSubscriber;
import sd2223.trab2.servers.kafka.RecordProcessor;
import sd2223.trab2.servers.kafka.sync.SyncPoint;
import utils.JSON;

import static sd2223.trab2.api.java.Result.ErrorCode.*;
import static sd2223.trab2.api.java.Result.error;


import java.lang.runtime.SwitchBootstraps;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RepFeeds<T extends Feeds> implements Feeds, RecordProcessor {

    private static final long FEEDS_MID_PREFIX = 1_000_000_000;

    private KafkaPublisher publisher;
    private KafkaSubscriber subscriber;

    private SyncPoint sync;

    final String KAFKA_BROKERS = "kafka:9092";

    private Gson json;


    protected AtomicLong serial = new AtomicLong(Domain.uuid() * FEEDS_MID_PREFIX);

    final protected T preconditions;


    public RepFeeds(T preconditions, SyncPoint sync) {
        json = new Gson();
        this.sync = sync;
        this.preconditions = preconditions;
        publisher = KafkaPublisher.createPublisher(KAFKA_BROKERS);
        subscriber = KafkaSubscriber.createSubscriber(KAFKA_BROKERS, List.of("kafkadirectory"), "earliest");

        subscriber.start(false, (r) -> onReceive(r));
    }

    @Override
    public void onReceive(ConsumerRecord<String, String> r) {

        var key = r.key();
        switch (key) {
            case "post":
                receivePostMsg(r.value(), r.offset());
                break;
            case "":
                break;
        }
    }


    static protected record FeedInfo(String user, Set<Long> messages, Set<String> following, Set<String> followers) {
        public FeedInfo(String user) {
            this(user, new HashSet<>(), new HashSet<>(), ConcurrentHashMap.newKeySet());
        }
    }


    protected Map<Long, Message> messages = new ConcurrentHashMap<>();
    protected Map<String, FeedInfo> feeds = new ConcurrentHashMap<>();

    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {
        return null;
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
        return null;
    }

    @Override
    public Result<Message> getMessage(String user, long mid) {
        return null;
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) {
        return null;
    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) {
        return null;
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
        return null;
    }

    @Override
    public Result<List<String>> listSubs(String user) {
        return null;
    }

    @Override
    public Result<Void> deleteUserFeed(String user) {
        return null;
    }

    private void receivePostMsg(String value, long offset) {
        Message message = JSON.decode(value, Message.class);

        FeedInfo ufi = feeds.computeIfAbsent(message.getUser(), FeedInfo::new);
        synchronized (ufi.user()) {
            ufi.messages().add(message.getId());
            messages.putIfAbsent(message.getId(), message);
        }
        sync.setResult(offset, Result.ok(message.getId()));
    }

    private void receiveSubscribe(String value, long offset){

    }
    private void receiveUnsubscribe(String value, long offset){

    }
    private void receiveRemoveMessage(String value, long offset){

    }


}

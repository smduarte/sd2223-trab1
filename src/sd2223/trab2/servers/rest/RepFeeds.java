package sd2223.trab2.servers.rest;

import com.google.gson.Gson;
import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.servers.Domain;
import sd2223.trab2.servers.java.JavaFeedsCommon;
import sd2223.trab2.servers.kafka.KafkaPublisher;
import sd2223.trab2.servers.kafka.KafkaSubscriber;
import sd2223.trab2.servers.kafka.sync.SyncPoint;

import static sd2223.trab2.api.java.Result.ErrorCode.*;
import static sd2223.trab2.api.java.Result.error;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RepFeeds<T extends Feeds> implements Feeds {

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
    }


    static protected record FeedInfo(String user, Set<Long> messages, Set<String> following, Set<String> followees) {
        public FeedInfo(String user) {
            this(user, new HashSet<>(), new HashSet<>(), ConcurrentHashMap.newKeySet());
        }
    }


    protected Map<Long, Message> messages = new ConcurrentHashMap<>();
    protected Map<String, FeedInfo> feeds = new ConcurrentHashMap<>();

    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {

        var preconditionsResult = preconditions.postMessage(user, pwd, msg);
        if (!preconditionsResult.isOK())
            return preconditionsResult;

        Long mid = serial.incrementAndGet();
        msg.setId(mid);
        msg.setCreationTime(System.currentTimeMillis());

        FeedInfo ufi = feeds.computeIfAbsent(user, FeedInfo::new);
        synchronized (ufi.user()) {
            ufi.messages().add(mid);
            messages.putIfAbsent(mid, msg);
        }
        var offset = publisher.publish("topic1", "post", json.toJson(msg));
        if (offset < 0) {
            return error(INTERNAL_ERROR);
        }
        sync.waitForResult(offset);
        return Result.ok(mid);
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
}

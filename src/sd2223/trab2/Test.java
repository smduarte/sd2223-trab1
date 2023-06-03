package sd2223.trab2;

import sd2223.trab2.api.Message;
import sd2223.trab2.servers.mastodon.Mastodon;

public class Test {
    public static void main(String[] args) {
        var res0 = Mastodon.getInstance().postMessage("57372", "a7a31211e43f120f6c0a7706abdb45f1", new Message(1, "user1", "", "test 1 " + System.currentTimeMillis()));
        System.out.println(res0);
        /*var res3 = Mastodon.getInstance().postMessage("59943", "123456", new Message(2, "user2", "", "test 2 " + System.currentTimeMillis()));
        System.out.println(res3);
/*
        var res1 = Mastodon.getInstance().getMessages("user1", 0);
        System.out.println(res1);

        var res2 = Mastodon.getInstance().removeFromPersonalFeed("57372", 1, "a7a31211e43f120f6c0a7706abdb45f1");
        System.out.println(res2);

        var res1 = Mastodon.getInstance().getMessage("57372", 1);
        System.out.println(res1);

        var res4 = Mastodon.getInstance().subUser("57372", "59943", "a7a31211e43f120f6c0a7706abdb45f1");
        System.out.println(res4);*/

        var res2 = Mastodon.getInstance().listSubs("57372");
        System.out.println(res2);

    }
}

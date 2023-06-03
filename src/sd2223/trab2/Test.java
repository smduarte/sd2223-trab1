package sd2223.trab2;

import sd2223.trab2.api.Message;
import sd2223.trab2.servers.mastodon.Mastodon;

public class Test {
    public static void main(String[] args) {
        var res0 = Mastodon.getInstance().postMessage("57372", "a7a31211e43f120f6c0a7706abdb45f1", new Message(2, "57372", "", "test 2 " + System.currentTimeMillis()));
        System.out.println(res0);

       /* var res1 = Mastodon.getInstance().getMessages("user1", 0);
        System.out.println(res1);*/

        var res2 = Mastodon.getInstance().removeFromPersonalFeed("57372", 2, "a7a31211e43f120f6c0a7706abdb45f1");
        System.out.println(res2);
    }
}

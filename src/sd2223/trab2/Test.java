package sd2223.trab2;

import sd2223.trab2.api.Message;
import sd2223.trab2.servers.mastodon.Mastodon;

public class Test {
    public static void main(String[] args) {
        var res0 = Mastodon.getInstance().postMessage("user1", "123456", new Message(1, "user1", "", "test 1 " + System.currentTimeMillis()));
        System.out.println(res0);
/*
        var res1 = Mastodon.getInstance().getMessages("user1", 0);
        System.out.println(res1);*/

//        var res2 = Mastodon.getInstance().removeFromPersonalFeed("user1", 1, "123456");
//        System.out.println(res2);

        var res1 = Mastodon.getInstance().getMessage("user1", 1);
        System.out.println(res1);
    }
}

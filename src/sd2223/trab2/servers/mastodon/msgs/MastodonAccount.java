package sd2223.trab2.servers.mastodon.msgs;

public record MastodonAccount(String id, String username) {

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

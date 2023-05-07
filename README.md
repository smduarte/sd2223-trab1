# SD2223 TP1 Reference Solution

This project implements both message propagation models,
controlled by the `-push true|false` flag as an extra argument of the Feeds server in [feeds.props](https://github.com/smduarte/sd2223-trab1/blob/main/feeds.props)

+ Push Solution
  Messages are forwarded to remote domains, as needed, when posted.
  Requires keeping for each user a list of "followees".
  
+ Pull Solution
  The user feeds is materialized as needed, pulling messages from *followed* users on demand.
  A cache is used to keep remote feeds for a short period of time.

The specific parts of each solution are implemented in classes that include *Push* or *Pull* in the name.
For SOAP, the remote API is split across two packages with *push* and "pull" in their names. This should
allow to create a project version with either just the *Pull* or "Push" version, with minimal changes.

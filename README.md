# twitter-sentiment

detect sentiment for a given keyword using Akka Streams

This will initially retrieve tweets with the keyword selected in the `filtered-term` from the configs.

Currently the only kind of insights that this project detects is the number of tweets that contain words indicating of sentiment.
you can play around with those terms using the configs `negative-terms` or `positive-terms`.

make sure you generate consumer tokens as well as access tokens from your twitter account 
as explained here : https://developer.twitter.com/ja/docs/basics/authentication/guides/access-tokens.

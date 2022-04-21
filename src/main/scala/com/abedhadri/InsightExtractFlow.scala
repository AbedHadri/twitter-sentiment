package com.abedhadri

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import com.abedhadri.SentimentType.SentimentType

object SentimentDetectFlow {
  def apply(
    streamConfig: StreamConfig
  )(implicit sys: ActorSystem): Flow[TweetEntry, GroupedTweets, NotUsed] =
    new SentimentDetectFlow(streamConfig).flow
}

class SentimentDetectFlow private (config: StreamConfig)(
  implicit sys: ActorSystem
) {

  val flow: Flow[TweetEntry, GroupedTweets, NotUsed] =
    Flow[TweetEntry]
      .map(tweet => (detectSentimentType(tweet), tweet))
      .groupBy(config.maxTermCount, _._1, allowClosedSubstreamRecreation = true)
      .fold(GroupedTweets.generateDefault) {
        case (groupedTweets, (sentimentType, tweet)) =>
          GroupedTweets(sentimentType, groupedTweets.tweets :+ tweet)
      }
      .concatSubstreams

  def detectSentimentType(tweet: TweetEntry): SentimentType = {
    def textContainsAny(text: String, terms: List[String]) =
      terms.exists(text.contains)
    if (textContainsAny(tweet.text, config.positiveTerms)) {
      SentimentType.Positive
    } else if (textContainsAny(tweet.text, config.negativeTerms)) {
      SentimentType.Negative
    } else {
      SentimentType.Neutral
    }

  }
}

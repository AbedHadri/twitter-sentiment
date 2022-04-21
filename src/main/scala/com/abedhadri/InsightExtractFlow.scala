package com.abedhadri

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import com.abedhadri.SentimentType.SentimentType

object InsightExtractFlow {

  def apply(): Flow[GroupedTweets, SentimentInsight, NotUsed] =
    Flow[GroupedTweets].map(
      tweets => SentimentInsight(tweets.`type`, tweets.tweets.size)
    )

}

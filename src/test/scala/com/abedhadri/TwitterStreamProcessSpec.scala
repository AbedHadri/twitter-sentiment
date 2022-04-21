package com.abedhadri

import scala.concurrent.ExecutionContextExecutor
import scala.util.Success

import akka.actor.ActorSystem
import akka.stream.KillSwitches
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.abedhadri.TestFixtures._
import org.scalatest.concurrent._
import org.scalatest.matchers.should._
import org.scalatest.wordspec.AnyWordSpec

class TwitterStreamProcessSpec
    extends AnyWordSpec
    with ScalaFutures
    with Matchers {

  implicit val sys: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = sys.dispatcher

  "should successfully finish if stream is empty" in {
    Main
      .bootstrapFromSource(Source(List()), defaultSteamConfig)
      .viaMat(KillSwitches.single)(Keep.both)
      .runWith(Sink.seq)
      .onComplete {
        case Success(results) =>
          results shouldBe empty
      }
  }

  "should find all types of sentiments" in {
    Main
      .bootstrapFromSource(Source(defaultTweets), defaultSteamConfig)
      .viaMat(KillSwitches.single)(Keep.both)
      .runWith(Sink.seq)
      .onComplete {
        case Success(results) =>
          results.map { insight =>
            insight.`type` match {
              case SentimentType.Positive =>
                insight.tweetCount shouldBe 1
              case SentimentType.Negative =>
                insight.tweetCount shouldBe 1
              case SentimentType.Neutral =>
                insight.tweetCount shouldBe 1
            }
          }
      }
  }
}

package com.abedhadri

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationLong
import scala.util.{Failure, Success}

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.KillSwitches
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.abedhadri.ModelFormats._
import spray.json._

object Main extends App {

  val config = AppConfig.read.get
  implicit val sys: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = sys.dispatcher

  private val log = Logging(sys, this.getClass)

  val twitterClient = new TwitterClient(config.twitter)

  main()

  def main(): Unit = {
    bootstrapTwitterStream()
      .viaMat(KillSwitches.single)(Keep.both)
      .runWith(Sink.seq)
      .onComplete {
        case Success(results) =>
          results.foreach(tweets => log.info(tweets.toJson.toString()))
          log.info("Done")
          System.exit(0)
        case Failure(e) =>
          println("An error occurred " + e)
          System.exit(1)
      }
  }

  def bootstrapTwitterStream() = {
    Source
      .futureSource(
        twitterClient.getWithKeywordFilter(config.stream.filteredTerm)
      )
      .take(config.stream.maxTermCount)
      .completionTimeout(config.stream.runDuration.toSeconds.seconds)
      .groupBy(
        config.stream.maxTermCount,
        _.user.id,
        allowClosedSubstreamRecreation = true
      )
      .fold(UserTweets.generateDefault)(
        (acc, entry) =>
          acc.copy(user = entry.user, tweets = acc.tweets :+ entry)
      )
      .mergeSubstreams
  }

}

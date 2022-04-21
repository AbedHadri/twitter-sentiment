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
    bootstrapFromSource(
      Source
        .futureSource(
          twitterClient.getWithKeywordFilter(config.stream.filteredTerm)
        ),
      config.stream
    ).viaMat(KillSwitches.single)(Keep.both)
      .runWith(Sink.seq)
      .onComplete {
        case Success(results) =>
          results.foreach(tweets => log.info(tweets.toJson.toString()))
          log.info("Done")
          System.exit(0)
        case Failure(e) =>
          log.error("An error occurred ", e)
          System.exit(1)
      }
  }

  def bootstrapFromSource(source: Source[TweetEntry, Any],
                          config: StreamConfig) = {
    source
      .take(config.maxTermCount)
      .via(SentimentDetectFlow(config))
      .via(InsightExtractFlow())

  }

}

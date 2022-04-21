package com.abedhadri

import scala.concurrent.{ExecutionContextExecutor, TimeoutException}
import scala.util.{Failure, Success}

import akka.actor.ActorSystem
import akka.stream.KillSwitches
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.abedhadri.Main.{bootstrapTwitterStream, log, orderGroups}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.concurrent._
import org.scalatest.matchers.should._

class TwitterStreamProcessSpec
    extends AnyWordSpec
    with ScalaFutures
    with Matchers {

  implicit val sys: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = sys.dispatcher

  "" in {
    Source(List())
      .viaMat(KillSwitches.single)(Keep.both)
      .runWith(Sink.seq)
      .onComplete {
        case Success(results) =>
          results
        case Failure(e) =>
      }
  }
}

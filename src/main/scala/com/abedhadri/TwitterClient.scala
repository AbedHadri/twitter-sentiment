package com.abedhadri

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Accept, RawHeader}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{JsonFraming, Source}
import com.abedhadri.ModelFormats._
import com.hunorkovacs.koauth.domain.KoauthRequest
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService.createOauthenticatedRequest

class TwitterClient(config: TwitterConfig)(implicit ec: ExecutionContext,
                                           sys: ActorSystem,
                                           mat: Materializer) {

  def getWithKeywordFilter(keyword: String): Future[Source[TweetEntry, _]] = {
    val signedRequest = authenticateRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = Uri(config.streamEndpoint)
          .withQuery(Query("track" -> keyword)),
        headers = Seq(Accept(MediaRange(MediaTypes.`application/json`)))
      )
    )

    streamResponse(signedRequest)
  }

  private def streamResponse(
    request: HttpRequest
  ): Future[Source[TweetEntry, _]] = {
    Http()
      .singleRequest(request)
      .map(
        _.entity.dataBytes
          .via(JsonFraming.objectScanner(Int.MaxValue))
          .mapAsync(1)(
            byteString =>
              Unmarshal(byteString.utf8String)
                .to[TweetEntry]
          )
      )
  }

  private def authenticateRequest(request: HttpRequest): HttpRequest = {
    val requestWithInfo = createOauthenticatedRequest(
      KoauthRequest(request.method.value, request.uri.toString(), None, None),
      config.consumer.key,
      config.consumer.secret,
      config.access.token,
      config.access.secret
    )
    request.withHeaders(RawHeader("Authorization", requestWithInfo.header))
  }
}

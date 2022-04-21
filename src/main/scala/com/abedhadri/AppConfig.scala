package com.abedhadri

import scala.annotation.nowarn
import scala.util.Try

import akka.http.scaladsl.model.Uri
import pureconfig._
import pureconfig.error.ConfigReaderException
import pureconfig.generic.auto._

case class AppConfig(stream: StreamConfig, twitter: TwitterConfig)

case class StreamConfig(maxTermCount: Int,
                        filteredTerm: String,
                        positiveTerms: List[String],
                        negativeTerms: List[String],
)

case class TwitterConfig(streamEndpoint: String,
                         access: AccessCredentials,
                         consumer: ConsumerCredentials)

case class AccessCredentials(token: String, secret: String)
case class ConsumerCredentials(key: String, secret: String)

object AppConfig {
  implicit val uriReader: ConfigReader[Uri] =
    ConfigReader.fromStringTry(value => Try(Uri(value)))

  @nowarn("cat=lint-byname-implicit")
  def read: Try[AppConfig] =
    ConfigSource.default
      .load[AppConfig]
      .left
      .map(ConfigReaderException(_))
      .toTry
}

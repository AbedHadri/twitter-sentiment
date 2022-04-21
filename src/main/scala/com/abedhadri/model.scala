package com.abedhadri

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

import com.abedhadri.SentimentType.SentimentType
import spray.json._

case class User(id: Long, name: String, createdAt: Instant)

case class TweetEntry(id: Long, text: String, user: User, createdAt: Instant)

object SentimentType extends Enumeration {
  type SentimentType = Value

  val Positive: SentimentType = Value(0, "positive")
  val Negative: SentimentType = Value(1, "negative")
  val Neutral: SentimentType = Value(2, "neutral")
}

case class GroupedTweets(`type`: SentimentType, tweets: List[TweetEntry])

case class SentimentInsight(`type`: SentimentType, tweetCount: Long)

object GroupedTweets {
  def generateDefault: GroupedTweets =
    GroupedTweets(SentimentType.Neutral, List.empty)
}

object ModelFormats extends DefaultJsonProtocol {

  implicit val sentimentTypeFormat = new JsonFormat[SentimentType] {

    override def write(obj: SentimentType): JsString = JsString(obj.toString)

    override def read(value: JsValue): SentimentType = ???
  }

  implicit val instantFormat: JsonFormat[Instant] = new JsonFormat[Instant] {
    val parser =
      new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)

    override def write(obj: Instant): JsString = JsString(obj.toString)

    override def read(value: JsValue): Instant = value match {
      case JsString(value) =>
        parser.parse(value).toInstant
      case o =>
        deserializationError(
          s"Expected Instant as long representing epoch millis, but got $o"
        )
    }
  }

  implicit val UserFormat: RootJsonFormat[User] =
    jsonFormat(User.apply, "id", "name", "created_at")
  implicit val tweetEntryFormat: RootJsonFormat[TweetEntry] =
    jsonFormat(TweetEntry.apply, "id", "text", "user", "created_at")
  implicit val groupedTweetsFormat: RootJsonFormat[GroupedTweets] =
    jsonFormat2(GroupedTweets.apply)
  implicit val sentimentInsightFormat: RootJsonFormat[SentimentInsight] =
    jsonFormat2(SentimentInsight.apply)

}

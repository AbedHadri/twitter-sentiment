package com.abedhadri

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

import spray.json._

case class User(id: Long, name: String, createdAt: Instant)

case class TweetEntry(id: Long, text: String, user: User, createdAt: Instant)

case class UserTweets(user: User, tweets: List[TweetEntry])

object UserTweets {

  def generateDefault: UserTweets =
    UserTweets(User(0L, "", Instant.now()), List.empty)

}

object ModelFormats extends DefaultJsonProtocol {

  implicit val instantFormat: JsonFormat[Instant] = new JsonFormat[Instant] {
    val parser =
      new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)

    override def write(obj: Instant): JsString = JsString(obj.toString)

    override def read(value: JsValue): Instant = value match {
      case JsString(value) =>
        val zz = parser.parse(value).toInstant
        zz
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
  implicit val userTweetsFormat: RootJsonFormat[UserTweets] =
    jsonFormat2(UserTweets.apply)

}

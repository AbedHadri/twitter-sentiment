package com.abedhadri

import java.time.Instant

object TestFixtures {
  val defaultPositiveTerm = "good"
  val defaultNegativeTerm = "bad"

  val defaultSteamConfig = StreamConfig(
    maxTermCount = 100,
    filteredTerm = "",
    positiveTerms = List(defaultPositiveTerm),
    negativeTerms = List(defaultNegativeTerm),
  )

  val defaultUser = User(id = 1, name = "name", createdAt = Instant.now())

  val defaultTweets = List(
    TweetEntry(
      id = 1L,
      text = s"$defaultNegativeTerm boy",
      user = defaultUser,
      createdAt = Instant.now()
    ),
    TweetEntry(
      id = 1L,
      text = s"in the middle $defaultPositiveTerm boy",
      user = defaultUser,
      createdAt = Instant.now()
    ),
    TweetEntry(
      id = 1L,
      text = "have nothing to do",
      user = defaultUser,
      createdAt = Instant.now()
    )
  )

}

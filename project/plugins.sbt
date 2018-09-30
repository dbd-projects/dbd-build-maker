logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.19")
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "4.0.1")

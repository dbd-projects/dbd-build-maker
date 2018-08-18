logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "3.0.0")
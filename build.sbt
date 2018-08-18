name := "dbd-build-maker"

version := "1.0"

lazy val `dbd-build-maker` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs)
libraryDependencies += "org.mockito" % "mockito-core" % "2.21.0"

unmanagedResourceDirectories in Test += (baseDirectory.value / "target/web/public/test")

(sourceDirectories in Test) := Seq(new File("test/controllers"), new File("test/models"))

jacocoExcludes in Test := Seq(
  "controllers.javascript.ReverseApplication",
  "controllers.javascript.ReverseAssets",
  "controllers.ReverseApplication",
  "controllers.ReverseAssets",
  "controllers.routes",
  "controllers.routes.javascript",
  "jooq.*",
  "Module",
  "router.Routes*",
  "*.routes*"
)

jacocoIncludes in Test := Seq(
  "controllers.*",
  "models.*"
)

jacocoReportSettings := JacocoReportSettings()
  .withThresholds(
    JacocoThresholds(
      instruction = 0,
      method = 0,
      branch = 0,
      complexity = 0,
      line = 90,
      clazz = 100)
  )
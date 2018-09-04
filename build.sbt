lazy val akkaHttpVersion = "10.1.1"
lazy val akkaVersion = "2.5.13"
lazy val circeVersion = "0.9.3"

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "com.idarlington",
        scalaVersion := "2.12.6"
      )),
    name := "complex",
    libraryDependencies ++= commonDependencies
  )
  .aggregate(
    domain,
    clusterSharding,
    distributedData
  )

lazy val domain = project.settings(
  name := "domain",
  libraryDependencies ++= commonDependencies
)

lazy val clusterSharding = project
  .settings(
    name := "clusterSharding",
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.akkaClusterSharding
    )
  )
  .dependsOn(domain)

lazy val distributedData = project
  .settings(
    name := "distributedData",
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.akkaDistributedData
    )
  )
  .dependsOn(domain)

lazy val commonDependencies = Seq(
  dependencies.akkaHttp,
  dependencies.akkaStreams,
  dependencies.akkaCluster,
  dependencies.circeCore,
  dependencies.circeGeneric,
  dependencies.circeParser,
  dependencies.swaggerAkkaHttp
)

lazy val dependencies =
  new {
    val akkaHttpVersion = "10.1.1"
    val akkaVersion = "2.5.13"
    val circeVersion = "0.9.3"
    val swaggerVersion = "1.0.0"

    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
    val akkaHttpXML = "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion
    val akkaStreams = "com.typesafe.akka" %% "akka-stream" % akkaVersion
    val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % akkaVersion
    val akkaClusterSharding = "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion
    val akkaDistributedData = "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion
    val circeCore = "io.circe" %% "circe-core" % circeVersion
    val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
    val circeParser = "io.circe" %% "circe-parser" % circeVersion
    val swaggerAkkaHttp = "com.github.swagger-akka-http" %% "swagger-akka-http" % swaggerVersion
    val akkHTTPTestKit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
    val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
    val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
  }

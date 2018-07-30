package com.idarlington

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.cluster.Cluster
import akka.cluster.sharding.ClusterSharding
import akka.stream.scaladsl.Source

object ComplexApp extends App with Routes {

  implicit val system: ActorSystem = ActorSystem("ComplexApp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val numberOfShards = 10
  /*private val extractEntityId = {
    case
  }*/



  val storageRegion: ActorRef = ClusterSharding(system).start(
    typeName = "Storage",
    entityProps = Props[StorageActor],
    settings = ???,
    extractEntityId = ???,
    extractShardId = ???
  )

  val storageActor: ActorRef =
    system.actorOf(StorageActor.props, "storageActor")
  lazy val routes: Route = storageRoutes

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s" Server started at http://localhost:8080")
  Await.result(system.whenTerminated, Duration.Inf)
}


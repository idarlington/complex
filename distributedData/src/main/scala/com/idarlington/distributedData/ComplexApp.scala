package com.idarlington.distributedData

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.idarlington.distributedData.cluster.ReplicatedStorage
import com.idarlington.distributedData.http.Routes

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ComplexApp extends App with Routes {

  implicit val system: ActorSystem = ActorSystem("ComplexApp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  lazy val routes: Route = storageRoutes
  val replicator = system.actorOf(ReplicatedStorage.props())

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s" Server started at http://localhost:8080")
  Await.result(system.whenTerminated, Duration.Inf)
}

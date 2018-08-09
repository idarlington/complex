package com.idarlington

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.idarlington.cluster.ReplicatedStorage
import com.idarlington.http.Routes

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ComplexApp extends App with Routes {

  implicit val system: ActorSystem = ActorSystem("ComplexApp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val replicator = system.actorOf(ReplicatedStorage.props())

  lazy val routes: Route = storageRoutes

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s" Server started at http://localhost:8080")
  Await.result(system.whenTerminated, Duration.Inf)
}

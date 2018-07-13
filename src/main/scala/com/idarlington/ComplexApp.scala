package com.idarlington

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ComplexApp extends App with Routes {

  implicit val system: ActorSystem = ActorSystem("complexApp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val storageActor: ActorRef =
    system.actorOf(StorageActor.props, "storageActor")
  lazy val routes: Route = storageRoutes

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s" Server started at http://localhost:8080")
  Await.result(system.whenTerminated, Duration.Inf)
}

package com.idarlington.clusterSharding

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.idarlington.clusterSharding.cluster.ClusterShardingRegion
import com.idarlington.clusterSharding.http.Routes
import com.idarlington.clusterSharding.http.swagger.SwaggerDocService
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ComplexApp extends App with Routes {

  implicit val system: ActorSystem             = ActorSystem("ComplexApp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  lazy val routes: Route                       = storageRoutes ~ SwaggerDocService.routes
  val storageRegion                            = new ClusterShardingRegion(system).clusterShardRegion

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s" Server started at http://localhost:8080")
  Await.result(system.whenTerminated, Duration.Inf)
}

package com.idarlington

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

trait Routes {

  implicit def system: ActorSystem

  def storageActor: ActorRef

  lazy val storageRoutes: Route =
    pathPrefix("store") {
      concat(pathEnd {
        concat(
          get {
            complete(StatusCodes.OK)
          },
          post {
            complete(StatusCodes.OK)
          })
      })
    }
}

package com.idarlington

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import CirceSupport._
import akka.Done
import akka.event.Logging
import akka.util.Timeout

import scala.concurrent.duration._
import io.circe.generic.auto._

import scala.collection.mutable.Map

import scala.concurrent.Future

trait Routes {

  implicit def system: ActorSystem
  implicit lazy val timeout = Timeout(5.seconds)

  lazy val log = Logging(system, classOf[StorageActor])

  def storageRegion: ActorRef

  lazy val storageRoutes: Route =
    pathPrefix("store") {
      concat(
        pathEnd {
          concat(
            get {
              log.info("Getting all storage content")
              val content: Future[Map[String, String]] =
                (storageRegion ? Model.Get("")).mapTo[Map[String, String]]
              complete(content)
            },
            post {
              entity(as[Entity]) { content =>
                (storageRegion ? Model.Set(content.key, content.value))
                  .mapTo[Done]
                complete(StatusCodes.Created)
              }
            })
        },
        path(Segment) { key =>
          concat(get {
            val value =
              (storageRegion ? Model.Get(key)).mapTo[Option[String]]
            rejectEmptyResponse {
              complete(value)
            }
          })
        })
    }
}

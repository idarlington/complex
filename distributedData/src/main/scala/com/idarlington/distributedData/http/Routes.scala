package com.idarlington.distributedData.http

import akka.Done
import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.idarlington.distributedData.cluster.ReplicatedStorage
import com.idarlington.CirceSupport._
import com.idarlington.{ Entity, Model }
import scala.collection.mutable.Map
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern.ask

import io.circe.generic.auto._

trait Routes {

  implicit def system: ActorSystem

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val log = Logging(system, classOf[ReplicatedStorage])
  lazy val storageRoutes: Route =
    pathPrefix("store") {
      concat(
        pathEnd {
          concat(
            get {
              log.info("Getting all storage content")
              val content: Future[Map[String, String]] =
                (replicator ? Model.Get("")).mapTo[Map[String, String]]
              complete(content)
            },
            post {
              entity(as[Entity]) { content =>
                (replicator ? Model.Set(content.key, content.value))
                  .mapTo[Done]
                complete(StatusCodes.Created)
              }
            })
        },
        path(Segment) { key =>
          concat(get {
            val value =
              (replicator ? Model.Get(key)).mapTo[Option[String]]
            rejectEmptyResponse {
              complete(value)
            }
          })
        })
    }

  def replicator: ActorRef
}

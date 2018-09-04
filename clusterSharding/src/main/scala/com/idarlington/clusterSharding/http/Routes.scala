package com.idarlington.clusterSharding.http

import akka.Done
import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.sharding.ShardRegion
import akka.pattern.ask
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.idarlington.{ Entity, Model }
import com.idarlington.CirceSupport._
import com.idarlington.clusterSharding.cluster.StorageActor
import io.circe.generic.auto._

import scala.concurrent.Await
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

trait Routes {

  implicit def system: ActorSystem

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val log = Logging(system, classOf[StorageActor])
  lazy val storageRoutes: Route =
    pathPrefix("store") {
      concat(
        pathEnd {
          concat(post {
            entity(as[Entity]) { content =>
              (storageRegion ? Model.Set(content.key, content.value))
                .mapTo[Done]
              complete(StatusCodes.Created)
            }
          })
        },
        path(Segment) { key =>
          concat(get {
            val shardingState =
              (storageRegion ? ShardRegion.GetShardRegionState)
                .mapTo[ShardRegion.CurrentShardRegionState]
                .map(println)
            Await.result(shardingState, 5.seconds)

            val value =
              (storageRegion ? Model.Get(key)).mapTo[Option[String]]
            rejectEmptyResponse {
              complete(value)
            }
          })
        })
    }

  def storageRegion: ActorRef
}

package com.idarlington.cluster

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.sharding.{ ClusterSharding, ClusterShardingSettings }

class ClusterShardingRegion(actorSystem: ActorSystem) {
  val clusterShardRegion: ActorRef = ClusterSharding(actorSystem).start(
    typeName = "Storage",
    entityProps = StorageActor.props,
    settings = ClusterShardingSettings(actorSystem),
    extractEntityId = StorageActor.extractEntityId,
    extractShardId = StorageActor.extractShardId)
}

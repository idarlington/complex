package com.idarlington.cluster

import akka.Done
import akka.actor.{ Actor, ActorLogging, Props }
import akka.cluster.sharding.ShardRegion
import com.idarlington.Model._

import scala.collection.mutable.Map

object StorageActor {

  private val numberOfShards = 5
  private val numberOfPartitions = 5

  def props: Props = Props[StorageActor]

  def entity(key: String): Int = {
    math.abs(key.hashCode) % numberOfPartitions
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case Messages(key) =>
      (entity(key) % numberOfShards).toString
  }

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case msg @ Messages(key) =>
      (entity(key).toString, msg)
  }
}

class StorageActor extends Actor with ActorLogging {

  var storage: Map[String, String] = Map.empty
  override def receive: Receive = {
    case Set(key, value) =>
      storage += (key -> value)
      sender() ! Done
    case Get(key) =>
      val value = storage.get(key)
      sender() ! value
    case GetContent =>
      sender() ! storage
  }
}

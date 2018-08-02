package com.idarlington

import akka.Done
import akka.actor.{ Actor, ActorLogging, Props }
import KeyIdentifier._
import akka.cluster.sharding.ShardRegion

import scala.collection.mutable.Map
import scala.util.Random

object StorageActor {

  trait Messages {
    def key: String
  }

  object Messages {
    def unapply(m: Messages) = Some(m.key)
  }

  case class Set(key: String, value: String) extends Messages
  case class Get(key: String) extends Messages
  case class Update(key: String, value: String)
    extends Messages
  case class Delete(key: String) extends Messages
  case class GetContent()

  private val numberOfShards = 5

  def props: Props = Props[StorageActor]

  def entity(key: String): Int = {
    stringUnicodeDecimal(key).map(identifier).getOrElse(Random.nextInt(5))
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
  import StorageActor._

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

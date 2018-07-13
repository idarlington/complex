package com.idarlington

import akka.Done
import akka.actor.{ Actor, ActorLogging, Props }

import scala.collection.mutable.Map

object StorageActor {

  case class GetContent()

  case class Set(key: String, value: String)
  case class Get(key: String)
  case class Update(key: String, value: String)
  case class Delete(key: String)

  def props: Props = Props[StorageActor]
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
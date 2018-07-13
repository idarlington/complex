package com.idarlington

import akka.actor.{ Actor, ActorLogging, Props }

object StorageActor {
  case class Set(key: String, value: String)
  case class Get(key: String)
  case class Update(key: String, value: String)
  case class Delete(key: String)

  def props: Props = Props[StorageActor]
}

class StorageActor extends Actor with ActorLogging {
  var storage: Map[String, String] = ???
  override def receive: Receive = ???
}
package com.idarlington.cluster

import akka.actor.{ Actor, ActorRef, Props }
import akka.cluster.Cluster
import akka.cluster.ddata.Replicator._
import akka.cluster.ddata.{ DistributedData, LWWMap, LWWMapKey }
import com.idarlington.Model

object ReplicatedStorage {
  private final case class Request(key: String, replyTo: ActorRef)

  def props(): Props = Props(new ReplicatedStorage)
}

class ReplicatedStorage extends Actor {
  import ReplicatedStorage._

  val replicator: ActorRef = DistributedData(context.system).replicator
  implicit val cluster: Cluster = Cluster(context.system)

  def dataKey(entryKey: String): LWWMapKey[String, String] = {
    LWWMapKey(entryKey)
  }

  override def receive: Receive = {
    case Model.Set(key, value) =>
      replicator ! Update(dataKey(key), LWWMap(), WriteLocal)(_ + (key -> value))
    case Model.Delete(key) =>
      replicator ! Update(dataKey(key), LWWMap(), WriteLocal)(_ - key)
    case Model.Get(key) =>
      replicator ! Get(dataKey(key), ReadLocal, Some(Request(key, sender())))
    case g @ GetSuccess(LWWMapKey(_), Some(Request(key, replyTo))) =>
      g.dataValue match {
        case data: LWWMap[_, _] => data.asInstanceOf[LWWMap[String, String]].get(key) match {
          case Some(value) => replyTo ! Some(value)
          case None => replyTo ! None
        }
      }
  }

}

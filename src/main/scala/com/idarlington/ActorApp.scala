package com.idarlington

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.Cluster
import akka.cluster.ddata.DistributedData
import com.typesafe.config.ConfigFactory

object ActorApp {

  def main(args: Array[String]): Unit = {
    startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      val config = ConfigFactory
        .parseString("akka.remote.netty.tcp.port=" + port)
        .withFallback(ConfigFactory.load())

      val system = ActorSystem("ComplexApp", config)
      DistributedData(system).replicator
      implicit val cluster: Cluster = Cluster(system)
      /*new ClusterShardingRegion(system).clusterShardRegion*/
    }
  }

}

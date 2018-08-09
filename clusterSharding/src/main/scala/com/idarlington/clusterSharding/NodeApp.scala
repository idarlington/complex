package com.idarlington.clusterSharding

import akka.actor.ActorSystem
import com.idarlington.clusterSharding.cluster.ClusterShardingRegion
import com.typesafe.config.ConfigFactory

object NodeApp {

  def main(args: Array[String]): Unit = {
    startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      val config = ConfigFactory
        .parseString("akka.remote.netty.tcp.port=" + port)
        .withFallback(ConfigFactory.load())

      val system = ActorSystem("ComplexApp", config)
      new ClusterShardingRegion(system).clusterShardRegion
    }
  }

}

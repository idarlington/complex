package com.idarlington

import akka.actor.ActorSystem
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
      new ClusterShardingRegion(system).clusterShardRegion
    }
  }

}

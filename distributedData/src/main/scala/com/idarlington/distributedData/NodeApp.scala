package com.idarlington.distributedData

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.ddata.DistributedData
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
      DistributedData(system).replicator
      implicit val cluster: Cluster = Cluster(system)
    }
  }

}

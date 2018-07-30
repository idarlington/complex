import java.util.concurrent.TimeUnit

import akka.Done
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.{Await, Future}
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import akka.actor.ActorSystem

import scala.concurrent.duration._

implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()

val settings = ActorMaterializerSettings(system)

val source = Source.fromFuture(Future.successful(10))
val sink = Sink.foreach((a:Int) => println(a))

val done = source.runWith(sink)

val a: Done = Await.result(done, 10.seconds)

Await.result(Future[Int](10), 10.seconds)

import scala.concurrent.Await
import scala.concurrent.duration.Duration

Await.result(f1, Duration.create(10, TimeUnit.SECONDS))

akka.stream.javadsl.Source.fromFuture()
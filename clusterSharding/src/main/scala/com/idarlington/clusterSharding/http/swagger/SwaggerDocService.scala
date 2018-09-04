package com.idarlington.clusterSharding.http.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import com.idarlington.clusterSharding.http.Routes

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[Routes])
  override val host = "localhost:8080"
  override val info = Info(version = "1.0")
}

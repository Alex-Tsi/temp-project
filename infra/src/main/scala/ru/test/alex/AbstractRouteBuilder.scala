package ru.test.alex

import org.apache.camel.builder.RouteBuilder

abstract class AbstractRouteBuilder(x: => Unit) extends RouteBuilder {

  override def configure(): Unit = {
    x
  }

}

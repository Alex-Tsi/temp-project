package ru.test

import org.apache.camel.Exchange

import scala.collection.JavaConverters.{mapAsJavaMapConverter, mapAsScalaMapConverter}

package object alex {

  type E = Exchange


  implicit class ExchangeHelper(ex: E) {


    def setNewBody[T](body: T): Exchange = setNewBodySaveHeaders(body)

    def setNewBodySaveHeaders[T](body: T, saveHeaders: String*): Exchange = {
      val lowerCaseHeaders = saveHeaders.map(h => h.toLowerCase).toList
      ex.getMessage.setBody(body)
      ex.getMessage.setHeaders(ex.getMessage.getHeaders.asScala.filterKeys(k => lowerCaseHeaders.contains(k.toLowerCase)).asJava)
      ex
    }
  }
}

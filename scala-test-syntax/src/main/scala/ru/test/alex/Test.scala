package ru.test.alex

import org.springframework.core.io.{ClassPathResource, FileSystemResource}

import scala.io.Source

class Test {}


object Test extends App {

  val list1 = List(1,2,3)

  list1 :: List(4)
  println(list1)

}

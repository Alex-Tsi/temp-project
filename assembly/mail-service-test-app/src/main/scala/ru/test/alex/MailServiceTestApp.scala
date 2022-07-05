package ru.test.alex

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class MailServiceTestApp

object MailServiceTestApp extends App {
  val context: ConfigurableApplicationContext = SpringApplication.run(classOf[MailServiceTestApp])
}

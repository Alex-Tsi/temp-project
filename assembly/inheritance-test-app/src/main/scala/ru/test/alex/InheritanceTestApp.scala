package ru.test.alex

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, PostMapping, RequestBody, RequestMapping, RestController}

import scala.beans.BeanProperty

@SpringBootApplication
class InheritanceTestApp {}

object InheritanceTestApp extends App {
  SpringApplication.run(classOf[InheritanceTestApp])
}


@RestController
@CrossOrigin(origins = Array("http://localhost:4200"))
//@RequestMapping("/inherit")
class UserController {


  @PostMapping(Array("test-ng"))
  def responseEntity(@RequestBody obj: Obj): ResponseEntity[Array[String]] = {
    val s: Array[String] = Array.empty
    print("yee");
    print(obj)
    new ResponseEntity[Array[String]](Array("hi"), HttpStatus.OK)
  }

}


class Obj {

  @BeanProperty
  var test: Int = _

}
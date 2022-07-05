package ru.test.alex

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("temP")
class Temp extends TempPar {

}


abstract class TempPar {

  private var otherBean: OtherBean = _

  @Autowired
  final def setOtherBean(otherBean: OtherBean): Unit = this.otherBean = otherBean
}

@Component
class OtherBean {
  val x = 10

}


//class DataForOtherBean(x: Int = 10, s = "test")

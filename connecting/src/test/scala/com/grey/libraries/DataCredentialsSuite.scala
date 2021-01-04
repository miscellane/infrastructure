package com.grey.libraries

import org.scalatest.funsuite.AnyFunSuite

class DataCredentialsSuite extends AnyFunSuite {

  val dataCredentials = new DataCredentials()
  val keysAndValues: Map[String, String] = dataCredentials.dataCredentials()

  test ("The .conf parameters ...") {

    val basicKeys = Array("accessKey", "secretKey")
    val keysAndValuesArray = keysAndValues.keys.toArray

    basicKeys.par.foreach{key =>
      assert(keysAndValuesArray.intersect(Array(key)).nonEmpty)
    }

  }

  test ("Each included parameter has a value ...") {

    keysAndValues.par.foreach{case (key: String, value: String) =>
        assert(value.nonEmpty)
    }

  }

}

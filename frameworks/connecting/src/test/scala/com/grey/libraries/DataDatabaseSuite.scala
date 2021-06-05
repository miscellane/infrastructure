package com.grey.libraries

import org.scalatest.funsuite.AnyFunSuite

class DataDatabaseSuite extends AnyFunSuite {

  test("The .conf parameters ...") {

    val basicKeys = Array("db", "url", "driver")

    val dataDatabase = new DataDatabase()
    val keysAndValues = dataDatabase.dataDatabase(databaseName = "sqlserver.database-name")
    val keysAndValuesArray = keysAndValues.keys.toArray

    basicKeys.par.foreach{key =>
      assert( keysAndValuesArray.intersect(Array(key)).nonEmpty )
    }

  }



}

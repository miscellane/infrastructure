package com.grey.libraries.redshift

import scala.util.Try

class CreateTable {


  def createTable(databaseString: String, tableVariables: Map[String, String]): Try[Int] = {


    // Query execution
    val executeOnly = new ExecuteOnly()


    // Execute
    val F: Try[Int] = executeOnly.executeOnly(tableVariables("stringCreateTable"), databaseString)


    // States
    if (F.isFailure) {
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }


}

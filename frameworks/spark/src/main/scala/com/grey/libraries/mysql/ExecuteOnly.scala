package com.grey.libraries.mysql

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception


/**
  * https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
  */
class ExecuteOnly {

  // Connectivity Instance
  val connectivityServices = new Connecting()

  def executeOnly(queryString: String, databaseString: String, setAutoCommit: Boolean = false): Try[Boolean] = {


    // Database
    val databaseValues: Map[String, String] = connectivityServices.databaseKeys(Array(databaseString))


    // Driver
    Class.forName(databaseValues("driver"))


    // Properties
    val properties = new Properties()
    properties.setProperty("user", databaseValues("user"))
    properties.setProperty("password", databaseValues("password"))


    // Connection Object
    val connection = DriverManager.getConnection(databaseValues("url"), properties)
    connection.setAutoCommit(setAutoCommit)


    // Statement Object
    val statement = connection.createStatement()


    // Execute
    val F: Try[Boolean] = Exception.allCatch.withTry(
      statement.execute(queryString)
    )
    connection.close()


    // States
    if (F.isFailure) {
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }

}

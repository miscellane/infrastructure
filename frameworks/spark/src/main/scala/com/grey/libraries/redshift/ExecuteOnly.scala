package com.grey.libraries.redshift

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception

class ExecuteOnly {


  // Connectivity Instance
  val connectivityServices = new Connecting()


  // Function
  def executeOnly(queryString: String, databaseString: String, setAutoCommit: Boolean = false): Try[Boolean] = {


    // Database
    val databaseValues: Map[String, String] = connectivityServices.databaseKeys(scala.Array(databaseString))


    // Driver
    Class.forName("com.amazon.redshift.jdbc.Driver")


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

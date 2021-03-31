package com.grey.libraries.postgresql

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception


/**
  * Execute
  * https://jdbc.postgresql.org/documentation/head/connect.html
  *
  */
class ExecuteOnly {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  /**
    *
    * @param queryString: The query
    * @param databaseString: In this case postgresql.{databaseName} string
    * @param setAutoCommit: https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setAutoCommit-boolean-
    * @return
    */
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

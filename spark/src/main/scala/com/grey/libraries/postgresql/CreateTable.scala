package com.grey.libraries.postgresql

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception


/**
  * https://www.postgresql.org/docs/10/sql-createtable.html
  */
class CreateTable {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  /**
    *
    * @param databaseString: In this case postgresql.{databaseName} string
    * @param tableVariables: The table related variables
    * @return
    */
  def createTable(databaseString: String, tableVariables: Map[String, String]): Try[Boolean] = {


    val queryString = tableVariables("stringCreateTable")


    // Database
    val databaseValues: Map[String, String] = connectivityServices.databaseKeys(scala.Array(databaseString))


    // Driver
    Class.forName(databaseValues("driver"))


    // Properties
    val properties = new Properties()
    properties.setProperty("user", databaseValues("user"))
    properties.setProperty("password", databaseValues("password"))


    // Connection Object
    val connection = DriverManager.getConnection(databaseValues("url"), properties)
    connection.setAutoCommit(true)


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

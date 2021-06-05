package com.grey.libraries.mysql

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception


/**
  * https://dev.mysql.com/doc/refman/8.0/en/load-data.html
  */
class LoadData {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  /**
    *
    * @param databaseString: In this case mysql.{databaseName} string
    * @param tableVariables: Table related variables
    * @param dropOnFailure: A boolean that dictates whether or not the table in question is dropped on execution failure
    * @return
    */
  def loadData (databaseString: String, tableVariables: Map[String, String], dropOnFailure: Boolean = false): Try[Boolean] = {


    val queryString = tableVariables("uploadString")


    // Database
    val databaseValues = connectivityServices.databaseKeys(Array(databaseString))


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


    // Function
    if (F.isFailure) {
      if (dropOnFailure) {
        new com.grey.libraries.mysql.ExecuteOnly()
          .executeOnly(s"drop table if exists ${tableVariables("tableName")}", databaseString)
      }
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }

}

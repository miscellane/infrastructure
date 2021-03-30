package com.grey.libraries.postgresql

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception


/**
  * https://www.postgresql.org/docs/10/sql-copy.html
  * https://www.postgresql.org/docs/10/populate.html
  */
class CopyData {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  /**
    *
    * @param databaseString: In this case postgresql.{databaseName} string
    * @param tableVariables: The table related variables
    * @param dropOnFailure: A boolean that dictates whether or not the table in question is dropped on execution failure
    * @return
    */
  def copyData(databaseString: String, tableVariables: Map[String, String], dropOnFailure: Boolean = false): Try[Boolean] = {


    // Ref. https://www.postgresql.org/docs/10/sql-copy.html
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
    // https://www.postgresql.org/docs/10/sql-droptable.html
    if (F.isFailure) {
      if (dropOnFailure) {
        new com.grey.libraries.mysql.ExecuteOnly()
          .executeOnly(s"DROP TABLE IF EXISTS ${tableVariables("tableName")} RESTRICT", databaseString)
      }
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }

}

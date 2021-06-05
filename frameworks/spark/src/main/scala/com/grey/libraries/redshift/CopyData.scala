package com.grey.libraries.redshift

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception

class CopyData {


  // Connectivity Instance
  val connectivityServices = new Connecting()


  def copyData(databaseString: String, bucketString: String, tableVariables: Map[String, String],
               dropOnFailure: Boolean = false): Try[Boolean] = {


    // Database
    val databaseValues = connectivityServices.databaseKeys(Array(databaseString))


    // Credentials
    val credentialsValues = connectivityServices.credentialsKeys()


    // Driver
    Class.forName("com.amazon.redshift.jdbc.Driver")


    // Properties
    val properties = new Properties()
    properties.setProperty("user", databaseValues("user"))
    properties.setProperty("password", databaseValues("password"))


    // Connection Object
    val connection = DriverManager.getConnection(databaseValues("url"), properties)
    connection.setAutoCommit(true)


    // Statement Object
    val statement = connection.createStatement()


    // Statement String
    val queryString =
      s"""
         |COPY ${tableVariables("tableName")}
         |FROM '$bucketString'
         |credentials '${credentialsValues("stringCredentials")}' ${tableVariables("uploadParameters")}
     """.stripMargin


    // Execute
    val F: Try[Boolean] = Exception.allCatch.withTry(
      statement.execute(queryString)
    )
    connection.close()


    // Function
    if (F.isFailure) {
      if (dropOnFailure) {
        new com.grey.libraries.redshift.ExecuteOnly()
          .executeOnly(s"drop table if exists ${tableVariables("tableName")}", databaseString)
      }
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }


}

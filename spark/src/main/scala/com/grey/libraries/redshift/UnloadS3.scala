package com.grey.libraries.redshift

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception

class UnloadS3 {


  // Connectivity Instance
  val connectivityServices = new Connecting()


  def unloadS3(queryText: String, databaseString: String, bucketString: String): Try[Boolean] = {

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
    val queryString =
      s"""
         |UNLOAD('$queryText')
         |TO '$bucketString'
         |credentials '${credentialsValues("stringCredentials")}'
     """.stripMargin


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

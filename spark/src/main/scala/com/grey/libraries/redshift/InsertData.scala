package com.grey.libraries.redshift

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception

class InsertData {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  // Function
  def insertData(intoTableString: String, fromTableString: String, databaseString: String,
                 excludingIdentity: String = ""): Try[Boolean] = {


    // Database
    val databaseValues = connectivityServices.databaseKeys(Array(databaseString))


    // Credentials
    // val credentialsValues = connectivityServices.credentialsKeys()


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


    // The SQL query string for updating the target table
    val (fieldsString: String, fieldsStringTuple: String) = if (excludingIdentity.isEmpty) {
      ("*", "")
    } else {
      (excludingIdentity, "(" + excludingIdentity + ")")
    }

    val queryString =
      s"""
         |insert into $intoTableString $fieldsStringTuple
         |(
         |  select distinct $fieldsString from $fromTableString except (select $fieldsString from $intoTableString)
         |)
       """.stripMargin


    // Execute
    val F: Try[Boolean] = Exception.allCatch.withTry(
      statement.execute(queryString)
    )


    // Drop the temporary table
    if (F.isSuccess || F.isFailure) {
      new ExecuteOnly().executeOnly(s"drop table if exists $fromTableString", databaseString, setAutoCommit = true)
    }


    // States
    if (F.isFailure) {
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }


}

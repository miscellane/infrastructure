package com.grey.libraries.redshift

import java.sql.DriverManager
import java.util.Properties

import com.grey.libraries.Connecting

import scala.util.Try
import scala.util.control.Exception

class InsertDataBySlice {


  // Connectivity Instance
  val connectivityServices = new Connecting()


  // Function
  def insertDataBySlice(intoTableString: String, fromTableString: String, databaseString: String,
                        fieldsOfSlice: List[String], fieldsOutwithSlice: List[String],
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


    // In the case of automatically generated identification fields
    // a table's set of fields - excluding the auto-generated field - must
    // be specified in the insert statement
    val fieldsStringTuple: String = if (excludingIdentity.isEmpty) {
      ""
    } else {
      "(" + excludingIdentity + ")"
    }


    // Sometimes a table must have unique records w.r.t. one or more fields, but not all fields.
    // The fields in question should be assigned to fieldsOfSlice.
    val fieldsOfSliceString = fieldsOfSlice.mkString(", ")


    // And, the fields outwith the slice group should be assigned to fieldsOutwithSlice
    val fieldsOutwithSliceString = fieldsOutwithSlice.mkString(", ")


    // Consequently, the insert statement requires the following.  Study the statement.
    val fieldsOfBaseString = fieldsOfSlice.map( x => "base." + x ).mkString(", ")
    val onClauseString = fieldsOfSlice.map( x => "base." + x  + " = " + fromTableString + "." + x ).mkString(" and ")


    // The insert statement
    val queryString =
      s"""
         |insert into $intoTableString $fieldsStringTuple
         |(
         |  with base as (
         |    select distinct $fieldsOfSliceString from $fromTableString except( select $fieldsOfSliceString from $intoTableString )
         |  )
         |  select distinct $fieldsOfBaseString, $fieldsOutwithSliceString
         |    from base left join $fromTableString on ($onClauseString)
         |)
       """.stripMargin
    println(queryString)

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

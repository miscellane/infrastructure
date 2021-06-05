package com.grey.libraries.redshift

import com.grey.libraries.Connecting

import scala.util.Try

class CopyData {


  // Query execution
  private val executeOnly = new ExecuteOnly()


  def copyData(databaseString: String, bucketString: String, tableVariables: Map[String, String],
               dropOnFailure: Boolean = false): Try[Int] = {


    // Credentials
    val connectingScala = new Connecting()
    val credentialKeys = connectingScala.credentialsKeys()
    val stringCredentials = credentialKeys("stringCredentials")


    // Query
    val queryString = s"""
                         |copy ${tableVariables("tableName")} from '$bucketString'
                         |credentials '$stringCredentials'
                         |${tableVariables("uploadParameters")}
   """.stripMargin


    // Execute
    val F: Try[Int] = executeOnly.executeOnly(queryString, databaseString)


    // States
    if (F.isFailure) {
      if (dropOnFailure) {
        executeOnly.executeOnly( s"drop table if exists ${tableVariables("tableName")}", databaseString)
      }
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }

  }


}

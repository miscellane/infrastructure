package com.grey.libraries.redshift

import com.grey.libraries.Connecting

import scala.util.Try

class UnloadS3 {


  // Query execution
  private val executeOnly = new ExecuteOnly()


  def unloadS3(queryText: String, databaseString: String,
                 bucketString: String): Try[Int] = {


    // Credentials
    val connectingScala = new Connecting()
    val stringCredentials = connectingScala.credentialsKeys()


    // Query
    // CASE STANDARD: ADDQUOTES ESCAPE [NOT ALLOWED FOR CASE FORMAT AS CSV]
    val queryString =
      s"""
         |unload ('$queryText') to '$bucketString'
         |credentials '${stringCredentials("stringCredentials")}'
         |HEADER
         |ADDQUOTES
         |ESCAPE
       """.stripMargin


    // Execute
    val F: Try[Int] = executeOnly.executeOnly(queryString, databaseString)


    if (F.isFailure) {
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }

}

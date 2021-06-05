package com.grey.libraries.redshift


import scala.util.Try


class InsertData {
  

  // Query execution
  private val executeOnly = new ExecuteOnly()


  def insertData(intoTableString: String, fromTableString: String, databaseString: String, excludingIdentity: String = ""): Try[Int] = {


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
    val F: Try[Int] = executeOnly.executeOnly(queryString, databaseString)


    // Drop the temporary table
    if (F.isSuccess || F.isFailure) {
      executeOnly.executeOnly(s"drop table if exists $fromTableString", databaseString)
    }


    // States
    if (F.isFailure) {
      sys.error("Error: " + F.failed.get.getMessage)
    } else {
      F
    }


  }


}

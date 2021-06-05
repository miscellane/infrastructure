package com.grey.libraries.redshift

import scala.util.Try

class StageData {


  // Function
  def stageData(databaseString: String, bucketString: String,
                tableVariables: Map[String, String]): Try[Boolean] = {


    // Create the temporary table that would stage the data.
    // On failure, CreateTable will terminate the calling JAR
    val createTable = new CreateTable()
    val F: Try[Boolean] = createTable.createTable(databaseString, tableVariables)


    // Populate the temporary table
    // If the copy step fails, CopyData will terminate the calling JAR; it will drop the
    // temporary table beforehand
    val copyData = new CopyData()
    if (F.isSuccess) {
      copyData.copyData(databaseString, bucketString, tableVariables, dropOnFailure = true)
    } else {
      sys.error("Error: " + F.failed.get.getMessage)
    }


  }


}

package com.grey.libraries.mssql

import com.grey.libraries.Connecting
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.control.Exception

class UnloadData(spark: SparkSession) {


  // Connectivity Instance
  val connectivityServices = new Connecting()


  def unloadData(queryString: String, databaseName: String, numberOfPartitions: Int = 1): DataFrame = {


    // Database
    val databaseValues = connectivityServices.databaseKeys(Array(databaseName))


    // JDBC URL
    val jdbcUrl = databaseValues("url") + ";characterEncoding=UTF-8;user=" +
      databaseValues("user") + ";password=" + databaseValues("password")


    // Setting additional database connection properties
    val connectionProperties = new java.util.Properties()
    connectionProperties.setProperty("Driver", databaseValues("driver"))


    // The data
    val F = Exception.allCatch.withTry(
      spark.read.option("numPartitions", numberOfPartitions).jdbc(jdbcUrl, s"($queryString) dataset", connectionProperties)
    )


    // States
    if (F.isFailure) {
      sys.error( "Error: " + F.failed.get.getMessage)
    } else {
      F.get
    }


  }


}

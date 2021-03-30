package com.grey.libraries.postgresql

import com.grey.libraries.Connecting
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.control.Exception

/**
  *
  * @param spark: An instance of SparkSession
  */
class UnloadData(spark: SparkSession) {

  // Connectivity Instance
  val connectivityServices = new Connecting()


  /**
    *
    * @param queryString: The query
    * @param databaseString: In this case postgresql.{databaseName} string
    * @param numberOfPartitions:  The number parallel processing options
    * @return
    */
  def unloadData(queryString: String, databaseString: String, numberOfPartitions: Int = 1): DataFrame = {


    // Database
    val databaseValues: Map[String, String] = connectivityServices.databaseKeys(Array(databaseString))


    // Driver
    Class.forName(databaseValues("driver"))


    // JDBC URL
    val jdbcUrl = databaseValues("url") + ";user=" +
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

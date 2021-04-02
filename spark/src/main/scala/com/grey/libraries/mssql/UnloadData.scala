package com.grey.libraries.mssql

import java.util.Properties

import com.grey.libraries.Connecting
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.Try
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
    * @param databaseString: In this case mssql.{databaseName} string
    * @param numberOfPartitions:  The number parallel processing options
    * @return
    */
  def unloadData(queryString: String, databaseString: String, numberOfPartitions: Int = 1): DataFrame = {


    // Database
    val databaseValues = connectivityServices.databaseKeys(Array(databaseString))


    // Driver
    Class.forName(databaseValues("driver"))


    // Properties
    val properties = new Properties()
    properties.setProperty("user", databaseValues("user"))
    properties.setProperty("password", databaseValues("password"))
    properties.setProperty("Driver", databaseValues("driver"))


    // The data
    val F: Try[DataFrame] = Exception.allCatch.withTry(
      spark.read.option("numPartitions", numberOfPartitions).jdbc(databaseValues("url"),
        s"($queryString) dataset", properties)
    )


    // States
    if (F.isFailure) {
      sys.error( "Error: " + F.failed.get.getMessage)
    } else {
      F.get
    }


  }


}

package com.grey.libraries

import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.Try
import scala.util.control.Exception

class HiveLayer(spark: SparkSession, hiveBaseProperties: HiveBaseCaseClass#HBCC) {

  def hiveLayer(hiveLayerProperties: HiveLayerCaseClass#HLCC): Try[DataFrame] = {

    val addPartitionQuery =
      s"""
         |ALTER TABLE ${hiveBaseProperties.databaseName}.${hiveBaseProperties.tableName}
         |ADD PARTITION (${hiveLayerProperties.partitionSpecification})
         |LOCATION '${hiveLayerProperties.partitionOfInterest}'
       """.stripMargin

    val T: Try[DataFrame] = Exception.allCatch.withTry(
      spark.sql(addPartitionQuery)
    )

    T

  }

}

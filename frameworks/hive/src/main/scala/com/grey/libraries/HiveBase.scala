package com.grey.libraries

import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.Try
import scala.util.control.Exception

class HiveBase(spark: SparkSession) {


  def hiveBase(hiveBaseProperties: HiveBaseCaseClass#HBCC, stringOfFields: String): Try[DataFrame] = {


    // Database statement
    val createDatabase =
      s"""
         |CREATE DATABASE IF NOT EXISTS ${hiveBaseProperties.databaseName}
         |COMMENT '${hiveBaseProperties.commentDatabase}'
         |LOCATION '${hiveBaseProperties.databaseLocation}'
         |WITH DBPROPERTIES (${hiveBaseProperties.databaseProperties})
       """.stripMargin


    // Table statement
    val createTable =
      s"""
         |CREATE EXTERNAL TABLE IF NOT EXISTS ${hiveBaseProperties.databaseName}.${hiveBaseProperties.tableName}
         |(
         |  $stringOfFields
         |)
         |COMMENT '${hiveBaseProperties.commentTable}'
         |PARTITIONED BY ( ${hiveBaseProperties.partitionString} )
         |${hiveBaseProperties.bucketClause}
         |${hiveBaseProperties.rowFormat}
         |STORED AS ${hiveBaseProperties.storedAs}
         |LOCATION '${hiveBaseProperties.tableLocation}'
         |${hiveBaseProperties.tblProperties}
        """.stripMargin


    // Execute
    val base: List[Try[DataFrame]] = List(createDatabase, createTable).map{statement =>

      val execution: Try[DataFrame] = Exception.allCatch.withTry(
        spark.sql(statement)
      )

      if (execution.isFailure) {
        sys.error(execution.failed.get.getMessage)
      } else {
        execution
      }

    }


    // Hence
    base.last


  }

}

package com.grey.libraries

import org.apache.spark.sql.SparkSession

class ClearUp(spark: SparkSession, hiveBaseProperties: HiveBaseCaseClass#HBCC) {


  def clearUp(hiveLayerProperties: HiveLayerCaseClass#HLCC): Unit = {

    // Get the array of tables in a database.  Of course, this
    // will be empty at the beginning of time ...
    val arrayOfTables: Array[String] =  spark.sqlContext.tableNames(hiveBaseProperties.databaseName)
    val tableOfInterest: Array[String] = arrayOfTables.filter(_ == hiveBaseProperties.tableName)

    // Conditional partition drop.  If the table in question exists, drop the
    // partition in question.
    val dropPartitionQuery =
    s"""
       |ALTER TABLE ${hiveBaseProperties.databaseName}.${hiveBaseProperties.tableName}
       |  DROP IF EXISTS PARTITION (${hiveLayerProperties.partitionSpecification})
       """.stripMargin

    // Hence
    if (tableOfInterest.length > 0) {
      spark.sql(dropPartitionQuery)
    }


  }


}

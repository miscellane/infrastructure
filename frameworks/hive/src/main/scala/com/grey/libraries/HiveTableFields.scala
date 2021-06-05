package com.grey.libraries

import org.apache.spark.sql.types.{StructField, StructType}

import scala.util.Try
import scala.util.control.Exception

class HiveTableFields {

  // Pending: '.toDDL' switch.  From Spark 2.4.+ onward the function StructField.toDDL
  // converts a field's properties to the required string format, i.e., nameOfField hiveDataType COMMENT 'comment'
  def hiveTableFields(fieldsStructType: StructType): String = {

    // The instance for converting Spark DataTypes to their Hive counterparts.
    val hiveDataType = new HiveDataType()

    // Building a Hive table's strings of fields
    val F: Try[Seq[String]] = Exception.allCatch.withTry(
      fieldsStructType.map { fieldProperties: StructField =>
        fieldProperties.name + " " +
          hiveDataType.hiveDataType(fieldProperties.dataType) +
          " COMMENT '" + fieldProperties.getComment.orNull + "'"
      }
    )
    if (F.isSuccess) {
      F.get.mkString(",")
    } else {
      sys.error("Error: " + F.failed.get.getMessage)
    }

  }

}

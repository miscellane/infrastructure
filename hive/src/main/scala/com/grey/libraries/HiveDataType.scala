package com.grey.libraries

import org.apache.spark.sql.types._

// From Spark 2.4.+ onward StructField.toDDL addresses type mapping
class HiveDataType {

  def hiveDataType(dataTypeOfVariable: DataType): String = {

    dataTypeOfVariable match {

      case _:ByteType => "tinyint"
      case _:ShortType => "shortint"
      case _:IntegerType => "integer"
      case _:LongType => "bigint"
      case _:FloatType => "float"
      case _:DoubleType => "double"
      case _:DecimalType => "decimal"
      case _:StringType => "string"
      case _:BinaryType => "binary"
      case _:BooleanType => "boolean"
      case _:TimestampType => "timestamp" // Beware
      case _:DateType => "date"
      case _:ArrayType => "array"
      case _:StructType => "struct"
      case _:MapType => "map"
      case _ => "string"

    }

  }

}

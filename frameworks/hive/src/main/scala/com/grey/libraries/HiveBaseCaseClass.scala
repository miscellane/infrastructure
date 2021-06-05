package com.grey.libraries

class HiveBaseCaseClass {

  case class HBCC (
                    databaseName: String,
                    commentDatabase: String,
                    databaseProperties: String,
                    databaseLocation: String,
                    tableName: String,
                    commentTable: String,
                    tableLocation: String,
                    rowFormat: String,
                    storedAs: String,
                    tblProperties: String,
                    partitionString: String,
                    bucketClause: String
                  )

}

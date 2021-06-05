package com.grey.libraries.redshift

import com.grey.libraries.interface.{ScalikeAutoCommit, ScalikeAutoSession}

import scala.util.Try
import scala.util.control.Exception

class ExecuteOnly {

  // Connectivity Instances
  val SAC = new ScalikeAutoCommit()
  val SAS = new ScalikeAutoSession()


  // Function
  def executeOnly(queryString: String, databaseString: String): Try[Int] = {

    Exception.allCatch.withTry(
      SAC.scalikeAutoCommit(
        SAS.scalikeAutoSession(databaseString), queryString
      )
    )

  }

}

package com.grey.libraries.interface

import scalikejdbc.{AutoSession, DB, SQL}

class ScalikeAutoCommit {

  // Executing SQL commands in outer-space
  def scalikeAutoCommit(session: AutoSession, executionString: String): Int = {

    // False: successful execution, nothing returned
    val isAlive: Boolean = DB autoCommit{
      implicit session =>
        SQL(executionString).execute().apply()
    }

    if (isAlive.compareTo(false) == 0) {0} else {1}

  }

}

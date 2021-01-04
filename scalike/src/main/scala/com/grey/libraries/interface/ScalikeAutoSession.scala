package com.grey.libraries.interface

import com.grey.libraries.Connecting
import scalikejdbc.{AutoSession, ConnectionPool}

class ScalikeAutoSession() {

  def scalikeAutoSession(databaseName: String): AutoSession = {

    // The database keys, properties, of the database
    val keysValues = new Connecting()
    val keysMap: Map[String, String] = keysValues.databaseKeys(Array(databaseName))


    // The properties are used to build the session properties of a database
    Class.forName(keysMap("driver"))
    ConnectionPool.singleton(keysMap("url"), keysMap("user"), keysMap("password"))


    // The session
    implicit val session: AutoSession = AutoSession
    session

  }

}

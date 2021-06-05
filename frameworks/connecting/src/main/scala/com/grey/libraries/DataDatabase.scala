package com.grey.libraries

import java.io.File
import java.nio.file.Paths

import com.grey.libraries.environment.LocalSettings
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try
import scala.util.control.Exception

class DataDatabase {

  private val localSettings = new LocalSettings()

  def dataDatabase(databaseName: String): Map[String, String] = {

    // Path
    val path: String = Paths.get(localSettings.resourcesDirectory, "databases.conf").toString

    // Read the configuration file
    val config: Try[Config] = Exception.allCatch.withTry(
      ConfigFactory.parseFile(new File(path)).getConfig("databases")
    )

    // Read the parameters of the database of interest
    val database: Try[Config] = if (config.isSuccess) {
      Exception.allCatch.withTry(
        config.get.getConfig(databaseName)
      )
    } else {
      sys.error(config.failed.get.getMessage)
    }

    // Structuring
    val keyValuePairs: Map[String, String] = if (database.isSuccess) {

      // The set of parameters
      val set = database.get.entrySet()

      // The iterator form of the parameters
      val setIterator = set.iterator()

      // Hence -> mappings
      var mappings = Map[String, String]()
      while (setIterator.hasNext) {
        val pair = setIterator.next()
        val key: String = pair.getKey
        val value: String = pair.getValue.render()
          .substring(1, pair.getValue.render().lastIndexOf("\""))

        mappings += (key -> value)
      }
      mappings

    } else {

      sys.error(database.failed.get.getMessage)

    }

    // Hence
    keyValuePairs

  }

  case class Database(db: String, user: String, password: String, url: String, driver: String)

}

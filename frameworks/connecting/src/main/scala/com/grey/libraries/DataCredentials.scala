package com.grey.libraries

import java.io.File
import java.nio.file.Paths

import com.grey.libraries.environment.LocalSettings
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try
import scala.util.control.Exception

class DataCredentials {

  private val localSettings = new LocalSettings()

  def dataCredentials(): Map[String, String] = {

    // Path
    val path: String = Paths.get(localSettings.resourcesDirectory, "credentials.conf").toString

    // Read the configuration file
    val config: Try[Config] = Exception.allCatch.withTry(
      ConfigFactory.parseFile(new File(path)).getConfig("credentials")
    )

    // Read the parameters of the credentials of interest
    val credentials: Try[Config] = if (config.isSuccess) {
      Exception.allCatch.withTry(
        config.get.getConfig("aws").getConfig("datascience")
      )
    } else {
      sys.error(config.failed.get.getMessage)
    }

    // Hence
    val keyValuePairs = if (credentials.isSuccess) {

      val accessKey = credentials.get.getString("accessKey")
      val secretKey = credentials.get.getString("secretKey")
      val stringCredentials = s"aws_access_key_id=$accessKey;aws_secret_access_key=$secretKey"

      Map("accessKey" -> accessKey, "secretKey" -> secretKey, "stringCredentials" -> stringCredentials)

    } else {
      sys.error(credentials.failed.get.getMessage)
    }

    // Hence
    keyValuePairs

  }

  case class Credentials(user: String, password: String, accesskey: String, secretkey: String)

  // Original
  /*
  val T: Seq[Credentials] = Seq( Credentials("", "", "", "") )

  val keysValues: Credentials = T.head
  val accessKey = keysValues.accesskey
  val secretKey = keysValues.secretkey
  val stringCredentials = s"aws_access_key_id=$accessKey;aws_secret_access_key=$secretKey"

  Map("accessKey" -> accessKey, "secretKey" -> secretKey, "stringCredentials" -> stringCredentials)
  */

}

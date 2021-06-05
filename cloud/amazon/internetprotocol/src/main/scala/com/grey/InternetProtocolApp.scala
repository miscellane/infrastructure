package com.grey

import java.io.{File, FileInputStream}
import net.liftweb.json.{DefaultFormats, JValue, JsonParser}

import java.nio.file.{Path, Paths}
import scala.util.Try
import scala.util.control.Exception
import scala.sys.process._

/**
 *
 */
object InternetProtocolApp {

  case class ParameterValuesOf(clusterFunctionTagValue: String, clusterVPCID: String, allocationID: String)

  /**
   *
   * @param args: None
   */
  def main(args: Array[String]): Unit = {
    
    // System
    val localDirectory = System.getProperty("user.dir")
    val localSeparator = System.getProperty("file.separator")

    // Base names
    val names = List("parametersValues.json")

    // Resources directory
    val directory: String = Paths.get(localDirectory, names: _* ).toString
    
    // Get the parameter values from a parameter file named
    // parametersValues.json, which must exist within the same
    // directory that this program's JAR is placed.
    val parametersInputStream: Try[FileInputStream] = Exception.allCatch.withTry(
      new FileInputStream( new File(directory + localSeparator) )
    )
    
    // Extract the JSON string
    val jsonString: String = if (parametersInputStream.isSuccess){
      scala.io.Source.fromInputStream(parametersInputStream.get).mkString
    } else {
      sys.error("Error: " + parametersInputStream.failed.get.getMessage)
    }
    
    // The values
    implicit val formats: DefaultFormats.type = DefaultFormats
    val liftJValue: JValue = JsonParser.parse(jsonString)
    val T: ParameterValuesOf = liftJValue.extract[ParameterValuesOf]
    
    // In-cluster CLI query
    val clusterFunctionTag: String = if (T.clusterFunctionTagValue.isEmpty) {
      ""
    } else {
      s"Name=tag:ClusterFunction,Values=${T.clusterFunctionTagValue} "
    }

    val instanceIDQueryString: String = "aws ec2 describe-instances --instance-id --filters " +
      s"Name=vpc-id,Values=${T.clusterVPCID} " +
      "Name=instance-state-code,Values=16 " + clusterFunctionTag +
      "Name=tag:aws:elasticmapreduce:instance-group-role,Values=MASTER " +
      "--query Reservations[*].Instances[*].[InstanceId] --output text"
    println(instanceIDQueryString)

    // Execute Query
    // val stringOf: String = instanceIDQueryString.!!

    // The ID of the master instance
   //  val instanceID = stringOf.trim

    // Assign Elastic IP to cluster
    /*
    val associateIPQueryString = s"""sudo aws ec2 associate-address --instance-id $instanceID --allocation-id ${T.allocationID}"""
    associateIPQueryString.!
    */

  }

}

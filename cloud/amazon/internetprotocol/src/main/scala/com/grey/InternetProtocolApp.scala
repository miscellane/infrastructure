package com.grey

import java.io.{File, FileInputStream}

import net.liftweb.json.{DefaultFormats, JValue, JsonParser}

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


    // Get the parameter values from a parameter file named
    // parameterValues.json, which must exist within the same
    // directory that this program's JAR is placed.
    val parametersInputStream: Try[FileInputStream] = Exception.allCatch.withTry(
      new FileInputStream( new File(localDirectory + localSeparator + "parametersValues.json") )
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
    /*
    s"""aws ec2 describe-instances --instance-id --filters Name=vpc-id,Values=${T.clusterVPCID} """ +
      s"""Name=instance-state-code,Values=16 Name=tag:ClusterFunction,Values=${T.clusterFunctionTagValue} """ +
      s""" Name=tag:aws:elasticmapreduce:instance-group-role,Values=MASTER --query Reservations[*].Instances[*].[InstanceId] --output text"""
    */

    val instanceIDQueryString = s"""aws ec2 describe-instances --instance-id --filters Name=vpc-id,Values=${T.clusterVPCID} """ +
      s"""Name=instance-state-code,Values=16 """ +
      s""" Name=tag:aws:elasticmapreduce:instance-group-role,Values=MASTER --query Reservations[*].Instances[*].[InstanceId] --output text"""
    println(instanceIDQueryString)


    // Execute Query
    val stringOf: String = instanceIDQueryString.!!


    // The ID of the master instance
    val instanceID = stringOf.trim


    // Assign Elastic IP to cluster
    val associateIPQueryString = s"""sudo aws ec2 associate-address --instance-id $instanceID --allocation-id ${T.allocationID}"""
    associateIPQueryString.!


  }


}


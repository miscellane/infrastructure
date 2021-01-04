package com.grey.libraries.awscli

import scala.util.Try
import scala.util.control.Exception

class S3API {


  def S3API(bucketString: String, prefixString: String, functionString: String, patternString: String): Try[String] = {


    // Query
    val queryString =  s"aws s3api list-objects --bucket $bucketString " +
      s"--prefix $prefixString --query Contents[?$functionString(Key,`$patternString`)].[Key] --output text "


    // Execute Query
    val T: Try[String] = Exception.allCatch.withTry(
      scala.sys.process.stringToProcess(queryString).!!
    )


    // States
    if (T.isFailure) {
      sys.error( s"The S3API query for $patternString failed" )
    } else {
      T
    }


  }


}

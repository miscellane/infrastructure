package com.grey.libraries.awscli

import scala.collection.parallel.mutable.ParArray

class S3DIR(operatingSystemWindows: Boolean) {


  // The list of directories within a specified S3 directory
  def getListOfDirectories(bucketString: String): List[String] = {


    // The S3 command string
    val commandString = s"""aws s3 ls $bucketString"""


    // Run the command
    val stringOfHeadings: String = if (operatingSystemWindows) {
      scala.sys.process.stringToProcess("cmd /C " + commandString).!!
    } else {
      scala.sys.process.stringToProcess(commandString).!!
    }


    // Deal with the string of headings, which excludes bucket & prefix names
    val splitsOfHeading = stringOfHeadings.split("/").map(_.trim)

    val listOfNames: ParArray[String] =  splitsOfHeading.par.filter(_.startsWith("PRE"))

    listOfNames.map(x => x.replace("PRE ", "")).map(_.trim).toList

  }


}

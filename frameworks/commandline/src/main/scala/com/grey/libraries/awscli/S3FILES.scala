package com.grey.libraries.awscli

class S3FILES(operatingSystemWindows: Boolean) {

  def getListOfFiles(bucketString: String): List[String] = {

    val commandString = s"""aws s3 ls $bucketString --recursive"""

    val L = if (operatingSystemWindows) {
      scala.sys.process.stringToProcess("cmd /C " + commandString).!!
    } else {
      scala.sys.process.stringToProcess(commandString).!!
    }

    val listOfFiles: List[String] = L.split("\n").toList.par.map(x => x.split(" ").reverse.head).toList

    listOfFiles

  }

}

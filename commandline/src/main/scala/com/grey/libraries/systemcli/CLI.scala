package com.grey.libraries.systemcli

import scala.util.Try
import scala.util.control.Exception

class CLI(operatingSystemWindows: Boolean) {


  // Unzip
  def UNZIP(dataFilesDirectory: String): Try[Int] = {


    val F = Exception.allCatch.withTry(
      if (operatingSystemWindows) {

        // In a Windows environment, unzip then delete the zip files.
        // If each of the two steps is successful, then the sum of the exit codes will be zero
        List(scala.sys.process.stringToProcess("cmd /C " + s"7z e $dataFilesDirectory*.gz -o$dataFilesDirectory").!,
          scala.sys.process.stringToProcess("cmd /C " + s"del $dataFilesDirectory*.gz").!).map(x => x).sum

      } else {

        // In a Linux environment, the gunzip command unzips the files in place, and the unzipped files are deleted.
        // If the step succeeds the exit code will be zero
        scala.sys.process.stringToProcess(s"sudo gunzip $dataFilesDirectory*.gz").!

      }
    )

    if (F.isFailure) {
      sys.error( "Error: " + F.failed.get.getMessage )
    } else {
      F
    }


  }


}

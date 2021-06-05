package com.grey.libraries.environment

class LocalSettings {

  // The operating system
  val operatingSystem: String = System.getProperty("os.name").toUpperCase
  val operatingSystemWindows: Boolean = operatingSystem.startsWith("WINDOWS")


  // Local characteristics
  val localDirectory: String = System.getProperty("user.dir")
  val localSeparator: String = System.getProperty("file.separator")
  val localWarehouse: String = s"$localDirectory${localSeparator}warehouse$localSeparator"


  // Local data directories
  val resourcesDirectory: String = s"$localDirectory${localSeparator}src" +
    s"${localSeparator}main${localSeparator}resources$localSeparator"

}

package com.grey.libraries

class Connecting {

  // The database keys
  def databaseKeys(args: Array[String]): Map[String, String] = {

    // Validation Step: Number of arguments
    if (args.length < 1) {
      System.err.println("Required: Array('the name of the database of interest')")
      System.exit(1)
    }

    // Using a different approach
    val dataDatabase = new DataDatabase()
    dataDatabase.dataDatabase(args(0))

  }


  // The credentials keys
  def credentialsKeys(): Map[String, String] = {

    // Using a different approach
    val dataCredentials = new DataCredentials()
    dataCredentials.dataCredentials()

  }

}

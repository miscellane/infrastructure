<br>

### spark

**Project Purpose**: For interacting with databases via [spark](http://spark.apache.org/docs/2.4.7/sql-data-sources-jdbc.html).

<br>

#### Building spark

Foremost

```bash
    mvn clean package
```

Subsequently

```bash
    cd spark/target
```

and noting that the name of the ``.jar`` partly depends on the project tags

```xml
    <project>
      <groupId>com.grey.libraries</groupId>
      <artifactId>spark</artifactId>
      <version>1.0</version>
    </project>
```
 
in `pom.xml`, run

```bash
     mvn deploy:deploy-file 
         -Dfile=spark-1.0.jar 
         -DgroupId=com.grey.libraries   # ref. <groupId>com.grey.libraries</groupId> of pom.xml
         -DartifactId=spark             # ref. <artifactId>spark</artifactId> of pom.xml
         -Dversion=1.0                  # ref. <version>1.0</version> of pom.xml
         -Dpackaging=jar 
         -Durl=file:///[disk partition letter]:/[path to .m2]/.m2/repository -DrepositoryId=repository
```

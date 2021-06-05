<br>

### Apache Hive

**Project Purpose**: For systematically projecting structures over data files via [Apache Hive](https://hive.apache.org).  After 
structure projection, Hive provides the wherewithal to read, write, and query data files via 
SQL ([Structured Query Language](https://www.iso.org/standard/63555.html)).

<br>

#### Building Hive

Foremost

```bash
    mvn clean package
```

Subsequently

```bash
    cd hive/target
```

and noting that the name of the ``.jar`` partly depends on the project tags

```xml
    <project>
      <groupId>com.grey.libraries</groupId>
      <artifactId>hive</artifactId>
      <version>1.0</version>
    </project>
```
 
in `pom.xml`, run

```bash
     mvn deploy:deploy-file 
         -Dfile=hive-1.0.jar 
         -DgroupId=com.grey.libraries   # ref. <groupId>com.grey.libraries</groupId> of pom.xml
         -DartifactId=hive           # ref. <artifactId>hive</artifactId> of pom.xml
         -Dversion=1.0                  # ref. <version>1.0</version> of pom.xml
         -Dpackaging=jar 
         -Durl=file:///[disk partition letter]:/[path to .m2]/.m2/repository -DrepositoryId=repository
```

**Notes in progress**

<br>

### ScalikeJDBC

**Project Purpose**: For interacting with databases via [ScalikeJDBC](http://scalikejdbc.org).

<br>

#### Building Scalike

Foremost

```bash
    mvn clean package
```

Subsequently

```bash
    cd scalike/target
```

and noting that the name of the ``.jar`` partly depends on the project tags

```xml
    <project>
      <groupId>com.grey.libraries</groupId>
      <artifactId>scalike</artifactId>
      <version>1.0</version>
    </project>
```
 
in `pom.xml`, run

```bash
     mvn deploy:deploy-file 
         -Dfile=scalike-1.0.jar 
         -DgroupId=com.grey.libraries   # ref. <groupId>com.grey.libraries</groupId> of pom.xml
         -DartifactId=scalike           # ref. <artifactId>scalike</artifactId> of pom.xml
         -Dversion=1.0                  # ref. <version>1.0</version> of pom.xml
         -Dpackaging=jar 
         -Durl=file:///[disk partition letter]:/[path to .m2]/.m2/repository -DrepositoryId=repository
```

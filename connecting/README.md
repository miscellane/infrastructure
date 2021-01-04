### Connecting

**Notes in progress**

<br>

#### Building Connecting

Foremost

```bash
    mvn clean package
```

Subsequently

```bash
    cd connecting/target
````

and noting that the name of the ``.jar`` partly depends on the project tags

```xml
    <project>
      <groupId>com.grey.libraries</groupId>
      <artifactId>connecting</artifactId>
      <version>1.0</version>
    </project>
```
 
in `pom.xml`, run

```bash
     mvn deploy:deploy-file 
         -Dfile=connecting-1.0.jar 
         -DgroupId=com.grey.libraries   # ref. <groupId>com.grey.libraries</groupId> of pom.xml
         -DartifactId=connecting        # ref. <artifactId>connecting</artifactId> of pom.xml
         -Dversion=1.0                  # ref. <version>1.0</version> of pom.xml
         -Dpackaging=jar 
         -Durl=file:///[disk partition letter]:/[path to .m2]/.m2/repository -DrepositoryId=repository
```

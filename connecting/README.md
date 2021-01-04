### Connecting

**Notes in progress**


<br>
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
        <artifactId>connecting</artifactId>
        <version>1.0</version>
    </project>
```
 
in `pom.xml`, run

```bash
     mvn deploy:deploy-file 
     -Dfile=connecting-1.0.jar 
     -DgroupId=com.grey.libraries 
     -DartifactId=connecting 
     -Dversion=1.0 
     -Dpackaging=jar 
     -Durl=file:///[disk partition letter]:/[path to .m2]/.m2/repository -DrepositoryId=repository
```

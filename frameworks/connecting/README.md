<br>

### Connecting

**Project Purpose**: A configurations data file reader, and parameters set builder,  for packages that interact with 
databases and/or CLI (Command Line Interface) applications.  A CLI application example is AWS CLI, i.e., 
[Amazon Web Services CLI](https://aws.amazon.com/cli/), which is used for interacting with [AWS S3](https://aws.amazon.com/s3/), 
[AWS Data Pipeline](https://aws.amazon.com/datapipeline/), [AWS EMR](https://aws.amazon.com/emr/), etc.  

Thus far, this project sets-up **database connection** and **application CLI credentials** parameters via their configuration files 
data, as outlined in [The Configuration Files](#the-configuration-files) section.  

<br>

#### The Configuration Files

<br>

**Case:** Databases

A configuration file named databases.conf is expected, and the expected in-file structure is

```yaml
    databases {
    
      sqlserver.{databasename} {
        db = "databasename",
        user = "username",
        password = "***",
        url = "jdbc:sqlserver://...windows.net:...;databaseName={databasename};encrypt=true;
                  trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30",
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
      },
      
      redshift.{databasename} {
        db = "databasename",
        user = "username",
        password = "***",
        url = "jdbc:redshift://...eu-west-1.redshift.amazonaws.com:{portNumber}/{databasename}",
        driver = "com.amazon.redshift.jdbc42.Driver"
      },
      
      ...
      
    }
    
```

<br>

**Case:** Credentials

A configuration file named credentials.conf is expected, and the expected in-file structure is rather flexible.  Only
the outermost key, **credentials**, is mandatory.  The structure thereof depends on the requirements of what is being 
served.

```yaml
    credentials { # mandatory
    
      # Sometimes greyhypotheses packages read & process data stored 
      # in AWS S3. The keys ...      
      aws { # Example: For AWS
        
        alpha { # user alpha 
          password = "***",
          accessKey = "***",
          secretKey = "***"
        },
        
        beta { # user beta
          password = "***",
          accessKey = "***",
          secretKey = "***"
        },
        
        ...
        
      },
      
      ...
       
    }
```

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

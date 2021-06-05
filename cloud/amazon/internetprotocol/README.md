<br>

# Interacting With Services Outwith of AWS

There are times whereby a program being run within an [Amazon EMR (Elastic MapReduce)](https://docs.aws.amazon.com/emr/latest/ManagementGuide/emr-what-is-emr.html) machine has to 
interact with cloud services outwith of AWS (Amazon Web Services).  For example, the program might have to read data 
from one or more [Azure SQL Databases](https://azure.microsoft.com/en-us/products/azure-sql/database/), amongst other places.  

<br>

* [Amazon EMR Programs & Azure SQL Databases](#amazon-emr-programs--azure-sql-databases)
    * [Bootstrap Action](#bootstrap-action)
    * [Underlying IP Association Software](#underlying-ip-association-software)
        * [The JAR](#the-jar)
        * [The Parameters JSON](#the-parameters-json)

<br>

## Amazon EMR Programs & Azure SQL Databases

Focusing on interactions with databases outwith of AWS, and using Azure SQL as an example, in order to seamlessly read 
data from an Azure SQL Database a set of security conditions must be met.  In general, and in addition to all 
required database credentials,  an Azure SQL Database will only accept database interaction requests from an 
IP (internet protocol) address that has been registered with the Azure SQL Database.

<br>

Alas, the default Amazon EMR Machine launch method randomly assigns a different IP address to an EMR Machine each 
time a machine is launched; once the machine is terminated, the IP address is available for re-assignment 
elsewhere in the universe.  Therefore, if we have an Amazon Data Pipeline that 

* runs every day for a few minutes, and 
* involves launching an EMR Machine that runs a program that interacts with one or more Azure SQL Databases

what are the options?  Due to costs, we definitely shouldn't leave an EMR Machine in an active state in 
order to retain its randomly & automatically assigned IP address.

<br>

A solution is to create a VPC Elastic IP Address that is always associated with any EMR Machine that is launched to 
run the program.  **Important** → Over time, a VPC Elastic IP Address might be associated with more than one task, 
when this occurs always ensure that the running periods of these tasks do not overlap.  After creating a VPC Elastic IP 
Address, ensure that

* the VPC Elastic IP Address is registered with each Azure SQL Database that the program interacts with.
* each time an EMR Machine is launched for the program, the machine is associated with the same VPC Elastic IP Address.

The latter point means that during the program's EMR Machine launch a set of [bootstrap actions](https://docs.aws.amazon.com/emr/latest/ManagementGuide/emr-plan-software.html) that associate a specified VPC 
Elastic IP Address with the machine is required.  

<br>

## Bootstrap Action

In brief, assuming an EMR Machine is launched via the script →

```bash
aws emr create-cluster 
--applications Name=Hadoop Name=Hive Name=Pig Name=Spark Name=Presto 
--ec2-attributes file://ec2attributes.json  
--release-label emr-6.3.0 
--log-uri 's3n://...' 
--bootstrap-actions file://bootstrap.json 
--instance-groups file://instancegroups.json 
--configurations file://configurations.json 
--no-visible-to-all-users 
--service-role EMR_DefaultRole 
--security-configuration 'Secure EMR S3 ...' 
--enable-debugging 
--name '...' 
--region eu-west-1 
--ebs-root-volume-size 65 
--tags 'ClusterFunction=DataScienceEngineering'

```

such that `bootstrap.json` associates the launch with the bootstrap actions script `bootstrap.sh`

```bash
[
  {
    "Path": "s3://.../bootstrap.sh",
    "Name": "Bootstrap Actions",
    "Args": []
  }
]
```

The bootstrap actions commands that would associate a specified VPC Elastic IP Address with an EMR Machine must be 
outlined within `bootstrap.sh`.  

<br>

## Underlying IP Association Software

This project creates a small software package that associates an EMR Machine with a specified VPC Elastic IP Address. 
If `internetprotocol...jar` is the package in question, and `parametersValues.json` is the required parameters 
file, then the  lines

```shell
#!/bin/bash
sudo aws configure set default.region ...
sudo aws s3 cp  s3://.../internetprotocol-assembly-0.1.jar /home/hadoop/
sudo aws s3 cp  s3://.../parametersValues.json /home/hadoop/

cd /home/hadoop/
java -jar internetprotocol-assembly-0.1.jar

```

execute the required association.  

<br>

### The JAR

The `.jar` is based on [InternetProtocolApp.scala](./src/main/scala/com/grey/InternetProtocolApp.scala)

<br>

### The Parameters JSON

The `parametersValues.json` file consists of 3 parameters. Its structure is

```json
{
  "clusterFunctionTagValue": "...",
  "clusterVPCID": "vpc-...",
  "allocationID": "eipalloc-..."
}
```

An EMR Machine is associated with a VPC Elastic IP Address via the IP address' `allocationID`.  Hence, the 
`allocationID` is compulsory.  The parameter `clusterFunctionTagValue` is optional, it denotes the tag value that is always 
associated with any EMR Machine that is launched for the analytics program in question.  Finally, `clusterVPCID` is 
the ID of the VPC within which the EMR instance is being launched.

<br>
<br>
<br>
<br>
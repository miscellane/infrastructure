<br>

An EMR Cluster launch example that

* Associates the EMR Cluster with a VPC Elastic IP Address ([more details](https://github.com/miscellane/infrastructure/tree/develop/cloud/amazon/internetprotocol)).
* Ensures that the cluster can run spark programs; via `steps.json`.
* Enables all the settings required for Amazon Athena interactions; via `configurations.json`.

<br>

```shell
  aws emr create-cluster \
  --applications Name=Hadoop Name=Hive Name=Pig Name=Spark Name=Presto \
  --ec2-attributes file://ec2attributes.json  \
  --steps file://steps.json \
  --release-label emr-6.3.0 \
  --log-uri 's3n://.../logs/emr/' \
  --bootstrap-actions file://bootstrap.json \
  --instance-groups file://instancegroups.json \
  --configurations file://configurations.json \
  --no-visible-to-all-users \
  --service-role EMR_DefaultRole \
  --security-configuration 'Secure EMR S3 Access' \
  --enable-debugging \
  --name 'Prototype' \
  --region eu-west-1 \
  --ebs-root-volume-size 65 \
  --tags 'ClusterFunction=Signals'

```

<br>
<br>

The Spark programs are outlined within `steps.json`.  Whilst testing programs/packages, setting

```shell
"ActionOnFailure": "CONTINUE"
```

within `steps.json` gives us the wherewithal to tunnel into a cluster's master node via

```shell
ssh -i KeyNameString.pem hadoop@ec2-xx-xx-xx-xx.xx-xx-x.compute.amazonaws.com
```

and hence investigate & address program/package problems.

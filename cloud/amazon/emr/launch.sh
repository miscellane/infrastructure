#!/bin/bash

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

#!/bin/bash

sudo aws configure set default.region eu-west-1
sudo aws s3 cp s3://.../internetprotocol-jar-with-dependencies.jar /home/hadoop/
sudo aws s3 cp s3://.../parametersValues.json /home/hadoop/

cd /home/hadoop/
java -jar internetprotocol-jar-with-dependencies.jar

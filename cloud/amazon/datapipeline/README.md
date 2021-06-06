<br>

A view of [architecture.json](https://nbviewer.jupyter.org/github/miscellane/infrastructure/blob/develop/cloud/amazon/datapipeline/architecture.json)

<br>

Pipeline:

```shell

  # Create a pipeline node, a code is returned
  >> aws datapipeline create-pipeline --name **PipelineName** --unique-id **PipelineCode**
  → {code}

  # Submit the pipeline design
  >> aws datapipeline put-pipeline-definition --pipeline-id {code} --pipeline-definition file://architecture.json

  # Add tags to the pipeline, if required.
  >> aws datapipeline add-tags --pipeline-id {code} --tags key=Purpose,value=SignalsProcessing

  # Activate the pipeline.
  >> aws datapipeline activate-pipeline --pipeline-id {code}

```

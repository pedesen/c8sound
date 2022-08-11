# c8sound

Project for Camunda Summer Hackdays 2022

## Content

* [resources/puredata/](resources/puredata) a bunch of (resources/puredata) [puredata](https://puredata.info/) patches to generate sound: 
* [resources/soundProcess.bpmn](resources/soundProcess.bpmn) Process Diagram to deploy to Camunda 8
* [resources/instance-creator.sh](resources/instance-creator.sh) Bash script to batch create process instances of soundProcess
* [zeebe-socket-exporter/](zeebe-socket-exporter) A custom zeebe exporter that filters events and sends them out via UDP

# c8sound - How does Camunda 8 Sound like?

Project for Camunda Summer Hackdays 2022

## In a nutshell

Export events from Zeebe (like incidents or process instance state changes) via UDP connection to puredata* to generate sound.

🔊 **HQ sound examples**: https://nextcloud.pedesen.de/s/RzTzbfawD6Rjwq5

![c8sound](https://user-images.githubusercontent.com/2543743/184417756-803acd1f-d4de-47dc-92b2-d4c9bfb1dcd4.jpg)

*puredata is a visual programmung language for dealing with audio streams

## Content

* [zeebe-socket-exporter/](zeebe-socket-exporter) A custom zeebe exporter that filters events and sends them out via a UDP network socket
* [resources/puredata/](resources/puredata) a bunch of (resources/puredata) [puredata](https://puredata.info/) patches to generate sound: 
* [resources/soundProcess.bpmn](resources/soundProcess.bpmn) Process Diagram to deploy to Camunda 8
* [resources/instance-creator.sh](resources/instance-creator.sh) Bash script to batch create process instances of soundProcess

## Run puredata

[Download](https://puredata.info/downloads) and start puredata

If you're using vanilla puredata add the moocow/bytes2any external library: Help -> Find Externals -> Search for moocow -> Click to install

Open the zeebe.pd patch from resources/puredata/

Start audio by clicking the **DSP** checkbox in the puredata main window.

## Run Zeebe broker with zeebe-socker exporter

First package the zeebe-socker-exporter as jar file:

```
cd zeebe-socket-exporter
mvn package
```

Then copy the `zeebe-socket-exporter-*.jar` from `zeebe-socket-exporter/target` to an exporter directory in your Zeebe folder and add some configuration to application.yaml:

```yaml
zeebe:
  broker:
    exporters:
      socket:
        className: io.camunda.SocketExporter
        jarPath: exporters/zeebe-socket-exporter-*.jar
```

Run zeebe

```
./bin/broker
```

🔊 Now you should be able to hear sounds whenever process instances are created, completed, or if there are incidents

## Deploy sound process and start instances (optional)

Deploy the process

```
zbctl --insecure deploy resource resources/soundProcess.bpmn
```

Example: Start 58 process instances with 21% incidents:

```
resources/instance-creator.sh 58 21
```

Or use your own processes

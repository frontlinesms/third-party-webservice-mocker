Third Party Web Service Mocker
==============================

This project helps you to mock 3rd party web services to allow full testing of your apps against 3rd party APIs.

# Running WSMocker

Clone this git repository, and start the server:

	run $portNumber $configFile

# Running your app

To run a Java app using WSMocker in place of 3rd party services, add the following JVM arguments:

	-Dhttp.proxyHost=localhost -Dhttp.proxyPort=$portNumber

# Release Notes

## in progress

## TODO

* grails plugin to provide launching and shutdown from _Events.groovy

## 0.1

* package as a JAR with jetty embedded
* enabled the proxy controller, so the tool is now functional!
* loading of config file at startup
* loading of config file via grails remote-control


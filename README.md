Third Party Web Service Mocker
==============================

This project helps you to mock 3rd party web services to allow full testing of your apps against 3rd party APIs.

# Running WSMocker

Clone this git repository, and start the server:

	run $portNumber $configFile

# Running your app

To run a Java app using WSMocker in place of 3rd party services, add the following JVM arguments:

	-Dhttp.proxyHost=localhost -Dhttp.proxyPort=$portNumber

# grails plugin

## installation

Add the following to grails-app/conf/BuildConfig.groovy:

	test ":wsmocker:${version}"

To configure the server for a particular test

	wsmocker.WsmockerTestUtils.remote {
		ctx.proxyService.loadConfig {
			'example.com' {
				'**' { 'OK' }
			}
		}
	}

# Release Notes

## in progress

## TODO

* listen for changes to the config file, and reload when it changes
* allow single * as well as ** as wildcard matcher(?)
* add support for wilcards in domain names
* reset server config at the start of each test file or test (probably the latter, except for stepwise specs?)
* simplify WsmockerTestUtils so that end-user reference to `ctx.proxyService.loadConfig` and nested closure is not required
* different rules for HTTP and HTTPS, e.g.

	'secure.example.com'(allowProtocol:'https') { ...rules... }
	'insecure.example.com'(restrictProtocol:'https') { ...rules... }

or perhaps

	'https://secure.example.com' { ...rules... }
	'http://insecure.example.com' { ...rules... }

## 0.1

* grails plugin which automatically configures proxy and runs it for integration and functional tests
* HTTPS support for grails plugin
* package as a JAR with jetty embedded
* enabled the proxy controller, so the tool is now functional!
* loading of config file at startup
* loading of config file via grails remote-control
* allow more restful API definitions, e.g.

	GET contact/:id
	PUT contact
	GET contacts

### known issues

* jetty sometimes fails to shutdown at the end of testing


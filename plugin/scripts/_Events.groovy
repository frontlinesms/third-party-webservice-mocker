def proxyProcess

eventTestPhaseStart = { phaseName ->
	if(phaseName in ['functional', 'integration']) {
		final String SERVER_STARTED_OK = 'server started ok. 473yt384nvy7n'
		final String libName = 'wsmocker'
		final String version = '0.1-SNAPSHOT'
		final String libDirPath = "lib/${libName}"
		def port = System.getProperty('wsmocker.server.port',
				(System.getProperty('server.port', '8080').toInteger() + 1).toString())
		try {
			proxyProcess = "java -jar ${libDirPath}/${libName}-${version}.jar port=$port".execute()
		} catch(Exception ex) {
			ex.printStackTrace()
			exit 100
		}
		try {
			proxyProcess.inputStream.eachLine { line ->
				println "PROXY :: OUT :: $line"
				if(line.startsWith('Server running.')) {
					throw new RuntimeException(SERVER_STARTED_OK)
				}
			}
			proxyProcess.errorStream.eachLine { line ->
				println "PROXY :: ERR :: $line"
			}

			println "Proxy startup failed."
			proxyProcess?.destroy()
			exit 101
		} catch(RuntimeException _) {
			if(_.message != SERVER_STARTED_OK) {
				_.printStackTrace()
				proxyProcess?.destroy()
				exit 102
			}
		}
	}
}

eventTestPhaseEnd = { phaseName ->
	if(phaseName in ['functional', 'integration']) {
		proxyProcess?.destroy()
	}
}


def proxyProcess

eventTestPhaseStart = { phaseName ->
	if(phaseName in ['functional', 'integration']) {
		final String libName = 'wsmocker'
		final String version = '0.1-SNAPSHOT'
		final String libDirPath = "lib/${libName}"
		proxyProcess = "java -jar ${baseDir}/${libDirPath}/${libName}-${version}.jar".execute()
		try {
			proxyProcess.inputStream.eachLine { line ->
				println "PROXY :: $line"
				if(line.startsWith('Server started.')) {
					throw new RuntimeException(SERVER_STARTED_OK)
				}
			}
		} catch(RuntimeException _) {
			if(_.message != SERVER_STARTED_OK) {
				throw _
			}
		}
	}
}

eventCompileEnd = {
	if(phaseName in ['functional', 'integration']) {
		proxyProcess?.destroy()
	}
}


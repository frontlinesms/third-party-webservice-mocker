class BootStrap {
	private static final String CONFIG_SYS_PROP = 'wsmocker.config'
	def proxyService

	def init = { servletContext ->
		def lifelongConfFile = System.properties[CONFIG_SYS_PROP]
		if(lifelongConfFile) {
			def configFile = new File(lifelongConfFile)
			if(!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
				throw new RuntimeException("Could not open file: $lifelongConfFile")
			}
			proxyService.loadConfig(configFile)
		} else {
			def errorMessage = "No config file was specified.  Either set up config with remote control, or set the '$CONFIG_SYS_PROP' system property using the commandline option -D$CONFIG_SYS_PROP=/path/to/config.groovy"
			log.warn errorMessage
			println "WARNING: $errorMessage"
		}
	}
	def destroy = {
	}
}


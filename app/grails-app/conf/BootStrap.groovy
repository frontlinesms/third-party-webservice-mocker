class BootStrap {
	def proxyConfigService

	def init = { servletContext ->
		def lifelongConfFile = System.properties['wsmocker.config']
		if(lifelongConfFile) {
			def configFile = new File(lifelongConfFile)
			if(!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
				throw new RuntimeException("Could not open file: $lifelongConfFile")
			}
			proxyConfigService.loadConfigFromFile(configFile)
		}
	}
	def destroy = {
	}
}


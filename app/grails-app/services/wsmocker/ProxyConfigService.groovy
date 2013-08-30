package wsmocker

class ProxyConfigService {
	private synchronized ruleset

	def handle(request, response) {
		ruleset.handle(request, response)
	}

	void loadConfigFromFile(String filePath) {
		loadConfigFromFile(new File(filePath))
	}

	void loadConfigFromFile(File f) {
		// Is this the nastiest way of loading a closure from a file ever?  Who knows...
		Closure ruleDefinition = new GroovyShell().evaluate('{->' + f.text + '}')
		ruleset = new ProxyDomainRuleParser().parse(ruleDefinition)
	}
}


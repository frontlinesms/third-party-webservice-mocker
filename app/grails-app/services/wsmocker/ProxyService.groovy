package wsmocker

class ProxyService {
	private final Object __LOCK__ = new Object()
	private ruleset

	def handle(request, response) {
		log.info "Handling request: ${request.properties.forwardURI}"
		synchronized(__LOCK__) {
			if(!ruleset) throw new IllegalStateException('No ruleset has been loaded for proxy service.')
			ruleset.handle(request, response)
		}
	}

	void loadConfig(String filePath) {
		loadConfig(new File(filePath))
	}

	void loadConfig(File f) {
		// Is this the nastiest way of loading a closure from a file ever?  Who knows...
		Closure ruleDefinition = new GroovyShell().evaluate('{->' + f.text + '}')
		loadConfig(ruleDefinition)
	}

	void loadConfig(Closure c) {
		def _ruleset = new ProxyDomainRuleParser().parse(c)
		synchronized(__LOCK__) {
			ruleset = _ruleset
		}
	}
}


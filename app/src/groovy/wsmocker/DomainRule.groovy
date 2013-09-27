package wsmocker

class DomainRule {
	private String domain
	private Map options
	private List<Closure> handlers
	DomainRule(String domain, Map options, List<Closure> handlers) {
		this.domain = domain
		this.options = options
		this.handlers = handlers
	}
	boolean matches(request) {
		request.properties.serverName == domain
	}
	Closure getHandler(request) {
		getRule(request)?.handler
	}
	PathRule getRule(request) {
		assert this.matches(request)
		for(h in handlers) {
			if(h.matches(request)) {
				return h
			}
		}
	}
}


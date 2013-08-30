package wsmocker

class ProxyRuleset {
	private List rules
	ProxyRuleset(List rules) { this.rules = rules }
	private getDomainHandler(request) {
		for(r in rules) {
			if(r.matches(request)) {
				return r
			}
		}
	}
	private Closure getPathHandler(request) {
		def domainHandler = getDomainHandler(request)
		if(domainHandler) {
			def requestHandler = domainHandler.getHandler(request)
			if(requestHandler) return requestHandler
		}
	}
	def handle(request, response) {
		def domainHandler = getDomainHandler(request)
		new RequestHandlerContext(domainHandler.options, getPathHandler(request), response).handle()
	}
}


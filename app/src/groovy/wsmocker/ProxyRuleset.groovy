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
			domainHandler.getHandler(request)
		}
	}
	def handle(request, response) {
		def domainHandler = getDomainHandler(request)
		if(!domainHandler) throw new NoProxyActionDefinedException()
		def handler = domainHandler.getHandler(request)
		if(!handler) throw new NoProxyActionDefinedException()
		new RequestHandlerContext(domainHandler.options, handler, response).handle()
	}
}


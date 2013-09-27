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
		def rule = domainHandler.getRule(request)
		if(!rule) throw new NoProxyActionDefinedException()
		new RequestHandlerContext(request, domainHandler.options, rule.pathMatcher, rule.handler, response).handle()
	}
}


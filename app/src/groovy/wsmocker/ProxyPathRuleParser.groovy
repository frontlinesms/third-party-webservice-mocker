package wsmocker

class ProxyPathRuleParser {
	private static final HTTP_METHODS = ['GET', 'POST', 'DELETE', 'PUT']
	private rules = []
	def methodMissing(String methodName, args) {
		def httpMethod
		if(methodName in HTTP_METHODS) {
			httpMethod = methodName
			methodName = args[0]
			args = args as List
			args.remove(0)
		}
		rules << new PathRule(httpMethod, methodName, args)
	}
	def parse(Closure ruleDef) {
		assert ruleDef != null
		ruleDef.delegate = this
		ruleDef.resolveStrategy = Closure.DELEGATE_FIRST
		ruleDef.call()
		rules
	}
}


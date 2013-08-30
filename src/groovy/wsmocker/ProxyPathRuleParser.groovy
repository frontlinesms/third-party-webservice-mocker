package wsmocker

class ProxyPathRuleParser {
	private rules = []
	def methodMissing(String methodName, args) {
		rules << new PathRule(methodName, args)
	}
	def parse(Closure ruleDef) {
		assert ruleDef != null
		ruleDef.delegate = this
		ruleDef.resolveStrategy = Closure.DELEGATE_FIRST
		ruleDef.call()
		rules
	}
}


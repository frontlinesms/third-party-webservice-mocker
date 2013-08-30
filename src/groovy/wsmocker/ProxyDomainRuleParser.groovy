package wsmocker

class ProxyDomainRuleParser {
	def rules = []
	def methodMissing(String methodName, args) {
		assert args.size() >= 1 && args.size() <= 2
		assert args[-1] instanceof Closure
		if(args.size() == 2) assert args[0] instanceof Map
		Map options = args.size() == 1? [:]: args[0]
		Closure ruleDef = args[-1]
		rules << new DomainRule(methodName, options, new ProxyPathRuleParser().parse(ruleDef))
	}
	def parse(Closure ruleDef) {
		ruleDef.delegate = this // object the rule 'methods' are actually called on
		ruleDef.resolveStrategy = Closure.DELEGATE_FIRST
		ruleDef.call()
		new ProxyRuleset(rules)
	}
}


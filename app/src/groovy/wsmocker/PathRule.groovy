package wsmocker

class PathRule {
	private String pathMatcher
	private Map options
	private Closure handler
	private respondToMethods
	PathRule(respondToMethods, String pathMatcher, args) {
		this.respondToMethods = respondToMethods
		this.pathMatcher = pathMatcher
		assert args.size() in (1..2)
		options = args.size() == 2? args[0]: [:]
		handler = args[-1]
	}
	boolean matches(request) {
		def path = request.properties.forwardURI
		return (!respondToMethods || request.method in respondToMethods) && (
				pathMatcher == '**' ||
				(pathMatcher == '/' && (path == '/' || path == '')) ||
				path[1..-1] == pathMatcher || matchesComplex(path[1..-1]))
	}
	boolean matchesComplex(path) {
		path ==~ pathMatcher.replaceAll(/:\w+/, /\\w+/)
	}
	Closure getHandler() { handler }
}


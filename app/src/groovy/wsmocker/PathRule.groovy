package wsmocker

class PathRule {
	private String pathMatcher
	private Map options
	private Closure handler
	PathRule(String pathMatcher, args) {
		this.pathMatcher = pathMatcher
		assert args.size() in (1..2)
		options = args.size() == 2? args[0]: [:]
		handler = args[-1]
	}
	boolean matches(request) {
		def path = request.properties.forwardURI
		return pathMatcher == '**' ||
				(pathMatcher == '/' && (path == '/' || path == '')) ||
				path[1..-1] == pathMatcher
	}
	Closure getHandler() { handler }
}


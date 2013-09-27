package wsmocker

import grails.converters.JSON

class RequestHandlerContext {
	private request
	private Map options
	private pathMatcher
	private response
	private handler
	
	RequestHandlerContext(request, Map options, pathMatcher, baseHandler, response) {
		this.request = request
		this.options = options
		this.pathMatcher = pathMatcher
		handler = baseHandler.clone()
		handler.delegate = this
		handler.resolveStrategy = Closure.DELEGATE_ONLY
		this.response = response
	}

	def propertyMissing(propertyName) {
		if(propertyName == 'params') {
			return request.parameterMap.collectEntries { k, v ->
				if(v.size() == 1) {
					v = v[0]
				}
				[(k):v]
			}
		}

		def m = pathMatcher =~ /(.*):$propertyName(\/.*)?/
		if(m.matches()) {
			def v = request.properties.forwardURI[1..-1] - m[0][1]
			if(v.contains('/')) {
				v = v[0..v.indexOf('/')-1]
			}
			return v
		} else {
			throw new MissingPropertyException("No such property: $propertyName for class: ${this.getClass().name}")
		}
	}
	
	def handle() {
		def responseBody = handler.call(response)
		if(options?.defaultFormat && !(responseBody instanceof String)) {
			return this."$options.defaultFormat"(responseBody)
		}
		return responseBody
	}
	def error(status, responseBody=null) {
		response.status = status
		if(responseBody) {
			responseBody instanceof Closure? responseBody.call(): responseBody
		}
	}
	def json = { obj ->
		return (obj as JSON).toString()
	}
}


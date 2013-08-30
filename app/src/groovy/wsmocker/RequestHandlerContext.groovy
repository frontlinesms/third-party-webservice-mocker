package wsmocker

import grails.converters.JSON

class RequestHandlerContext {
	private Map options
	private response
	private handler
	
	RequestHandlerContext(Map options, baseHandler, response) {
		this.options = options
		handler = baseHandler.clone()
		handler.delegate = this
		handler.resolveStrategy = Closure.DELEGATE_ONLY
		this.response = response
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


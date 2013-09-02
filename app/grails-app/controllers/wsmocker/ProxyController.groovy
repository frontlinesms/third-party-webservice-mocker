package wsmocker

import grails.converters.JSON

class ProxyController {
	def proxyService

	def index() {
		try {
			render text:proxyService.handle(request, response)
		} catch(NoProxyActionDefinedException ex) {
			return error(ex)
		}
	}

	private String getRequestUrl() {
		def p = request.properties
		"${p.serverName}${p.serverPort&&p.serverPort!='80'?':'+p.serverPort:''}${p.forwardURI}"
	}

	private void error(NoProxyActionDefinedException ex) {
		def errorMessage = "No proxy action defined for URL: $requestUrl"
		log.warn errorMessage
		render status:404, text:errorMessage
	}
}


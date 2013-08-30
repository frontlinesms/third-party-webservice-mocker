package wsmocker

import grails.plugin.spock.*
import spock.lang.*

class ProxyServiceSpec extends IntegrationSpec {
	def service

	def setup() {
		service = new ProxyService()
	}

	def 'can load config from a file'() {
		given:
			def request = [properties:[serverName:'example.com']]
			def response = [:]
		when:
			service.loadConfig('test/integration/data/ProxyServiceSpec_basic.groovy')
		then:
			service.handle(request, response) == 'OK'
	}
}


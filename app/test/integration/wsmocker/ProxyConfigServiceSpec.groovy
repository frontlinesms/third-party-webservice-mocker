package wsmocker

import grails.plugin.spock.*
import spock.lang.*

class ProxyConfigServiceSpec extends IntegrationSpec {
	def service

	def setup() {
		service = new ProxyConfigService()
	}

	def 'can load config from a file'() {
		given:
			def request = [properties:[serverName:'example.com']]
			def response = [:]
		when:
			service.loadConfigFromFile('test/integration/data/ProxyConfigServiceSpec_basic.groovy')
		then:
			service.handle(request, response) == 'OK'
	}
}


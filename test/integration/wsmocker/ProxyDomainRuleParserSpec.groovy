package wsmocker

import grails.plugin.spock.*
import spock.lang.*

class ProxyDomainRuleParserSpec extends IntegrationSpec {
	def ruleset
	def setup() {
		ruleset = new ProxyDomainRuleParser().parse {
			'api.clickatell.com'(defaultFormat:'json') {
					def remainingCredit = 100
					'credit' { [credit:remainingCredit] }
					'send' {
							if(remainingCredit--) [status:'OK']
							else [ERROR:102]
					}
					'**' { error(404, json([ERROR:101])) }
			}

			'integration.nexmo.com' {
					'credit' { '100' }
					'sms' { json(["SMS SENT OK"]) }
					'secret' { error(401, json([ERROR:'Restricted Zone'])) }
					'**' { error 404 }
			}

			'dumb.website' {
				'index.html' { '<html></html>' }
			}
		}
	}

	private mockRequest(domain, path) {
		[properties:[serverName:domain, forwardURI:path]]
	}

	private mockResponse() {
		[:]
	}

	def 'check for expected domain rules'() {
		expect:
			ruleset.getPathHandler(mockRequest("api.clickatell.com", "/credit")) != null
			ruleset.getPathHandler(mockRequest("api.clickatell.com", "/spoons")) != null
			ruleset.getPathHandler(mockRequest("dumb.website", "/index.html")) != null
			ruleset.getPathHandler(mockRequest("dumb.website", "/missing")) == null
	}

	@Unroll
	def 'check simple text handlers'() {
		when:
			def responseBody = ruleset.handle(mockRequest(domain, path), mockResponse())
		then:
			responseBody == expectedResponse
		where:
			domain                  | path          | expectedResponse
			'integration.nexmo.com' | '/credit'     | '100'
			'dumb.website'          | '/index.html' | '<html></html>'
	}

	def 'check JSON encoding'() {
		when:
			def responseBody = ruleset.handle(mockRequest("integration.nexmo.com", "/sms"), mockResponse())
		then:
			responseBody == '["SMS SENT OK"]'
	}

	def 'error with message'() {
		given:
			def response = mockResponse()
		when:
			def responseBody = ruleset.handle(mockRequest("integration.nexmo.com", "/secret"), response)
		then:
			responseBody == '{"ERROR":"Restricted Zone"}'
			response.status == 401
	}

	def 'stateful responder'() {
		when:
			def responseBody = ruleset.handle(mockRequest("api.clickatell.com", "/credit"), mockResponse())
		then:
			responseBody == '{"credit":100}'
		when:
			responseBody = ruleset.handle(mockRequest("api.clickatell.com", "/send"), mockResponse())
		then:
			responseBody == '{"status":"OK"}'
		when:
			responseBody = ruleset.handle(mockRequest("api.clickatell.com", "/credit"), mockResponse())
		then:
			responseBody == '{"credit":99}'
	}
}


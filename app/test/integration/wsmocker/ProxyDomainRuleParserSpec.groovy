package wsmocker

import grails.plugin.spock.*
import spock.lang.*

class ProxyDomainRuleParserSpec extends IntegrationSpec {
	private static final String PUT = 'PUT'
	private static final String POST = 'POST'
	private static final String GET = 'GET'
	private static final String DELETE = 'DELETE'

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

			'myplace.com' {
				'/' { 'Welcome home' }
			}

			'restland.com' {
				GET('objects') {
					'list'
				}
				POST('object') {
					'new'
				}
				GET('object/:id') {
					"get #$id"
				}
				DELETE('object/:id') {
					"delete #$id"
				}
				PUT('object/:id') {
					"update #$id"
				}
				'**' { 'error' }
			}

			'params.com' {
				'**' {
					params.xyz
				}
			}
		}
	}

	private mockRequest(domain, path, method='GET', Map params=[:]) {
		[properties:[serverName:domain, forwardURI:path], method:method, params:params]
	}

	private mockResponse() {
		[:]
	}

	def 'params should be available from the request'() {
		expect:
			ruleset.handle(mockRequest('params.com', '/', 'GET', [xyz:'123']), mockResponse()) == '123'
	}

	@Unroll
	def 'empty path definition should be legal'() {
		expect:
			ruleset.getPathHandler(mockRequest('myplace.com', path))
		where:
			path << ['', '/']
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

	def 'handling unmatched request domain should throw an exception'() {
		when:
			ruleset.handle(mockRequest('invented.org', 'anything'), mockResponse())
		then:
			thrown(NoProxyActionDefinedException)
	}

	def 'handling unmatched request path should throw an exception'() {
		when:
			ruleset.handle(mockRequest('dumb.website', 'anything'), mockResponse())
		then:
			thrown(NoProxyActionDefinedException)
	}

	@Unroll
	def 'rest responder for #method /#path'() {
		when:
			def responseBody = ruleset.handle(mockRequest('restland.com', "/$path", method), mockResponse())
		then:
			responseBody == expectedResponse
		where:
			path      | method | expectedResponse
			'objects' | GET    | 'list'
			'objects' | POST   | 'error'
			'objects' | PUT    | 'error'
			'objects' | DELETE | 'error'
			'object'  | POST   | 'new'
			'object/1'| POST   | 'error'
			'object/1'| GET    | 'get #1'
			'object/2'| GET    | 'get #2'
			'object/1'| DELETE | 'delete #1'
			'object/2'| DELETE | 'delete #2'
			'object/1'| PUT    | 'update #1'
			'object/2'| PUT    | 'update #2'
			'object/1'| POST   | 'error'
	}
}


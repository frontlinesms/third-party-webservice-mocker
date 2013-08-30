package wsmocker

import geb.spock.GebReportingSpec
import grails.plugin.remotecontrol.RemoteControl
import grails.plugin.spock.*
import spock.lang.*

class RemoteConfigLoadSpec extends GebReportingSpec {
	@Shared def remote = new RemoteControl()

	def 'no setup should be loaded by default'() {
		expect:
			remote.exec {
				try {
					ctx.proxyService.handle([:], [:])
				} catch(IllegalStateException ex) {
					return true
				}
				return false
			}
	}

	def 'setup can be loaded via a remote closure'() {
		given:
			remote.exec {
				ctx.proxyService.loadConfig {
					'example.com' {
						'test-page' { 'good response' }
					}
				}
			}
		when:
			def response = remote.exec {
				ctx.proxyService.handle([properties:[serverName:'example.com', forwardURI:'/test-page']], [:])
			}
		then:
			response == 'good response'
	}
}


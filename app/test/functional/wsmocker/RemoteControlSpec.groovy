package wsmocker

import geb.spock.GebReportingSpec
import grails.plugin.remotecontrol.RemoteControl
import grails.plugin.spock.*
import spock.lang.*

class RemoteControlSpec extends GebReportingSpec {
	@Shared def remote = new RemoteControl()

	def 'test that normal remote works ok'() {
		expect: 'A class only availabel in the main app is accessible when using remote control.'
			remote.exec {
				wsmocker.TestObject.OK && app.applicationMeta['app.name'] == 'wsmocker'
			} == true
	}

	def 'test some stuff a bit weird!'() {
		given:
			WsmockerTestUtils.remote {
				ctx.proxyService.loadConfig {
					'amazon.co.uk' {
						'**' { 'nice' }
					}
				}
			}
		expect:
			'http://amazon.co.uk/index'.toURL().text == 'nice'
	}
}


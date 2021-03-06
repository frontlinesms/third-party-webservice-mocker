grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
	def seleniumVersion = '2.32.0'

	inherits 'global'
	log 'error' // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums true // Whether to verify checksums on resolve
	legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

	repositories {
		inherits true // Whether to inherit repository definitions from plugins

		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenLocal()
		mavenCentral()
	}

	dependencies {
		test 'org.spockframework:spock-grails-support:0.7-groovy-2.0'
		test 'org.gebish:geb-spock:0.9.0'
		test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
		test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
	}

	plugins {
		runtime ":hibernate:$grailsVersion"
		build ":tomcat:$grailsVersion"
		test ':spock:0.7', {
			exclude "spock-grails-support"
		}
		test ':geb:0.9.0'
		compile ':remote-control:1.4'
		build ':standalone:1.2.1'
	}
}


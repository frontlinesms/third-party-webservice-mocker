grails.project.work.dir = 'target'
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
	inherits 'global'
	log 'warn'

	repositories {
		grailsHome()
		mavenLocal()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
	}

	plugins {
		build ':release:2.2.1',
				':rest-client-builder:1.0.3', {
			export = false
		}
		test ':spock:0.7', {
			exclude "spock-grails-support"
		}
		test ':tomcat:2.2.3'
		test ':hibernate:2.2.3'
		runtime ':remote-control:1.4'
	}
}


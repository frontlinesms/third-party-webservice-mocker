class UrlMappings {
	static excludes = ['/grails-remote-control']
	static mappings = {
		"/**"(controller:'proxy')
	}
}


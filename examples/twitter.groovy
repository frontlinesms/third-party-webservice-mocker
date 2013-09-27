'api.twitter.com'(defaultFormat:'json') {
	def lastId = 0
	def tweets = [:]
	GET('1.1/statuses/show/:id') {
		tweets.find { it.id == id }
	}
	POST('1.1/statuses/update') {
		tweets << [id:(++lastId as String), text:params.status]
		[id:lastId]
	}
}


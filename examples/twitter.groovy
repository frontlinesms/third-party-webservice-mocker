'api.twitter.com' {
	'**' {
		json([error:'No action defined'])
	}
	def tweetz = [[id:1, content:'hello'], [id:2, content:'nonsense']]
	tweets {
		json(tweets)
	}
	'new' {
		tweets << [content:'newly generated tweet']
	}
}


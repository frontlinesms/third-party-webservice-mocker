target(name:'installWsmockerLib') {
	final String libName = 'wsmocker'
	final String libDirPath = "lib/${libName}"
	final File libDir = new File(libDirPath)
	final String version = '0.1-SNAPSHOT'
	final String requiredJar = "${libName}-${version}"
	final String artefactDescription = "net.frontlinesms.test:${libName}:${version}"

	def f = new File(libDir, requiredJar)
	if(!f.exists()) {
		libDir.mkdirs()
		new File(libDir, '.gitignore').text = '/*'

		def proc = "grails --stacktrace install-dependency ${artefactDescription} --dir=${libDir}".execute()
		proc.errorStream.eachLine {
			println "InstallWsMockerLib() :: ERR: $it"
		}
		proc.inputStream.eachLine {
			println "InstallWsMockerLib() :: OUT: $it"
		}
		proc.waitFor()
		def status = proc.exitValue()
		if(status != 0) {
			fail message:"There was a problem installing the dependency: ${artefactDescription}.  You may need to install it manually.", status:status
		}
	}
}

setDefaultTarget installWsmockerLib


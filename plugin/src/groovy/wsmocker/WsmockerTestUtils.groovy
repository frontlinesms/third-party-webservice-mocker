package wsmocker

import groovyx.remote.client.RemoteControl
import groovyx.remote.transport.http.HttpTransport

class WsmockerTestUtils {
	/** The domain of this address is not important, as we're running through
	 * the proxy and it will ignore any reference to grails-remote-control */
	private static final String REMOTE_ADDR = 'http://wherever/grails-remote-control'

	@Lazy static remoteControl = new RemoteControl(new HttpTransport(REMOTE_ADDR))

	// TODO work out how to get this to properly exec `ctx.proxyService.loadConfig c`
	// N.B. tried with usedClosures but didn't work
	static void remote(Closure c) {
		remoteControl.exec c
	}
}


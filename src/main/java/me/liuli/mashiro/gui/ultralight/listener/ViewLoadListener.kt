package me.liuli.mashiro.gui.ultralight.listener

import com.labymedia.ultralight.plugin.loading.UltralightLoadListener
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.ultralight.page.View

class ViewLoadListener(private val view: View) : UltralightLoadListener {

    /**
     * Helper function to construct a name for a frame from a given set of parameters.
     *
     * @param frameId     The id of the frame
     * @param isMainFrame Whether the frame is the main frame on the page
     * @param url         The URL of the frame
     * @return A formatted frame name
     */
    private fun frameName(frameId: Long, isMainFrame: Boolean, url: String): String {
        return "[${if (isMainFrame) "MainFrame" else "Frame"} $frameId ($url)]: "
    }

    /**
     * Called by Ultralight when a frame in a view beings loading.
     *
     * @param frameId     The id of the frame that has begun loading
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame started to load
     */
    override fun onBeginLoading(frameId: Long, isMainFrame: Boolean, url: String) {
        Mashiro.ultralightManager.logger.debug("${frameName(frameId, isMainFrame, url)}The view is about to load")
    }

    /**
     * Called by Ultralight when a frame in a view finishes loading.
     *
     * @param frameId     The id of the frame that finished loading
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url the frame has loaded
     */
    override fun onFinishLoading(frameId: Long, isMainFrame: Boolean, url: String) {
        Mashiro.ultralightManager.logger.info("${frameName(frameId, isMainFrame, url)}The view finished loading")
    }

    /**
     * Called by Ultralight when a frame in a view fails to load.
     *
     * @param frameId     The id of the frame that failed to load
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that failed to load
     * @param description A description of the error
     * @param errorDomain The domain that failed to load
     * @param errorCode   An error code indicating the error reason
     */
    override fun onFailLoading(frameId: Long, isMainFrame: Boolean, url: String, description: String, errorDomain: String, errorCode: Int) {
        Mashiro.ultralightManager.logger.error("${frameName(frameId, isMainFrame, url)}Failed to load $errorDomain, $errorCode($description)")
    }

    /**
     * Called by Ultralight when the history of a view changes.
     */
    override fun onUpdateHistory() { }

    /**
     * Called by Ultralight when the window object is ready. This point can be used to inject Javascript.
     *
     * @param frameId     The id of the frame that the object became ready in
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame currently contains
     */
    override fun onWindowObjectReady(frameId: Long, isMainFrame: Boolean, url: String) {
        // TODO: JSContent
        view.ultralightView.lockJavascriptContext().context
    }

    /**
     * Called by Ultralight when the DOM is ready. This point can be used to inject Javascript.
     *
     * @param frameId     The id of the frame that the DOM became ready in
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame currently contains
     */
    override fun onDOMReady(frameId: Long, isMainFrame: Boolean, url: String) { }

}
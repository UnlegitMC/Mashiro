package me.liuli.mashiro.gui.ultralight.page

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.config.UltralightViewConfig
import com.labymedia.ultralight.input.UltralightKeyEvent
import com.labymedia.ultralight.input.UltralightMouseEvent
import com.labymedia.ultralight.input.UltralightScrollEvent
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.ultralight.RenderLayer
import me.liuli.mashiro.gui.ultralight.listener.ViewListener
import me.liuli.mashiro.gui.ultralight.listener.ViewLoadListener
import me.liuli.mashiro.gui.ultralight.script.UltralightJsContext
import me.liuli.mashiro.util.MinecraftInstance

open class View(val layer: RenderLayer, private val viewRenderer: ViewRenderer) : MinecraftInstance() {

    val ultralightView: UltralightView

    val context: UltralightJsContext

    var viewingPage: Page? = null

    private var jsGarbageCollected = 0L

    init {
        // Setup view
        val width=mc.displayWidth
        val height=mc.displayHeight
        val viewConfig = UltralightViewConfig()
            .isTransparent(true)
            .initialDeviceScale(1.0)

        // Make sure renderer setups config correctly
        viewRenderer.setupConfig(viewConfig)

        ultralightView=Mashiro.ultralightManager.renderer.createView(width.toLong(), height.toLong(), viewConfig)
        ultralightView.setViewListener(ViewListener())
        ultralightView.setLoadListener(ViewLoadListener(this))

        // Setup JS bindings
        context = UltralightJsContext(this, ultralightView)
    }

    /**
     * Loads the specified [page]
     */
    fun loadPage(page: Page) {
        // Unregister listeners
//        context.events._unregisterEvents() TODO: EVENTS

        if (viewingPage != page && viewingPage != null) {
            page.close()
        }

        ultralightView.loadURL(page.viewableFile)
        viewingPage = page
    }

    /**
     * Update view
     */
    fun update() {
        // Check if page has new update
        val page = viewingPage

        if (page?.hasUpdate() == true) {
            loadPage(page)
        }

        // Collect JS garbage
        collectGarbage()
    }

    /**
     * Render view
     */
    open fun render() {
        viewRenderer.render(ultralightView)
    }

    /**
     * Resizes web view to [width] and [height]
     */
    fun resize(width: Long, height: Long) {
        ultralightView.resize(width, height)
        Mashiro.ultralightManager.logger.debug("Successfully resized to $width:$height")
    }

    /**
     * Garbage collect JS engine
     */
    private fun collectGarbage() {
        if (jsGarbageCollected == 0L) {
            jsGarbageCollected = System.currentTimeMillis()
        } else if (System.currentTimeMillis() - jsGarbageCollected > 1000) {
            Mashiro.ultralightManager.logger.debug("Garbage collecting Ultralight Javascript...")
            ultralightView.lockJavascriptContext().context.garbageCollect()
            jsGarbageCollected = System.currentTimeMillis()
        }
    }

    /**
     * Free view
     */
    fun free() {
        ultralightView.unfocus()
        ultralightView.stop()
        viewingPage?.close()
        viewRenderer.delete()
//        context.events._unregisterEvents() TODO: EVENTS
    }

    fun focus() {
        ultralightView.focus()
    }

    fun unfocus() {
        ultralightView.unfocus()
    }

    fun fireScrollEvent(event: UltralightScrollEvent) {
        ultralightView.fireScrollEvent(event)
    }

    fun fireMouseEvent(event: UltralightMouseEvent) {
        ultralightView.fireMouseEvent(event)
    }

    fun fireKeyEvent(event: UltralightKeyEvent) {
        ultralightView.fireKeyEvent(event)
    }
}
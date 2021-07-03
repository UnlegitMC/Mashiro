package me.liuli.mashiro.gui.ultralight.page

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.config.UltralightViewConfig

// TODO: Copy&Paste GPUViewRenderer after CCBlueX fix it
/**
 * Render Views
 * @author CCBlueX
 */
interface ViewRenderer {

    /**
     * Setup [viewConfig]
     */
    fun setupConfig(viewConfig: UltralightViewConfig)

    /**
     * Render view
     */
    fun render(view: UltralightView)

    /**
     * Delete
     */
    fun delete()

}
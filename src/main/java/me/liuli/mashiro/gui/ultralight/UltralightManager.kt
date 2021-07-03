package me.liuli.mashiro.gui.ultralight

import com.labymedia.ultralight.UltralightJava
import com.labymedia.ultralight.UltralightPlatform
import com.labymedia.ultralight.UltralightRenderer
import com.labymedia.ultralight.config.FontHinting
import com.labymedia.ultralight.config.UltralightConfig
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil
import com.labymedia.ultralight.plugin.logging.UltralightLogLevel
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.client.GuiLoadingClient
import me.liuli.mashiro.gui.ultralight.adaptor.ClipBoardAdaptor
import me.liuli.mashiro.gui.ultralight.adaptor.MCEventAdaptor
import me.liuli.mashiro.gui.ultralight.page.CPUViewRenderer
import me.liuli.mashiro.gui.ultralight.page.ScreenView
import me.liuli.mashiro.gui.ultralight.page.View
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.client.ClientUtils
import me.liuli.mashiro.util.exception.PCUnsupportedException
import me.liuli.mashiro.util.file.FileUtils
import me.liuli.mashiro.util.file.NetUtils
import net.minecraft.client.gui.GuiScreen
import org.apache.logging.log4j.LogManager
import java.io.File
import java.net.URL

class UltralightManager : MinecraftInstance() {
    val ultralightDir=File(mc.mcDataDir,".ultralight")
    val nativesDir=File(ultralightDir,"natives")
    val resourcesDir=File(ultralightDir,"resources")
    val dataDir=File(ultralightDir,"data")
    val cacheDir=File(ultralightDir,"cache")

    val platform: UltralightPlatform
    val renderer: UltralightRenderer
    val eventAdaptor: MCEventAdaptor

    val views=mutableListOf<View>()

    val logger = LogManager.getLogger("Ultralight")

    init {
        if(!ultralightDir.exists())
            ultralightDir.mkdirs()

        if(!cacheDir.exists())
            cacheDir.mkdir()

        if(!nativesDir.exists()){
            if(mc.currentScreen is GuiLoadingClient)
                (mc.currentScreen as GuiLoadingClient).displayString="ultralight natives (from web)"
            // download natives
            val name= ClientUtils.getSystemType().resourceName
            if(name == "unknown")
                throw PCUnsupportedException("The OS on your PC cannot works with UltraLight (os=${System.getProperty("os.name")})")

            val zipFile=File(nativesDir,"$name.zip")
            println("${Mashiro.fileManager.fileStorageServer}/ultralight/$name.zip")
            nativesDir.mkdir()
            NetUtils.downloadFile(URL("${Mashiro.fileManager.fileStorageServer}/ultralight/$name.zip"),zipFile)
            FileUtils.extractZip(zipFile,nativesDir)
            zipFile.delete()
        }

        if(!resourcesDir.exists()){
            if(mc.currentScreen is GuiLoadingClient)
                (mc.currentScreen as GuiLoadingClient).displayString="ultralight resources (from web)"

            val zipFile=File(resourcesDir,"resources.zip")
            resourcesDir.mkdir()
            NetUtils.downloadFile(URL("${Mashiro.fileManager.fileStorageServer}/ultralight/resources.zip"),zipFile)
            FileUtils.extractZip(zipFile,resourcesDir)
            zipFile.delete()
        }

        if(mc.currentScreen is GuiLoadingClient)
            (mc.currentScreen as GuiLoadingClient).displayString="ultralight engine"

        UltralightJava.load(nativesDir.toPath())
        UltralightGPUDriverNativeUtil.load(nativesDir.toPath())

        platform=UltralightPlatform.instance()
        platform.setConfig(UltralightConfig()
            .animationTimerDelay(1.0 / 60)
            .scrollTimerDelay(1.0 / 60)
            .resourcePath(resourcesDir.absolutePath)
            .cachePath(cacheDir.absolutePath)
            .fontHinting(FontHinting.SMOOTH))
        platform.usePlatformFontLoader()
        platform.usePlatformFileSystem(dataDir.absolutePath)
        platform.setClipboard(ClipBoardAdaptor())
        platform.setLogger { level, message ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (level) {
                UltralightLogLevel.ERROR -> logger.error(message)
                UltralightLogLevel.WARNING -> logger.warn(message)
                UltralightLogLevel.INFO -> logger.info(message)
            }
        }

        renderer=UltralightRenderer.create()

        eventAdaptor=MCEventAdaptor(this)
        Mashiro.eventManager.registerListener(eventAdaptor)
    }

    fun getActiveView() = views.find { it is ScreenView && mc.currentScreen == it.screen }

    fun update() {
        views.forEach(View::update)
        renderer.update()
    }

    fun render(layer: RenderLayer) {
        renderer.render()

        views.filter { it.layer == layer }
            .forEach(View::render)
    }

    fun resize(width: Long, height: Long) {
        views.forEach { it.resize(width, height) }
    }

    fun newOverlayView() = View(RenderLayer.OVERLAY_LAYER, newViewRenderer()).also { views += it }

    fun newScreenView(screen: GuiScreen, adaptedScreen: GuiScreen? = null, parentScreen: GuiScreen? = null) =
        ScreenView(newViewRenderer(), screen, adaptedScreen, parentScreen).also { views += it }

    fun removeView(view: View) {
        view.free()
        views.remove(view)
    }

    fun addView(view: View){
        views.add(view)
    }

    private fun newViewRenderer() = CPUViewRenderer()
}

enum class RenderLayer {
    OVERLAY_LAYER, SCREEN_LAYER
}
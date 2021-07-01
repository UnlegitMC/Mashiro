package me.liuli.mashiro.ultralight

import com.labymedia.ultralight.UltralightJava
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.client.GuiLoadingClient
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.client.ClientUtils
import me.liuli.mashiro.util.exception.PCUnsupportedException
import me.liuli.mashiro.util.file.FileUtils
import me.liuli.mashiro.util.file.NetUtils
import java.io.File
import java.net.URL

class UltraLightManager : MinecraftInstance() {
    val ultralightDir=File(mc.mcDataDir,".ultralight")
    val nativesDir=File(ultralightDir,"natives")
    val resourcesDir=File(ultralightDir,"resources")

    init {
//        Mashiro.fileManager.checkUltraLight()
        if(!ultralightDir.exists())
            ultralightDir.mkdirs()

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
    }
}
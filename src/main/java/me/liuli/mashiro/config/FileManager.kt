package me.liuli.mashiro.config

import com.google.gson.JsonParser
import me.liuli.mashiro.gui.client.GuiLoadingClient
import me.liuli.mashiro.util.MinecraftInstance
import net.minecraft.util.HttpUtil
import java.net.URL

class FileManager() : MinecraftInstance() {
    val isChinese: Boolean

    /**
     * Github is blocked in China, so we need to download it from a proxy
     */
    val fileStorageServer: String

    init {
        if (mc.currentScreen is GuiLoadingClient) {
            (mc.currentScreen as GuiLoadingClient).displayString = "files"
        }

        isChinese = try {
            JsonParser().parse(HttpUtil.get(URL("http://ip-api.com/json?fields=countryCode"))).asJsonObject.get("countryCode").asString.equals("CN")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        fileStorageServer = if (isChinese) {
            "https://hub.fastgit.org/Project-EZ4H/MashiroFiles/raw/master"
        } else {
            "https://github.com/Project-EZ4H/MashiroFiles/raw/master"
        }
    }
}
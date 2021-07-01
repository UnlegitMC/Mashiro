package me.liuli.mashiro.gui.alt

import com.google.gson.JsonElement
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.file.NetUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.HttpUtil
import net.minecraft.util.ResourceLocation
import java.io.File
import java.io.StringReader
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import javax.imageio.ImageIO

class AltSkin(val uuid: UUID) {
    var resourceLocation: ResourceLocation?=null
    val skinCacheFile=File(Mashiro.altManager.altCachePath,"$uuid.png")

    init {
        Thread {
            try {
                var afterDL=false
                if(!skinCacheFile.exists()){
                    downloadSkin()
                }else{
                    afterDL=true
                }

                Minecraft.getMinecraft().addScheduledTask {
                    resourceLocation=ResourceLocation("mashiro/alt/$uuid-skin")
                    Minecraft.getMinecraft().textureManager.loadTexture(resourceLocation, DynamicTexture(ImageIO.read(skinCacheFile)))
                }

                // to make the skin file latest
                if(afterDL){
                    Thread.sleep(5000)
                    downloadSkin()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }.start()
    }

    private fun phaseMalformJson(str: String):JsonElement{
        val jsonReader = JsonReader(StringReader(str))
        jsonReader.isLenient=true
        return Streams.parse(jsonReader)
    }

    fun downloadSkin(){
        val resp=phaseMalformJson(HttpUtil.get(URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid"))).asJsonObject
        val value=phaseMalformJson(String(Base64.getDecoder().decode(resp.getAsJsonArray("properties")[0].asJsonObject.get("value").asString), Charset.forName("utf-8"))).asJsonObject

        NetUtils.downloadFile(URL(value.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").asString),skinCacheFile)
    }
}
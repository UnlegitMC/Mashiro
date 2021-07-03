package me.liuli.mashiro.util.client

import com.google.gson.JsonObject
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.MinecraftInstance
import net.minecraft.client.settings.GameSettings
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.Display
import java.lang.reflect.Field

object ClientUtils : MinecraftInstance() {
    val logger = LogManager.getLogger(Mashiro.name)

    private var fastRenderField: Field? = null

    init{
        try {
            fastRenderField = GameSettings::class.java.getDeclaredField("ofFastRender")

            if (!fastRenderField!!.isAccessible)
                fastRenderField!!.isAccessible = true
        } catch (ignored: NoSuchFieldException) {
        }
    }

    fun logInfo(msg: String){
        logger.info(msg)
    }

    fun logWarn(msg: String){
        logger.warn(msg)
    }

    fun disableFastRender() {
        try {
            if (fastRenderField != null) {
                fastRenderField!!.setBoolean(mc.gameSettings, false)
            }
        } catch (ignored: IllegalAccessException) {
        }
    }

    fun displayAlert(message: String) {
        displayChatMessage("§7[${Mashiro.coloredName}§7] §f$message")
    }

    fun displayChatMessage(message: String) {
        if (mc.thePlayer == null) {
            logInfo("[CHAT] $message")
            return
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("text", message)
        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()))
    }

    fun setTitle(status: String? = null){
        Display.setTitle("${Mashiro.name} v${Mashiro.version} by ${Mashiro.author}"+
                if(status!=null&&status.isNotEmpty()){" | $status"}else{""})
    }

    fun getSystemType():EnumSystemType{
        val os=System.getProperty("os.name")

        return when{
            os.contains("win",true) -> EnumSystemType.WINDOWS
            os.contains("mac",true) || os.contains("darwin",true) -> EnumSystemType.MACOSX
            os.contains("nux",true) || os.contains("nix",true) || os.contains("aix",true) -> EnumSystemType.LINUX
            else -> EnumSystemType.UNKNOWN
        }
    }

    enum class EnumSystemType(val friendlyName: String, val resourceName: String){
        WINDOWS("Windows","win"),
        MACOSX("MacOSX","mac"),
        LINUX("Linux","linux"),
        UNKNOWN("Unknown","unknown")
    }
}
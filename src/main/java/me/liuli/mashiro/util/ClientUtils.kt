package me.liuli.mashiro.util

import com.google.gson.JsonObject
import me.liuli.mashiro.Mashiro
import net.minecraft.client.settings.GameSettings
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field

object ClientUtils : MinecraftInstance() {
    private val logger = LogManager.getLogger(Mashiro.name)

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
}
package me.liuli.mashiro.util

import me.liuli.mashiro.Mashiro
import net.minecraft.client.settings.GameSettings
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
}
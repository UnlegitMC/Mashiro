package me.liuli.mashiro.util

import me.liuli.mashiro.Mashiro
import org.apache.logging.log4j.LogManager

object ClientUtils {
    private val logger = LogManager.getLogger(Mashiro.name)

    fun logInfo(msg: String){
        logger.info(msg)
    }

    fun logWarn(msg: String){
        logger.warn(msg)
    }
}
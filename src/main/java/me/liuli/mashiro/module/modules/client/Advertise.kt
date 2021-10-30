package me.liuli.mashiro.module.modules.client

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.WorldEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import java.util.*

class Advertise : Module("Advertise", "lol", ModuleCategory.CLIENT, defaultOn = true, array = false) {
    @EventMethod
    fun onWorld(event: WorldEvent) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                mc.thePlayer.sendChatMessage(when (mc.gameSettings.language.substring(0, 2).lowercase()) {
                    "zh" -> "家人们我在使用 -> Mashiro Client <- by Liuli"
                    "en" -> "WTF Guys I'm on -> Mashiro Client <- by Liuli"
                    else -> "MDS Galera estou no -> Mashiro Client <- por Liuli"
                })
            }
        }, 1000L)
    }
}
package me.liuli.mashiro

import me.liuli.mashiro.event.EventManager
import me.liuli.mashiro.util.ClientUtils

object Mashiro {
    @JvmStatic
    val name="Mashiro"
    @JvmStatic
    val version="0.0.1"
    @JvmStatic
    val author="Liulihaocai"

    lateinit var eventManager: EventManager

    fun init(){
        ClientUtils.logInfo("Initialize $name v$version")
        eventManager=EventManager()
    }

    fun load(){
        ClientUtils.logInfo("Load $name v$version")
    }
}
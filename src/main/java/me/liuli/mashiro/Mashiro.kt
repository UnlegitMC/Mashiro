package me.liuli.mashiro

import me.liuli.mashiro.event.EventManager

object Mashiro {
    @JvmStatic
    val name="Mashiro"

    @JvmStatic
    val version="1.0.0"

    @JvmStatic
    val author="Liulihaocai"

    lateinit var eventManager: EventManager

    fun init(){

    }

    fun load(){
        eventManager=EventManager()
    }
}
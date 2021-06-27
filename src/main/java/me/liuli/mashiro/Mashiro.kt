package me.liuli.mashiro

import me.liuli.mashiro.command.CommandManager
import me.liuli.mashiro.config.ConfigManager
import me.liuli.mashiro.event.EventManager
import me.liuli.mashiro.gui.font.FontManager
import me.liuli.mashiro.module.ModuleManager
import me.liuli.mashiro.util.ClientUtils

object Mashiro {
    @JvmStatic
    val name="Mashiro"
    @JvmStatic
    val coloredName="§eM§fashiro"
    @JvmStatic
    val version="0.0.1"
    @JvmStatic
    val author="Liulihaocai"

    lateinit var eventManager: EventManager
    lateinit var configManager: ConfigManager
    lateinit var commandManager: CommandManager
    lateinit var moduleManager: ModuleManager
    lateinit var fontManager: FontManager

    fun init(){
        ClientUtils.logInfo("Initialize $name v$version")
        eventManager = EventManager()
    }

    fun load(){
        ClientUtils.logInfo("Loading $name v$version")

        configManager = ConfigManager()
        eventManager.registerListener(configManager)

        commandManager = CommandManager()

        moduleManager = ModuleManager()
        eventManager.registerListener(moduleManager)

        fontManager = FontManager()
        fontManager.loadFonts()

        configManager.loadDefault()

        ClientUtils.disableFastRender()
    }

    fun shutdown(){
        configManager.save()
    }
}
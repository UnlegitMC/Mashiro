package me.liuli.mashiro

import me.liuli.mashiro.command.CommandManager
import me.liuli.mashiro.config.ConfigManager
import me.liuli.mashiro.config.FileManager
import me.liuli.mashiro.event.EventManager
import me.liuli.mashiro.gui.client.GuiLoadingClient
import me.liuli.mashiro.gui.font.FontManager
import me.liuli.mashiro.module.ModuleManager
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.client.ClientUtils

object Mashiro : MinecraftInstance() {
    @JvmStatic
    val name = "Mashiro"
    @JvmStatic
    val coloredName = "§eM§fashiro"
    @JvmStatic
    val version = "1.0.0"
    @JvmStatic
    val author = "Liulihaocai"

    lateinit var eventManager: EventManager
    lateinit var configManager: ConfigManager
    lateinit var commandManager: CommandManager
    lateinit var moduleManager: ModuleManager
    lateinit var fontManager: FontManager
    lateinit var fileManager: FileManager

    fun init() {
        ClientUtils.logInfo("Initialize $name v$version")
        eventManager = EventManager()
    }

    fun load() {
        ClientUtils.logInfo("Loading $name v$version")
        ClientUtils.setTitle("Loading Client...")

        fileManager = FileManager()

        configManager = ConfigManager()
        eventManager.registerListener(configManager)

        commandManager = CommandManager()

        moduleManager = ModuleManager()
        eventManager.registerListener(moduleManager)

        fontManager = FontManager()
        fontManager.loadFonts()

        ClientUtils.disableFastRender()

        configManager.loadDefault()

        ClientUtils.setTitle(null)
    }

    fun shutdown() {
        configManager.save()
    }
}
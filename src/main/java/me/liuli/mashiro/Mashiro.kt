package me.liuli.mashiro

import me.liuli.mashiro.command.CommandManager
import me.liuli.mashiro.config.ConfigManager
import me.liuli.mashiro.config.FileManager
import me.liuli.mashiro.event.EventManager
import me.liuli.mashiro.gui.alt.AltManager
import me.liuli.mashiro.gui.client.GuiLoadingClient
import me.liuli.mashiro.gui.font.FontManager
import me.liuli.mashiro.module.ModuleManager
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.client.ClientUtils

object Mashiro : MinecraftInstance() {
    @JvmStatic
    val name="Mashiro"
    @JvmStatic
    val coloredName="§eM§fashiro"
    @JvmStatic
    val version="0.0.2"
    @JvmStatic
    val author="Liulihaocai"

    lateinit var eventManager: EventManager
    lateinit var configManager: ConfigManager
    lateinit var commandManager: CommandManager
    lateinit var moduleManager: ModuleManager
    lateinit var fontManager: FontManager
    lateinit var altManager: AltManager
    lateinit var fileManager: FileManager

    fun init(){
        ClientUtils.logInfo("Initialize $name v$version")
        eventManager = EventManager()
    }

    fun load(){
        val gui=GuiLoadingClient()
        mc.displayGuiScreen(gui)

        ClientUtils.logInfo("Loading $name v$version")
        ClientUtils.setTitle("Loading Client...")

        fileManager = FileManager()

        gui.displayString="config"
        configManager = ConfigManager()
        eventManager.registerListener(configManager)

        gui.displayString="command"
        commandManager = CommandManager()

        gui.displayString="module"
        moduleManager = ModuleManager()
        eventManager.registerListener(moduleManager)

        gui.displayString="font"
        fontManager = FontManager()
        fontManager.loadFonts()

        gui.displayString="renderer"
        ClientUtils.disableFastRender()

        gui.displayString="other things"
        altManager = AltManager()

        configManager.loadDefault()

        ClientUtils.setTitle(null)

        gui.ok=true
    }

    fun shutdown(){
        configManager.save()
    }
}
package me.liuli.mashiro.module.modules.client

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.EmptyGuiScreen
import me.liuli.mashiro.gui.ultralight.page.Page
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import org.lwjgl.input.Keyboard

class ClickGUI : Module("ClickGUI","display a clickgui",ModuleCategory.CLIENT,keyBind = Keyboard.KEY_RSHIFT,canToggle = false) {
    override fun onEnable() {
        val screen=EmptyGuiScreen()
        val view=Mashiro.ultralightManager.newScreenView(screen)
        view.loadPage(Page("test"))
        Mashiro.ultralightManager.addView(view)
        mc.displayGuiScreen(screen)
    }
}
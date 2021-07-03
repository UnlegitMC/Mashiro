package me.liuli.mashiro.gui.ultralight.page

import me.liuli.mashiro.gui.ultralight.RenderLayer
import net.minecraft.client.gui.GuiScreen

class ScreenView(viewRenderer: ViewRenderer, val screen: GuiScreen, val adaptedScreen: GuiScreen?, val parentScreen: GuiScreen?) : View(RenderLayer.SCREEN_LAYER, viewRenderer)
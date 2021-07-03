package me.liuli.mashiro.gui.client

import me.liuli.mashiro.Mashiro.coloredName
import me.liuli.mashiro.Mashiro.version
import me.liuli.mashiro.util.client.ClientUtils
import me.liuli.mashiro.util.render.RenderUtils
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiScreen
import java.awt.Color
import java.lang.RuntimeException

class GuiLoadingClient(val err: Throwable? = null) : GuiScreen() {
    var displayString=""
    var ok=false

    init {
        if(err!=null){
            ClientUtils.setTitle("Error :(")
        }
    }

    override fun drawDefaultBackground() {
        RenderUtils.drawRect(0f,0f,width.toFloat(),height.toFloat(), Color.DARK_GRAY)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if(ok){
            mc.displayGuiScreen(GuiMainMenu())
            return
        }

        drawDefaultBackground()

        // render client brand
        fontRendererObj.drawString("$coloredName v$version", (width-fontRendererObj.getStringWidth("$coloredName v$version")).toFloat(), (height - fontRendererObj.FONT_HEIGHT).toFloat(), 16777215, false)

        if(err==null){
            drawCenteredString(mc.fontRendererObj, "Please wait a moment, the client is loading...", width / 2, height / 2 - 40, Color.GREEN.rgb)
            if(displayString.isNotEmpty()){
                drawCenteredString(mc.fontRendererObj, "Loading $displayString", width / 2, height / 2 + 40, Color.WHITE.rgb)
            }
        }else{
            drawCenteredString(mc.fontRendererObj, "An error occurred while loading the client", width / 2, height / 2 - 40, Color.RED.rgb)
            drawCenteredString(mc.fontRendererObj, err.javaClass.name, width / 2, height / 2 + 40, Color.WHITE.rgb)
            if(err.localizedMessage!=null)
                drawCenteredString(mc.fontRendererObj, err.localizedMessage, width / 2, height / 2 + 50, Color.WHITE.rgb)
        }
    }
}
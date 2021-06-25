package me.liuli.mashiro.module.modules.render

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.Render2DEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import me.liuli.mashiro.module.value.BoolValue
import me.liuli.mashiro.module.value.FloatValue
import me.liuli.mashiro.module.value.IntValue
import me.liuli.mashiro.util.render.ColorUtils

class HUD : Module("HUD","Display hud of the client", ModuleCategory.RENDER) {
    @EventMethod
    private fun onRender2d(event: Render2DEvent){
        mc.fontRendererObj.drawString("Mashiro HUD Test",10,10,ColorUtils.mashiroRainbow(1).rgb)
    }
}
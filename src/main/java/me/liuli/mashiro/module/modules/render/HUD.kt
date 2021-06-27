package me.liuli.mashiro.module.modules.render

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.Render2DEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import me.liuli.mashiro.util.render.ColorUtils
import me.liuli.mashiro.util.render.EaseUtils
import me.liuli.mashiro.util.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import java.awt.Color

class HUD : Module("HUD","Display hud of the client", ModuleCategory.RENDER, defaultOn = true) {
    private var lastUpdate = System.currentTimeMillis()

    @EventMethod
    fun onRender2d(event: Render2DEvent){
        val time = System.currentTimeMillis()
        val pct = (time - lastUpdate) / 800.0
        lastUpdate=time
        val sr=ScaledResolution(mc)
        val fontRenderer=Mashiro.fontManager.font
        val fontHeight=10f

        fontRenderer.renderString("M",10f,10f,ColorUtils.mashiroRainbow(1),15f)
        fontRenderer.renderString("ashiro",10+fontRenderer.getStringWidth("M",15f),10f, Color.WHITE,15f)

        var index=0
        val blank=fontHeight*0.5f
        GL11.glPushMatrix()
        GL11.glTranslatef(sr.scaledWidth.toFloat(),0f,0f)
        val modules=Mashiro.moduleManager.modules.filter { (it.state||it.animate!=0.0)&&it.array }
            .sortedBy { -fontRenderer.getStringWidth(it.name) }
        modules.forEach { module ->
            if(module.state){
                module.animate=1.0.coerceAtMost(module.animate + pct)
            }else{
                module.animate=0.0.coerceAtLeast(module.animate - pct)
            }
            val color=ColorUtils.mashiroRainbow(index+1)
            GL11.glPushMatrix()
            val width=fontRenderer.getStringWidth(module.name,fontHeight)+blank*2
            val height=fontHeight+blank
            GL11.glTranslated(-width*if(module.state){ EaseUtils.easeOutCubic(module.animate) }
                else{ EaseUtils.easeInCubic(module.animate) },0.0,0.0)
            RenderUtils.drawRect(0f,0f,width,height,ColorUtils.darker(ColorUtils.reAlpha(color,130),0.25f))
            fontRenderer.renderString(module.name,blank,blank*0.6f,color,fontHeight)
            //draw outline
            val nextWidth=try {
                fontRenderer.getStringWidth(modules[modules.indexOf(module)+1].name,fontHeight)+blank*2f-1
            }catch (e: IndexOutOfBoundsException){
                0f
            }
            RenderUtils.drawRect(0f,height-1,width-nextWidth,height,color)
            RenderUtils.drawRect(0f,0f,1f,height,color)
            GL11.glPopMatrix()
            GL11.glTranslatef(0f,fontHeight+blank,0f)
            index++
        }
        GL11.glPopMatrix()
    }
}
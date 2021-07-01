package me.liuli.mashiro.gui.alt

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.render.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.io.File
import java.util.*

class AltManager : MinecraftInstance() {
    private val skins=HashMap<UUID,AltSkin>()
    val altCachePath=File(mc.mcDataDir,".cache/Mashiro/alt")

    init {
        if(!altCachePath.exists())
            altCachePath.mkdirs()
    }

    fun drawNowAltOnMenu(){
        val fontRenderer=Mashiro.fontManager.font
        val uuid=mc.session.profile.id

        GL11.glPushMatrix()
        GL11.glTranslatef(7f,7f,0f)

        RenderUtils.drawRect(0f,0f,20f+fontRenderer.getStringWidth(mc.session.username,17f).coerceAtLeast(50f)+4f,20f, Color.DARK_GRAY)

        fontRenderer.renderString(mc.session.username,20f,2f,Color.WHITE,17f)

        if(skins.containsKey(uuid)){
            val skin=skins[uuid]
            if(skin?.resourceLocation != null){
                RenderUtils.drawHead(skin.resourceLocation,2,2,16,16)
            }
        }else{
            skins[uuid] = AltSkin(uuid)
        }

        GL11.glPopMatrix()
    }
}
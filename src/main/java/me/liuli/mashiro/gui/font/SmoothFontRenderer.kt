package me.liuli.mashiro.gui.font

import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * @author Liulihaocai
 * render AWT font in minecraft
 */
class SmoothFontRenderer(private val font: Font, private val doNormalInit: Boolean = true, val defaultHeight: Float = mc.fontRendererObj.FONT_HEIGHT.toFloat()) : MinecraftInstance() {
    private lateinit var fontMetrics: FontMetrics

    private val chars=HashMap<Char, FontChar>()

    init {
        initFontMertics()

        // 先把英文渲染好,其他的被动渲染
        prepareCharImages('0','9')
        prepareCharImages('a','z')
        prepareCharImages('A','Z')
    }

    private fun putHints(graphics: Graphics2D){
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    private fun initFontMertics(){
        val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).graphics as Graphics2D

        putHints(graphics)
        graphics.font = font

        fontMetrics = graphics.fontMetrics
    }

    private fun loadChar(char: Char): FontChar {
        val fc=renderCharImage(char)
        chars[char] = fc
        return fc
    }

    private fun renderCharImage(char: Char): FontChar {
        var charWidth = fontMetrics.charWidth(char) + 8
        if (charWidth <= 0)
            charWidth = 7

        var charHeight = fontMetrics.height + 3
        if (charHeight <= 0)
            charHeight = font.size

        val fontImage = BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = fontImage.graphics as Graphics2D
        putHints(graphics)
        graphics.font = font
        graphics.color = Color.WHITE
        graphics.drawString(char.toString(), 3, 1 + fontMetrics.ascent)

        return FontChar(char, fontImage)
    }

    private fun prepareCharImages(start: Char, stop: Char){
        val startAscii=start.toInt().coerceAtMost(stop.toInt())
        val stopAscii=stop.toInt().coerceAtLeast(start.toInt())

        for (ascii in startAscii until stopAscii){
            loadChar(ascii.toChar())
        }
    }

    fun getStringWidth(text: String, height: Float = defaultHeight):Float{
        var width=0f

        for(char in text.toCharArray()){
            width+=getCharWidth(char, height)
        }

        return width
    }

    fun getCharWidth(char: Char, height: Float = defaultHeight):Float{
        val fontChar=chars[char] ?: loadChar(char)
        return fontChar.width*(height/fontChar.height)
    }

    fun getCharImageWidth(char: Char) = (chars[char] ?: loadChar(char)).width

    /**
     * @return width of the string
     */
    fun renderString(text: String, x: Float, y: Float, color: Color, height: Float = defaultHeight):Float{
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)

        RenderUtils.glColor(color)

        var xPos=0f
        GL11.glTranslatef(x,y,0f)

        for(char in text.toCharArray()){
            val singleWidth=renderChar(char,0f,0f,height)
            GL11.glTranslatef(singleWidth,0f,0f)
            xPos+=singleWidth
        }

        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glPopMatrix()

        return xPos
    }

    /**
     * @return width
     */
    fun renderChar(char: Char, x: Float, y: Float, height: Float = defaultHeight):Float{
        val fontChar=chars[char] ?: loadChar(char)
        val width=fontChar.width*(height/fontChar.height)
        val f = 1.0f / width
        val f1 = 1.0f / height

        mc.textureManager.bindTexture(fontChar.resourceLoc)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        worldrenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0)
            .tex(0.0, (height * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0)
            .tex((width * f).toDouble(), (height * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), y.toDouble(), 0.0)
            .tex((width * f).toDouble(), 0.0).endVertex()
        worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0)
            .tex(0.0, 0.0).endVertex()
        tessellator.draw()

        return width
    }

    class FontChar(val char: Char, val bufImg: BufferedImage){
        val resourceLoc = ResourceLocation("mashiro/font/char-${char.toInt()}")
        val width=bufImg.width
        val height=bufImg.height

        init {
            Minecraft.getMinecraft().textureManager.loadTexture(resourceLoc, DynamicTexture(bufImg))
        }
    }
}
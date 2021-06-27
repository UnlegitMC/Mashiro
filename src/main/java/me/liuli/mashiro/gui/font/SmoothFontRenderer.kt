package me.liuli.mashiro.gui.font

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.ClientUtils
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
 *
 * @param font AWT字体
 * @param doNormalInit 预渲染英文字符，如果是图标字体填false
 * @param defaultHeight 默认字体高度
 * @param enableCache 使用字体缓存
 */
class SmoothFontRenderer(private val font: Font, private val doNormalInit: Boolean = true,
                         val defaultHeight: Float = mc.fontRendererObj.FONT_HEIGHT.toFloat(), val enableCache: Boolean = true) : MinecraftInstance() {
    private lateinit var fontMetrics: FontMetrics

    private val chars=HashMap<Char, FontChar>()
    private val cacheDir=File(Mashiro.fontManager.fontCacheDir,"${font.fontName.toLowerCase().replace(" ","_")}-${font.size}")

    init {
        val loadTime=System.currentTimeMillis()

        initFontMertics()

        if(enableCache&&!cacheDir.exists())
            cacheDir.mkdirs()

        if(doNormalInit){
            // 先把英文渲染好,其他的被动渲染
            loadChar(' ')
            prepareCharImages('0','9')
            prepareCharImages('a','z')
            prepareCharImages('A','Z')
        }

        ClientUtils.logInfo("Font ${font.fontName} loaded in ${System.currentTimeMillis()-loadTime}ms!")
    }

    /**
     * 设置图片渲染hint
     */
    private fun putHints(graphics: Graphics2D){
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    /**
     * 初始化FontMetrics
     * 和LB的每次渲染获取一次比，性能好点？
     */
    private fun initFontMertics(){
        val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).graphics as Graphics2D

        putHints(graphics)
        graphics.font = font

        fontMetrics = graphics.fontMetrics
    }

    /**
     * @param char 字符
     * 初始化单个字符图片
     */
    private fun loadChar(char: Char): FontChar {
        val fc=if(enableCache){
            loadCharImageFromCache(char)
        }else{
            renderCharImage(char)
        }
        chars[char] = fc
        return fc
    }

    /**
     * 从本地缓存读取字符图片
     * 没有则渲染
     */
    private fun loadCharImageFromCache(char: Char): FontChar {
        val charImageFile=getCharCacheFile(char)
        return if(charImageFile.exists()){
            FontChar(char, ImageIO.read(charImageFile))
        }else{
            saveFontCharToCache(renderCharImage(char))
        }
    }

    /**
     * 将渲染好的FontChar保存至缓存
     * @return 传入的FontChar
     */
    private fun saveFontCharToCache(fontChar: FontChar): FontChar {
        ImageIO.write(fontChar.bufImg,"png",getCharCacheFile(fontChar.char))
        return fontChar
    }

    /**
     * 获取char对应的缓存文件
     */
    private fun getCharCacheFile(char: Char): File {
        return File(cacheDir,"char-${char.toInt()}.png")
    }

    /**
     * 渲染字符图片
     */
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

    /**
     * @param start 开始字符
     * @param stop 结束字符
     * 如果需要初始化单个直接loadChar(char)就行
     * 预初始化字符图片
     */
    private fun prepareCharImages(start: Char, stop: Char){
        val startAscii=start.toInt().coerceAtMost(stop.toInt())
        val stopAscii=stop.toInt().coerceAtLeast(start.toInt())

        for (ascii in startAscii until stopAscii){
            loadChar(ascii.toChar())
        }
    }

    /**
     * 获取字符串会渲染成的宽度
     */
    fun getStringWidth(text: String, height: Float = defaultHeight):Float{
        var width=0f

        for(char in text.toCharArray()){
            width+=getCharWidth(char, height)
        }

        return width
    }

    /**
     * 获取字符宽度
     */
    fun getCharWidth(char: Char, height: Float = defaultHeight):Float{
        val fontChar=chars[char] ?: loadChar(char)
        return fontChar.width*(height/fontChar.height)
    }

    /**
     * 获取字符图片宽度
     */
    fun getCharImageWidth(char: Char) = (chars[char] ?: loadChar(char)).width

    /**
     * @param text 字符串
     * @param color 颜色，字符串里不支持MC颜色码
     * @param height 字符高度，宽度会自动按比例缩放
     *
     * @return 字符串宽度
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
     * @param char 字符
     * @param height 字符高度，宽度会自动按比例缩放
     *
     * @return 单个字符宽度
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

    /**
     * @param char 对应的字符
     * @param bufImg 渲染出的字符图片
     */
    class FontChar(val char: Char, val bufImg: BufferedImage){
        val resourceLoc = ResourceLocation("mashiro/font/char-${char.toInt()}")
        val width=bufImg.width
        val height=bufImg.height

        init {
            Minecraft.getMinecraft().textureManager.loadTexture(resourceLoc, DynamicTexture(bufImg))
        }
    }
}
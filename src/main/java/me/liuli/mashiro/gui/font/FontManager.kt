package me.liuli.mashiro.gui.font

import me.liuli.mashiro.Mashiro
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer
import net.minecraft.client.Minecraft
import org.apache.commons.io.IOUtils
import java.awt.Font
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class FontManager {
    lateinit var font: GameFontRenderer
    private val fontFile = File(Mashiro.configManager.rootPath, "font.ttf")

    init {
        if (!fontFile.exists()) {
            IOUtils.copy(this.javaClass.classLoader.getResourceAsStream("res/verdana-bold.ttf"), FileOutputStream(fontFile))
        }
    }

    fun loadFonts() {
        font = GameFontRenderer(getFont(35))
    }

    private fun getFont(size: Int): Font {
        return try {
            val inputStream: InputStream = FileInputStream(fontFile)
            var awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream)
            inputStream.close()
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size.toFloat())
            awtClientFont
        } catch (e: Exception) {
            e.printStackTrace()
            Font("default", Font.PLAIN, size)
        }
    }
}
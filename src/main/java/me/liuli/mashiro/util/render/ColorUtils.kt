package me.liuli.mashiro.util.render

import java.awt.Color
import kotlin.math.abs

object ColorUtils {
    private val startTime=System.currentTimeMillis()

    fun mashiroRainbow(index: Int,lowest: Float=0.07f,bigest: Float=0.6f,indexOffset: Int=500):Color{
        return Color.getHSBColor((abs(((((System.currentTimeMillis()-startTime).toInt()+index*indexOffset)/5000f)%2)-1)*(bigest-lowest))+lowest,0.7f,1f)
    }

    fun rainbow(offset: Long):Color{
        return Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 1F, 1F))
    }
}
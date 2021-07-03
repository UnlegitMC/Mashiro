package me.liuli.mashiro.gui.ultralight.page

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface
import com.labymedia.ultralight.config.UltralightViewConfig
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.nio.ByteBuffer

/**
 * A cpu renderer which is being supported on OpenGL functionality from version 1.2.
 * @author CCBlueX
 */
class CPUViewRenderer : ViewRenderer {

    private var glTexture = -1

    override fun setupConfig(viewConfig: UltralightViewConfig) {
        // CPU rendering is not accelerated
        // viewConfig.isAccelerated(false)
    }

    override fun delete() {
        GL11.glDeleteTextures(glTexture)
        glTexture = -1
    }

    /**
     * Render the current view
     */
    override fun render(view: UltralightView) {
        if (glTexture == -1) {
            // create a gl texture
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            glTexture = GL11.glGenTextures()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
        }

        // As we are using the CPU renderer, draw with a bitmap (we did not set a custom surface)
        val surface = view.surface() as UltralightBitmapSurface
        val bitmap = surface.bitmap()
        val width = view.width().toInt()
        val height = view.height().toInt()

        // Prepare OpenGL for 2D textures and bind our texture
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture)

        val dirtyBounds = surface.dirtyBounds()

        if (dirtyBounds.isValid) {
            val imageData = bitmap.lockPixels()

            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0)
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0)
            GL11.glPixelStorei(GL12.GL_UNPACK_SKIP_IMAGES, 0)
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, bitmap.rowBytes().toInt() / 4)

            if (dirtyBounds.width() == width && dirtyBounds.height() == height) {
                // Update full image
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, imageData)
            } else {
                // Update partial image
                val x = dirtyBounds.x()
                val y = dirtyBounds.y()
                val dirtyWidth = dirtyBounds.width()
                val dirtyHeight = dirtyBounds.height()
                val startOffset = (y * bitmap.rowBytes() + x * 4).toInt()

                GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    x, y, dirtyWidth, dirtyHeight,
                    GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                    imageData.position(startOffset) as ByteBuffer
                )
            }
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0)

            bitmap.unlockPixels()
            surface.clearDirtyBounds()
        }

        // Set up the OpenGL state for rendering of a fullscreen quad
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT or GL11.GL_COLOR_BUFFER_BIT or GL11.GL_TRANSFORM_BIT)
        GL11.glMatrixMode(GL11.GL_PROJECTION)
        GL11.glPushMatrix()
        GL11.glLoadIdentity()
        GL11.glOrtho(0.0, view.width().toDouble(), view.height().toDouble(), 0.0, -1.0, 1.0)
        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glPushMatrix()

        // Disable lighting and scissoring, they could mess up th renderer
        GL11.glLoadIdentity()
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        // Make sure we draw with a neutral color
        // (so we don't mess with the color channels of the image)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        GL11.glBegin(GL11.GL_QUADS)

        // Lower left corner, 0/0 on the screen space, and 0/0 of the image UV
        GL11.glTexCoord2f(0f, 0f)
        GL11.glVertex2f(0f, 0f)

        // Upper left corner
        GL11.glTexCoord2f(0f, 1f)
        GL11.glVertex2i(0, height)

        // Upper right corner
        GL11.glTexCoord2f(1f, 1f)
        GL11.glVertex2i(width, height)

        // Lower right corner
        GL11.glTexCoord2f(1f, 0f)
        GL11.glVertex2i(width, 0)
        GL11.glEnd()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)

        // Restore OpenGL state
        GL11.glPopMatrix()
        GL11.glMatrixMode(GL11.GL_PROJECTION)
        GL11.glPopMatrix()
        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glPopAttrib()
    }
}
package me.liuli.mashiro.gui.ultralight.adaptor

import com.labymedia.ultralight.plugin.clipboard.UltralightClipboard
import net.minecraft.client.gui.GuiScreen

class ClipBoardAdaptor : UltralightClipboard {
    override fun clear() {
        GuiScreen.setClipboardString("")
    }

    override fun readPlainText(): String {
        return GuiScreen.getClipboardString()
    }

    override fun writePlainText(text: String) {
        GuiScreen.setClipboardString(text)
    }
}
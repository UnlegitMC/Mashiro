package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory

import net.minecraft.client.settings.GameSettings
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.client.gui.GuiChat

// Not compiled, built by BRAIN-DEVELOPMENT-KIT XD
class InvMove : Module("InvMove", "Allows you to walk while opening the inventory.", ModuleCategory.MOVEMENT) {

    @EventMethod
    fun onUpdate(event: UpdateEvent) {
         if (mc.currentScreen !is GuiChat && mc.currentScreen !is GuiIngameMenu) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward)
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack)
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight)
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump)
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint)
        }
    }
}

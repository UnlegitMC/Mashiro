package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory

class AntiAFK : Module("AntiAFK", "Prevent being kicked out in AFK", ModuleCategory.MISC) {

    private var playerRotationYaw = 0f

    @EventMethod
    fun onUpdate(event: UpdateEvent) {
        mc.gameSettings.keyBindForward.pressed = true
        playerRotationYaw += 2f
        mc.thePlayer.rotationYaw = playerRotationYaw % 360f
    }
}

package me.liuli.mashiro.util.move

import me.liuli.mashiro.util.MinecraftInstance

object MovementUtils : MinecraftInstance() {
    @JvmStatic
    fun isMoving(): Boolean {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)
    }
}
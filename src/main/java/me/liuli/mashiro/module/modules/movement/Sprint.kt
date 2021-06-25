package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import me.liuli.mashiro.module.value.BoolValue
import me.liuli.mashiro.util.move.MovementUtils
import net.minecraft.potion.Potion

class Sprint : Module("Sprint","Automatically make you sprint", ModuleCategory.MOVEMENT) {
    private val allDirectionValue=BoolValue("AllDirections",false)

    @EventMethod
    fun onUpdate(event: UpdateEvent){
        if(MovementUtils.isMoving()&&!mc.thePlayer.isPotionActive(Potion.blindness)&&(mc.thePlayer.foodStats.foodLevel > 6.0f || mc.thePlayer.capabilities.allowFlying)){
            if(mc.gameSettings.keyBindForward.isPressed||allDirectionValue.get())
                mc.thePlayer.isSprinting=true
        }
    }
}
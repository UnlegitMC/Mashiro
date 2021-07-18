package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import net.minecraft.potion.Potion

class LegitSpeed : Module("LegitSpeed","Automatically make you fast", ModuleCategory.MOVEMENT) {
    
    @EventMethod
    fun onUpdate(event: UpdateEvent){
        // Force sprint
        if(!mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.foodStats.foodLevel > 6.0f && !mc.thePlayer.isSneaking()){
            if(mc.thePlayer.movementInput.moveForward==1.0){// It is FLOAT? Replace to 1.0f
                mc.thePlayer.isSprinting=true;
            }
        }
        
        if(mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()){
            mc.thePlayer.jump();
        }
        
    }
}

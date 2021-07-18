package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import me.liuli.mashiro.module.value.BoolValue
import net.minecraft.potion.Potion

class Sprint : Module("Sprint","Automatically make you sprint", ModuleCategory.MOVEMENT) {
    //private val allDirectionValue=BoolValue("AllDirections",false);
    private val legitMode=BoolValue("LegitMode",true);
    
    @EventMethod
    fun onUpdate(event: UpdateEvent){
        
        if(legitMode.get()){
            if(!mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.foodStats.foodLevel > 6.0f && !mc.thePlayer.isSneaking()){
                if(mc.thePlayer.movementInput.moveForward==1.0){// It is FLOAT? Replace to 1.0f
                    mc.thePlayer.isSprinting=true;
                }
            }
        }else{
            mc.thePlayer.isSprinting=true;
        }
        
    }
}

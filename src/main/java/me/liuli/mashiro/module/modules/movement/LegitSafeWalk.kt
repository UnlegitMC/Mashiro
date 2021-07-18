package me.liuli.mashiro.module.modules.movement

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory

import net.minecraft.util.BlockPos
import net.minecraft.init.Blocks
import net.minecraft.client.settings.GameSettings



class LegitSafeWalk : Module("LegitSafeWalk","Automatically make you walk safely", ModuleCategory.MOVEMENT) {
    
    @EventMethod
    fun onUpdate(event: UpdateEvent){
        var underBlock=mc.theWorld.getBlockState(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).block;
        mc.gameSettings.keyBindSneak.pressed=(underBlock == Blocks.air);
    }
}

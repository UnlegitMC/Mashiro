package me.liuli.mashiro.module.modules.combat

import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.UpdateEvent
import me.liuli.mashiro.module.Module
import me.liuli.mashiro.module.ModuleCategory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C0APacketAnimation

class UnbypassableAura : Module("UnbypassableAura","KillAura like bedrock", ModuleCategory.COMBAT) {
    @EventMethod
    fun onUpdate(event: UpdateEvent){
        for(entity in mc.theWorld.loadedEntityList){
            if((!entity.equals(mc.thePlayer))&&entity is EntityPlayer&&mc.thePlayer.getDistanceSqToEntity(entity)<8){
                mc.netHandler.addToSendQueue(C02PacketUseEntity(entity,C02PacketUseEntity.Action.ATTACK))
                mc.netHandler.addToSendQueue(C0APacketAnimation())
            }
        }
    }
}
package me.liuli.mashiro.event

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.minecraft.network.Packet

class UpdateEvent : Event()

class AttackEvent(val targetEntity: Entity) : Event()

class WorldEvent(val worldClient: WorldClient?) : Event()

class ScreenEvent(val guiScreen: GuiScreen?) : Event()

class PacketEvent(val packet: Packet<*>, val type: Type) : EventCancellable() {
    enum class Type {
        RECEIVE,
        SEND
    }
}

class Render2DEvent(val partialTicks: Float) : Event()

class Render3DEvent(val partialTicks: Float) : Event()
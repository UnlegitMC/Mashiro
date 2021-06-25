package me.liuli.mashiro.util.world

import net.minecraft.block.Block

object BlockUtils {
    @JvmStatic
    fun getBlockName(id: Int): String = Block.getBlockById(id).localizedName
}
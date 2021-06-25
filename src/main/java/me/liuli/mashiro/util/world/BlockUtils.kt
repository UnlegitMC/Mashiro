package me.liuli.mashiro.util.world

import me.liuli.mashiro.util.MinecraftInstance
import net.minecraft.block.Block
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3

object BlockUtils : MinecraftInstance() {
    @JvmStatic
    fun getBlockName(id: Int): String = Block.getBlockById(id).localizedName

    @JvmStatic
    fun getBlock(vec3: Vec3?): Block? = getBlock(BlockPos(vec3))

    @JvmStatic
    fun getBlock(blockPos: BlockPos?): Block? = mc.theWorld?.getBlockState(blockPos)?.block
}
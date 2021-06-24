package me.liuli.mashiro.command

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.ClientUtils
import me.liuli.mashiro.util.MinecraftInstance

abstract class Command(val command: String, val description: String) : MinecraftInstance() {
    abstract fun exec(args: Array<String>)

    open fun tabComplete(args: Array<String>): List<String> {
        return emptyList()
    }

    protected fun chat(msg: String) = ClientUtils.displayAlert(msg)

    protected fun chatSyntax(syntax: String) = ClientUtils.displayAlert("Syntax: ${Mashiro.commandManager.prefix}$syntax")
}
package me.liuli.mashiro.command

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.util.MinecraftInstance
import me.liuli.mashiro.util.client.ClientUtils

abstract class Command(val command: String, val description: String, val subCommand: Array<String> = emptyArray()) : MinecraftInstance() {
    abstract fun exec(args: Array<String>)

    open fun tabComplete(args: Array<String>): List<String> {
        return emptyList()
    }

    protected fun chat(msg: String) = ClientUtils.displayAlert(msg)

    protected fun chatSyntax(syntax: String) = ClientUtils.displayAlert("Syntax: ${Mashiro.commandManager.prefix}$command $syntax")
}
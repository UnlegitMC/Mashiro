package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command
import me.liuli.mashiro.util.ClientUtils

class HelpCommand : Command("help","Show the list of commands") {
    override fun exec(args: Array<String>) {
        val commands=Mashiro.commandManager.commands.filter { it != this }
        chat("${Mashiro.name} Client Commands(${commands.size}):")
        commands.forEach {
            ClientUtils.displayChatMessage(" ${Mashiro.commandManager.prefix}${it.command} - ${it.description}")
        }
    }
}
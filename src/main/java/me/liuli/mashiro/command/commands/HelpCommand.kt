package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command
import me.liuli.mashiro.module.ModuleCommand
import me.liuli.mashiro.util.client.ClientUtils

class HelpCommand : Command("help", "Show the list of commands") {
    override fun exec(args: Array<String>) {
        val commands = Mashiro.commandManager.commands.filter {
            val command = it.value
            command != this && command !is ModuleCommand
        }
        chat("${Mashiro.name} Client Commands(${commands.size}):")
        commands.forEach {
            val command = it.value
            ClientUtils.displayChatMessage(" ${Mashiro.commandManager.prefix}${command.command} - ${command.description}")
        }
    }
}
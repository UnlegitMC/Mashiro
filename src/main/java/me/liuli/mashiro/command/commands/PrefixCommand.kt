package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command

class PrefixCommand : Command("prefix", "Change the command prefix") {
    override fun exec(args: Array<String>) {
        if (args.size == 1) {
            Mashiro.commandManager.prefix = args[0]
            chat("Command prefix successfully changed to '${args[0]}'")
        } else {
            chatSyntax("<prefix>")
        }
    }
}
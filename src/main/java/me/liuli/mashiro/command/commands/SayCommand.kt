package me.liuli.mashiro.command.commands

import me.liuli.mashiro.command.Command
import me.liuli.mashiro.util.StringUtils

class SayCommand : Command("say", "Allows you to say something without change the prefix") {
    override fun exec(args: Array<String>) {
        if (args.size > 1) {
            mc.thePlayer.sendChatMessage(StringUtils.toCompleteString(args, 1))
            chat("Your message was successfully sent to the chat.")
            return
        }
        chatSyntax("say <message>")
    }
}
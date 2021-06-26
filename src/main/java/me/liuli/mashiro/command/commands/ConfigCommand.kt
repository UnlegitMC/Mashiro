package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command

class ConfigCommand : Command("config","Manage configs of the client") {
    private val rootSyntax="<load/save/reload>"

    override fun exec(args: Array<String>) {
        if(args.isEmpty()) {
            chatSyntax(rootSyntax)
            return
        }

        when(args[0].toLowerCase()){
            "load" -> {
                if(args.size>1){
                    Mashiro.configManager.load(args[1])
                }else{
                    chatSyntax("load <config name>")
                }
            }

            "save" -> {
                Mashiro.configManager.save()
                chat("Config §l${Mashiro.configManager.nowConfig}§r saved")
            }

            "reload" -> {
                Mashiro.configManager.reload()
                chat("Config §l${Mashiro.configManager.nowConfig}§r reloaded")
            }

            else -> chatSyntax(rootSyntax)
        }
    }
}
package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command

class ArrayCommand : Command("array", "Toggle display module in hud") {
    override fun exec(args: Array<String>) {
        if (args.isNotEmpty()) {
            val module = Mashiro.moduleManager.getModule(args[0])

            if (module == null) {
                chat("Module §l${args[0]}§r not found.")
                return
            }

            if (args.size> 1) {
                when (args[1].lowercase()) {
                    "on", "true" -> module.array = true
                    "off", "false" -> module.array = false
                    else -> module.array = !module.array
                }
            } else {
                module.array = !module.array
            }
            chat("Set module §l${module.name}§r array stat to §l${module.array}§r.")

            return
        }

        chatSyntax("<module> <on/off>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        val moduleName = args[0]

        return when (args.size) {
            1 -> Mashiro.moduleManager.modules
                .map { it.name }
                .filter { it.startsWith(moduleName, true) }
                .toList()
            else -> emptyList()
        }
    }
}
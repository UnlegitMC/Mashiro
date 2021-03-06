package me.liuli.mashiro.command.commands

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.command.Command
import org.lwjgl.input.Keyboard

class BindCommand : Command("bind", "Manage your module binds", arrayOf("b")) {
    override fun exec(args: Array<String>) {
        if (args.isNotEmpty()) {
            // Get module by name
            val module = Mashiro.moduleManager.getModule(args[0])

            if (module == null) {
                chat("Module §l" + args[0] + "§r not found.")
                return
            }

            if (args.size> 1) {
                // Find key by name and change
                val key = Keyboard.getKeyIndex(args[1].uppercase())
                module.keyBind = key

                // Response to user
                chat("Bound module §l${module.name}§r to key §l${Keyboard.getKeyName(key)}§r.")
            } else {
                Mashiro.moduleManager.pendKeyBind(module)
            }

            return
        }

        chatSyntax("<module> <key/NONE>")
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
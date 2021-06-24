package me.liuli.mashiro.command

import me.liuli.mashiro.util.ClientUtils
import org.reflections.Reflections

class CommandManager {
    var prefix="."
    val commands = mutableListOf<Command>()
    var latestAutoComplete: Array<String> = emptyArray()

    init {
        val reflections = Reflections("${this.javaClass.`package`.name}.commands")
        val subTypes: Set<Class<out Command>> = reflections.getSubTypesOf(Command::class.java)
        for (theClass in subTypes) {
            try {
                registerCommand(theClass.newInstance())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun handleCommand(msg: String){
        val input=if(msg.startsWith(prefix)){
            msg
        }else{
            prefix+msg
        }
        val args = input.split(" ").toTypedArray()

        for (command in commands) {
            if (args[0].equals(prefix + command.command, ignoreCase = true)) {
                command.exec(args)
                return
            }
        }

        ClientUtils.displayAlert("Command not found. Type ${prefix}help to view all commands.")
    }

    fun registerCommand(command: Command) = commands.add(command)

    /**
     * Updates the [latestAutoComplete] array based on the provided [input].
     *
     * @param input text that should be used to check for auto completions.
     * @author NurMarvin
     */
    fun autoComplete(input: String): Boolean {
        this.latestAutoComplete = this.getCompletions(input) ?: emptyArray()
        return input.startsWith(this.prefix) && this.latestAutoComplete.isNotEmpty()
    }

    /**
     * Returns the auto completions for [input].
     *
     * @param input text that should be used to check for auto completions.
     * @author NurMarvin
     */
    private fun getCompletions(input: String): Array<String>? {
        if (input.isNotEmpty() && input.toCharArray()[0] == this.prefix.toCharArray()[0]) {
            val args = input.split(" ")

            return if (args.size > 1) {
                val command = getCommand(args[0].substring(1))
                val tabCompletions = command?.tabComplete(args.drop(1).toTypedArray())

                tabCompletions?.toTypedArray()
            } else {
                val rawInput = input.substring(1)
                commands.filter { it.command.startsWith(rawInput, true) }
                    .map {
                        this.prefix + it.command
                    }.toTypedArray()
            }
        }
        return null
    }

    private fun getCommand(name: String): Command? {
        return commands.find { it.command.equals(name, ignoreCase = true) }
    }
}
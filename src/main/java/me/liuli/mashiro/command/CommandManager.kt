package me.liuli.mashiro.command

import me.liuli.mashiro.util.client.ClientUtils
import org.reflections.Reflections

class CommandManager {
    val defaultPrefix="."

    var prefix=defaultPrefix
    val commands=HashMap<String,Command>()
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

    fun registerCommand(command: Command){
        commands[command.command.toLowerCase()] = command
        command.subCommand.forEach {
            commands[it.toLowerCase()] = command
        }
    }

    fun getCommand(name: String): Command? {
        return commands[name.toLowerCase()]
    }

    fun handleCommand(msg: String){
        val input=if(msg.startsWith(prefix)){ msg.substring(1) }else{ msg }
        val args = input.split(" ").toTypedArray()
        val command=getCommand(args[0])
        if(command==null){
            ClientUtils.displayAlert("Command not found. Type ${prefix}help to view all commands.")
            return
        }

        try {
            command.exec(args.copyOfRange(1,args.size))
        }catch (e: Exception){
            e.printStackTrace()
            ClientUtils.displayAlert("An error occurred while executing the command($e)")
        }
    }

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
                commands.filter { it.value.command.startsWith(rawInput, true) }
                    .map {
                        this.prefix + it.value.command
                    }.toTypedArray()
            }
        }
        return null
    }
}
package me.liuli.mashiro.module

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.KeyEvent
import me.liuli.mashiro.event.Listener
import org.reflections.Reflections

class ModuleManager : Listener {
    val modules=mutableListOf<Module>()

    init {
        val reflections = Reflections("${this.javaClass.`package`.name}.modules")
        val subTypes: Set<Class<out Module>> = reflections.getSubTypesOf(Module::class.java)
        for (theClass in subTypes) {
            try {
                registerModule(theClass.newInstance())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun registerModule(module: Module){
        modules.add(module)
        Mashiro.eventManager.registerListener(module)

        // module command
        val values=module.getValues()
        println("NAME ${module.name} VAL ${values.size}")
        if(module.command&&values.isNotEmpty()){
            Mashiro.commandManager.registerCommand(ModuleCommand(module, values))
        }
    }

    fun getModule(name: String):Module?{
        modules.forEach {
            if(it.name.equals(name,ignoreCase = true))
                return it
        }
        return null
    }

    @EventMethod
    private fun onKey(event: KeyEvent) {
        modules.filter { it.keyBind == event.key }.forEach { it.toggle() }
    }

    override fun listen() = true
}
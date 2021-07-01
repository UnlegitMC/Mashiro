package me.liuli.mashiro.module

import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.event.EventMethod
import me.liuli.mashiro.event.KeyEvent
import me.liuli.mashiro.event.Listener
import me.liuli.mashiro.util.client.ClientUtils
import org.lwjgl.input.Keyboard
import org.reflections.Reflections

class ModuleManager : Listener {
    val modules=mutableListOf<Module>()

    private var pendingKeyBindModule:Module?=null

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
        if(pendingKeyBindModule!=null){
            pendingKeyBindModule!!.keyBind=event.key
            ClientUtils.displayAlert("Bound module §l${pendingKeyBindModule!!.name}§r to key §l${Keyboard.getKeyName(event.key)}§r.")
            pendingKeyBindModule=null
            return
        }

        modules.filter { it.keyBind == event.key }.forEach { it.toggle() }
    }

    fun pendKeyBind(module: Module){
        pendingKeyBindModule=module
        ClientUtils.displayAlert("Press ANY key to set §l${module.name}§r key bind.")
    }

    override fun listen() = true
}
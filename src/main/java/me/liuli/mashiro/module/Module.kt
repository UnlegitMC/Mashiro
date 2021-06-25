package me.liuli.mashiro.module

import me.liuli.mashiro.event.Listener
import me.liuli.mashiro.module.value.Value
import me.liuli.mashiro.util.ClientUtils
import me.liuli.mashiro.util.MinecraftInstance
import org.lwjgl.input.Keyboard

open class Module(val name: String, val description: String, val category: ModuleCategory, val command: Boolean = true,
                  var keyBind: Int = Keyboard.CHAR_NONE, val canToggle: Boolean = true, val defaultOn: Boolean = false) : MinecraftInstance(),Listener {

    val defaultKeyBind=keyBind

    var state=defaultOn
       set(state){
           if (field == state) return

           if(!canToggle){
               onEnable()
               return
           }
           field = state

           if(state)
               onEnable()
           else
               onDisable()
       }

    open fun onEnable() {}

    open fun onDisable() {}

    // backend
    var animate=0.0

    fun chat(msg: String) = ClientUtils.displayAlert(msg)

    fun toggle(){
        state=!state
    }

    fun getValues() = javaClass.declaredFields.map { field ->
            field.isAccessible = true
            field.get(this)
        }.filterIsInstance<Value<*>>()

    fun getValue(valueName: String) = this.getValues().find { it.name.equals(valueName, ignoreCase = true) }

    override fun listen() = state
}
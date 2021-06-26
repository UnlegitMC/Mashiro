package me.liuli.mashiro.config.sections

import com.google.gson.JsonObject
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.config.ConfigSection
import me.liuli.mashiro.module.Module

class ModuleSection : ConfigSection("module") {
    override fun load(obj: JsonObject?) {
        val json=obj ?: JsonObject()
        val settedModules=mutableListOf<Module>()

        for(elementEntry in json.entrySet()){
            if(!elementEntry.value.isJsonObject)
                continue

            val module=Mashiro.moduleManager.getModule(elementEntry.key) ?: continue
            settedModules.add(module)
            val moduleJson=elementEntry.value.asJsonObject

            if(moduleJson.has("toggle"))
                module.state=moduleJson.get("toggle").asBoolean

            if(moduleJson.has("keybind"))
                module.keyBind=moduleJson.get("keybind").asInt

            if(moduleJson.has("array"))
                module.array=moduleJson.get("array").asBoolean
        }

        Mashiro.moduleManager.modules.filter { !settedModules.contains(it) }
            .forEach { // reset module settings that dont contains in the config
                it.state=it.defaultOn
                it.keyBind=it.defaultKeyBind
                it.array=it.defaultArray
            }
    }

    override fun save(): JsonObject {
        val json=JsonObject()

        Mashiro.moduleManager.modules.forEach {
            val moduleJson=JsonObject()
            moduleJson.addProperty("toggle",it.state)
            moduleJson.addProperty("keybind",it.keyBind)
            moduleJson.addProperty("array",it.array)
            json.add(it.name,moduleJson)
        }

        return json
    }
}
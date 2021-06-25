package me.liuli.mashiro.config.sections

import com.google.gson.JsonObject
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.config.ConfigSection

class CommandSection : ConfigSection("command") {
    override fun load(json: JsonObject?) {
        if(json==null || !json.has("prefix")){
            Mashiro.commandManager.prefix=Mashiro.commandManager.defaultPrefix
        }else{
            Mashiro.commandManager.prefix=json.get("prefix").asString
        }
    }

    override fun save(): JsonObject {
        val json=JsonObject()

        json.addProperty("prefix",Mashiro.commandManager.prefix)

        return json
    }
}
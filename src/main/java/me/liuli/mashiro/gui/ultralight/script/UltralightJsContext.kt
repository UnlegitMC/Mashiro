package me.liuli.mashiro.gui.ultralight.script

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.databind.Databind
import com.labymedia.ultralight.databind.DatabindConfiguration
import com.labymedia.ultralight.javascript.JavascriptContext
import me.liuli.mashiro.Mashiro
import me.liuli.mashiro.gui.ultralight.page.View
import net.minecraft.client.Minecraft

class UltralightJsContext(view: View, ulView: UltralightView) {

    val contextProvider = ViewContextProvider(ulView)
    val databind = Databind(
        DatabindConfiguration
            .builder()
            .contextProviderFactory(ViewContextProvider.Factory(ulView))
            .build()
    )

    // TODO: EVENTS
//    var events = UltralightJsEvents(contextProvider, view)

    fun setupContext(view: View, context: JavascriptContext) {
        val globalContext = context.globalContext
        val globalObject = globalContext.globalObject

        globalObject.setProperty(
            "view",
            databind.conversionUtils.toJavascript(context, view),
            0
        )

        globalObject.setProperty(
            "mashiro",
            databind.conversionUtils.toJavascript(context, Mashiro),
            0
        )

        // todo: minecraft has to be remapped
        globalObject.setProperty(
            "mc",
            databind.conversionUtils.toJavascript(context, Minecraft.getMinecraft()),
            0
        )

//        globalObject.setProperty(
//            "events",
//            databind.conversionUtils.toJavascript(context, events),
//            0
//        )

//        if (view is ScreenView) {
//            globalObject.setProperty(
//                "screen",
//                databind.conversionUtils.toJavascript(context, view.adaptedScreen ?: view.screen),
//                0
//            )
//
//            val parentScreen = view.parentScreen
//
//            if (parentScreen != null) {
//                globalObject.setProperty(
//                    "parentScreen",
//                    databind.conversionUtils.toJavascript(context, view.parentScreen),
//                    0
//                )
//            }
//        }
    }
}
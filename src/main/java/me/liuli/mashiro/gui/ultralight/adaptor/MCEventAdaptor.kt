package me.liuli.mashiro.gui.ultralight.adaptor

import com.labymedia.ultralight.input.*
import me.liuli.mashiro.event.*
import me.liuli.mashiro.gui.ultralight.RenderLayer
import me.liuli.mashiro.gui.ultralight.UltralightManager
import me.liuli.mashiro.gui.ultralight.page.ScreenView
import me.liuli.mashiro.util.MinecraftInstance
import org.lwjgl.input.Mouse

// TODO: KeyEvent,WindowFocus,MouseClick adaptor
class MCEventAdaptor(private val ultralightManager: UltralightManager) : MinecraftInstance(),Listener {
    private var lastMouseX=0
    private var lastMouseY=0

    @EventMethod
    fun onUpdate(event: UpdateEvent){
        ultralightManager.update()
    }

    @EventMethod
    fun onRender2d(event: Render2DEvent){
        ultralightManager.render(RenderLayer.OVERLAY_LAYER)
    }

    @EventMethod
    fun onRenderScreen(event: RenderScreenEvent){
        ultralightManager.render(RenderLayer.SCREEN_LAYER)
        // 开始处理其他事件

        // 鼠标滚轮滑动
        if (Mouse.hasWheel()) {
            val wheel = Mouse.getDWheel()
            if (wheel != 0) { // 没滚轮，只是正常鼠标移动
                val scrollEvent = UltralightScrollEvent()
                    .deltaX(0)
                    .deltaY(wheel * 32)
                    .type(UltralightScrollEventType.BY_PIXEL)

                ultralightManager.getActiveView()?.fireScrollEvent(scrollEvent)
            }
        }

        // 鼠标移动
        if(lastMouseX!=event.mouseX||lastMouseY!=event.mouseY){
            val mouseEvent = UltralightMouseEvent()
                .x((event.mouseX * 1f).toInt())
                .y((event.mouseY * 1f).toInt())
                .type(UltralightMouseEventType.MOVED)
                .button(
                    when {
                        Mouse.isButtonDown(0) -> UltralightMouseEventButton.LEFT
                        Mouse.isButtonDown(2) -> UltralightMouseEventButton.MIDDLE
                        Mouse.isButtonDown(1) -> UltralightMouseEventButton.RIGHT
                        else -> null
                    }
                )

            ultralightManager.getActiveView()?.fireMouseEvent(mouseEvent)
        }
    }

    @EventMethod
    fun onGuiKey(event: GuiKeyEvent){
        val text = event.typedChar.toString()

        val keyEvent = UltralightKeyEvent()
            .type(UltralightKeyEventType.CHAR)
            .text(text)
            .unmodifiedText(text)

        ultralightManager.getActiveView()?.fireKeyEvent(keyEvent)
    }

    @EventMethod
    fun onScreen(event: ScreenEvent){
        val activeView = ultralightManager.getActiveView()
        if (activeView is ScreenView) {
//            activeView.context.events._fireViewClose() TODO: EVENTS
            ultralightManager.removeView(activeView)
        }
    }

    fun onResize(width: Int, height: Int){
        ultralightManager.resize(width.toLong(), height.toLong())
    }

//    fun onMouseClick(mouseX: Int, mouseY: Int, mouseButton: Int){
//
//    }

    override fun listen() = true
}
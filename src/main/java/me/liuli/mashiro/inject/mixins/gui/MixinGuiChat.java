package me.liuli.mashiro.inject.mixins.gui;

import me.liuli.mashiro.Mashiro;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {
    private final Minecraft mc = Minecraft.getMinecraft();

    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private boolean waitingOnAutocomplete;

    @Shadow
    public abstract void onAutocompleteResponse(String[] p_onAutocompleteResponse_1_);

    /**
     * only trust message in KeyTyped to anti some client click check (like old zqat.top)
      */
    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void keyTyped(char typedChar, int keyCode, CallbackInfo callbackInfo) {
        String text=inputField.getText();
        if(text.startsWith(Mashiro.commandManager.getPrefix())) {
            if (keyCode == 28 || keyCode == 156) {
                Mashiro.commandManager.handleCommand(text);
                callbackInfo.cancel();
                mc.ingameGUI.getChatGUI().addToSentMessages(text);
                Minecraft.getMinecraft().displayGuiScreen(null);
            }else{
                Mashiro.commandManager.autoComplete(text);
            }
        }
    }

    /**
     * bypass click command auth like kjy.pub
      */
    @Inject(method = "setText", at = @At("HEAD"), cancellable = true)
    private void setText(String newChatText, boolean shouldOverwrite, CallbackInfo callbackInfo) {
        if(shouldOverwrite&&newChatText.startsWith(Mashiro.commandManager.getPrefix())){
            this.inputField.setText(Mashiro.commandManager.getPrefix()+"say "+newChatText);
            callbackInfo.cancel();
        }
    }

    /**
     * Adds client command auto completion and cancels sending an auto completion request packet
     * to the server if the message contains a client command.
     *
     * @author NurMarvin
     */
    @Inject(method = "sendAutocompleteRequest", at = @At("HEAD"), cancellable = true)
    private void handleClientCommandCompletion(String full, final String ignored, CallbackInfo callbackInfo) {
        if (Mashiro.commandManager.autoComplete(full)) {
            waitingOnAutocomplete = true;

            String[] latestAutoComplete = Mashiro.commandManager.getLatestAutoComplete();

            if (full.toLowerCase().endsWith(latestAutoComplete[latestAutoComplete.length - 1].toLowerCase()))
                return;

            this.onAutocompleteResponse(latestAutoComplete);

            callbackInfo.cancel();
        }
    }

    @Inject(method = "onAutocompleteResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;autocompletePlayerNames(F)V", shift = At.Shift.BEFORE), cancellable = true)
    private void onAutocompleteResponse(String[] autoCompleteResponse, CallbackInfo callbackInfo) {
        if (Mashiro.commandManager.getLatestAutoComplete().length != 0) callbackInfo.cancel();
    }

    // TODO: 这个有bug
    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        if (Mashiro.commandManager.getLatestAutoComplete().length > 0 && !inputField.getText().isEmpty() && inputField.getText().startsWith(Mashiro.commandManager.getPrefix())) {
            String[] latestAutoComplete = Mashiro.commandManager.getLatestAutoComplete();
            String[] textArray = inputField.getText().split(" ");
            String trimmedString = latestAutoComplete[0].replaceFirst("(?i)" + textArray[textArray.length - 1], "");

            mc.fontRendererObj.drawStringWithShadow(trimmedString, inputField.xPosition + mc.fontRendererObj.getStringWidth(inputField.getText()), inputField.yPosition, new Color(165, 165, 165).getRGB());
        }
    }
}
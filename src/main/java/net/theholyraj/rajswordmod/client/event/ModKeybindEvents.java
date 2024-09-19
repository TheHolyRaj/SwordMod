package net.theholyraj.rajswordmod.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.network.packet.UpgradeSwordC2SPacket;
import net.theholyraj.rajswordmod.world.keybinding.ModKeyBindings;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SwordMod.MODID, value = Dist.CLIENT)
public class ModKeybindEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event){
        if (ModKeyBindings.upgradeSword.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ModMessages.sendToServer(new UpgradeSwordC2SPacket());
            }
        }
    }
}

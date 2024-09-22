package net.theholyraj.rajswordmod.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.network.ModMessages;
import net.theholyraj.rajswordmod.network.packet.UpgradeSwordC2SPacket;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SwordMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ModKeyBindings {
    public static final ModKeyBindings INSTANCE = new ModKeyBindings();

    private ModKeyBindings(){}

    private static final String CATEGORY = "key.categories.rajswordmod";
    private static final String KEY_UPGRADE_SWORD = "key.rajswordmod.upgrade_sword";

    public final KeyMapping awakenSwordKey = new KeyMapping(KEY_UPGRADE_SWORD,
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_R,-1),
            CATEGORY);

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event){
        if (ModKeyBindings.INSTANCE.awakenSwordKey.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ModMessages.sendToServer(new UpgradeSwordC2SPacket());
            }
        }
    }
}

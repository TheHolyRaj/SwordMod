package net.theholyraj.rajswordmod.world.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String CATEGORY = "key.categories.rajswordmod"; // Custom keybinding category
    public static final String KEY_UPGRADE_SWORD = "key.rajswordmod.upgrade_sword";

    public static KeyMapping upgradeSword; // The key mapping for our hotkey

    public static void onKeyRegister(){
        upgradeSword = new KeyMapping(
                KEY_UPGRADE_SWORD,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        );
        MinecraftForge.EVENT_BUS.addListener(ModKeyBindings::onRegisterKeyMappings);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(upgradeSword);
    }

}

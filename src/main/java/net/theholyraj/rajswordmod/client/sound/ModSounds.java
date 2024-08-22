package net.theholyraj.rajswordmod.client.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theholyraj.rajswordmod.SwordMod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SwordMod.MODID);

    public static final RegistryObject<SoundEvent> SWITCH = registerSoundEvents("switch");

    public static final RegistryObject<SoundEvent> PROJECTILE_SLASH = registerSoundEvents("projectile_slash");

    public static final RegistryObject<SoundEvent> BLOCK_DAMAGE = registerSoundEvents("block_damage");

    public static final RegistryObject<SoundEvent> CHARGE_KNOCKBACK = registerSoundEvents("charge_knockback");




    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(SwordMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

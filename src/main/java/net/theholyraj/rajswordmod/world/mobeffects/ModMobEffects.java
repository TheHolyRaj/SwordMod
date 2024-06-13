package net.theholyraj.rajswordmod.world.mobeffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.item.custom.DeflectSwordItem;
import net.theholyraj.rajswordmod.world.mobeffects.custom.HolyFireEffect;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SwordMod.MODID);

    public static final RegistryObject<HolyFireEffect>HOLY_FIRE = EFFECTS.register("holy_fire",
            ()-> new HolyFireEffect(MobEffectCategory.HARMFUL,1));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}

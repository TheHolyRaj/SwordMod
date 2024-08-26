package net.theholyraj.rajswordmod.world.entity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.event.ModEventBusEvents;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;

@Mod.EventBusSubscriber(modid = SwordMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributesEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.CLONE_ENTITY.get(), CloneEntity.createAttributes().build());
    }
}

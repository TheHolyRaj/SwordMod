package net.theholyraj.rajswordmod.world.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.entity.DashProjectileModel;
import net.theholyraj.rajswordmod.client.entity.ModModelLayers;

@Mod.EventBusSubscriber(modid = SwordMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.DASH_PROJECTILE_LAYER, DashProjectileModel::createBodyLayer);
    }
}

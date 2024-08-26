package net.theholyraj.rajswordmod.client.event;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.client.entity.CloneEntityModel;
import net.theholyraj.rajswordmod.client.entity.DashProjectileModel;
import net.theholyraj.rajswordmod.client.entity.GaiaProjectileModel;
import net.theholyraj.rajswordmod.client.entity.ModModelLayers;

@Mod.EventBusSubscriber(modid = SwordMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.DASH_PROJECTILE_LAYER, DashProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.GAIA_PROJECTILE_LAYER, GaiaProjectileModel::createBodyLayer);

        event.registerLayerDefinition(ModModelLayers.CLONE_ENTITY_LAYER, CloneEntityModel::createBodyLayer);

    }
}

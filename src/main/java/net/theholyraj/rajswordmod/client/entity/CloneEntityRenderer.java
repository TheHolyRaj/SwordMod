package net.theholyraj.rajswordmod.client.entity;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;

import java.util.UUID;

public class CloneEntityRenderer extends MobRenderer<CloneEntity, CloneEntityModel<CloneEntity>> {

    public CloneEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CloneEntityModel<>(pContext.bakeLayer(ModModelLayers.CLONE_ENTITY_LAYER),false), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(CloneEntity pEntity) {
        // Assuming CloneEntity has a method to get the UUID of the player it is cloning
        UUID playerUUID = pEntity.getPlayerUUID();
        if (playerUUID != null) {
            return getPlayerSkin(playerUUID);
        }
        return new ResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
    }

    private ResourceLocation getPlayerSkin(UUID playerUUID) {
        return DefaultPlayerSkin.getDefaultSkin(playerUUID);
    }
}

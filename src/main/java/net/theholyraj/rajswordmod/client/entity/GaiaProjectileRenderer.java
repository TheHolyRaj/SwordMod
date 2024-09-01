package net.theholyraj.rajswordmod.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.entity.custom.DashProjectileEntity;
import net.theholyraj.rajswordmod.world.entity.custom.GaiaProjectileEntity;

public class GaiaProjectileRenderer extends EntityRenderer<GaiaProjectileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(SwordMod.MODID, "textures/entity/gaia_projectile.png");
    public GaiaProjectileModel model;

    public GaiaProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new GaiaProjectileModel(pContext.bakeLayer(ModModelLayers.GAIA_PROJECTILE_LAYER));
    }

    @Override
    public void render(GaiaProjectileEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPackedLight = 0xF000F0; // Ensure full brightness
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(pEntity)));

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(270.0F));

        // Scale and translate as needed
        pPoseStack.scale(1.0f, 1.0f, 1.0f);
        pPoseStack.translate(0.0, -1.4, 0.0);

        // Render the model
        model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }



    @Override
    public ResourceLocation getTextureLocation(GaiaProjectileEntity pEntity) {
        return TEXTURE;
    }
}

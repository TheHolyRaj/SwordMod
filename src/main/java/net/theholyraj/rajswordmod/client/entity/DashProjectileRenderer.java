package net.theholyraj.rajswordmod.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.theholyraj.rajswordmod.SwordMod;
import net.theholyraj.rajswordmod.world.entity.custom.DashProjectileEntity;

public class DashProjectileRenderer extends EntityRenderer<DashProjectileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(SwordMod.MODID, "textures/entity/dash_projectile.png");
    public DashProjectileModel model;

    public DashProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new DashProjectileModel(pContext.bakeLayer(ModModelLayers.DASH_PROJECTILE_LAYER));
    }

    @Override
    public void render(DashProjectileEntity entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(entity)), false, false);
        pPoseStack.scale(2,2,2);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        pPoseStack.popPose();
        super.render(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }



    @Override
    public ResourceLocation getTextureLocation(DashProjectileEntity pEntity) {
        return TEXTURE;
    }
}

package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.theholyraj.rajswordmod.world.entity.custom.GaiaProjectileEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaBladeItem extends SwordItem {
    public GaiaBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Player player = (Player) pAttacker;
        Level level = player.level();
        if (player.getAttackStrengthScale(0.0F) == 1.0F) {
            if (!level.isClientSide()){
                GaiaProjectileEntity projectile = new GaiaProjectileEntity(level,player,player.getItemInHand(InteractionHand.MAIN_HAND));
                projectile.setPos(projectile.getX(), projectile.getY() - 1.0, projectile.getZ());
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1  * 3.0F, 0.0F);
                level.addFreshEntity(projectile);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.METAL_BREAK, SoundSource.NEUTRAL,
                        1F, 1.5F);
                pStack.setDamageValue(5);
            }
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        Player player = (Player) entity;
        Level level = player.level();
        if (player.getAttackStrengthScale(0.0F) == 1.0F) {
            if (!level.isClientSide()){
                GaiaProjectileEntity projectile = new GaiaProjectileEntity(level,player,player.getItemInHand(InteractionHand.MAIN_HAND));
                projectile.setPos(projectile.getX(), projectile.getY() - 1.0, projectile.getZ());
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1  * 3.0F, 0.0F);
                level.addFreshEntity(projectile);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.METAL_BREAK, SoundSource.NEUTRAL,
                        1F, 1.5F);
                stack.setDamageValue(5);
            }
        }

        return super.onEntitySwing(stack, entity);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("rajswordmod.hovertext.gaia_sword"));
    }
}

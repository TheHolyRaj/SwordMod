package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.theholyraj.rajswordmod.world.explosion.HolyExplosion;

public class HolySwordItem extends SwordItem {
    public HolySwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        HolyExplosion explosion = new HolyExplosion(pLevel,pPlayer,pPlayer.getBlockX(),pPlayer.getBlockY(),pPlayer.getBlockZ(),2f);
        explosion.explode();
        explosion.finalizeExplosion(true);
        return InteractionResultHolder.success(pPlayer.getMainHandItem());
    }
}

package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.theholyraj.rajswordmod.world.entity.ModEntities;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ShadowCloneSwordItem extends SwordItem {
    public ShadowCloneSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            CloneEntity entity = new CloneEntity(ModEntities.CLONE_ENTITY.get(),pLevel);
            entity.setPlayerUUID(pPlayer.getUUID());
            Vec3 pos = pPlayer.position();
            entity.moveTo(pos.x, pos.y, pos.z, pPlayer.getYRot(), pPlayer.getXRot());

            pLevel.addFreshEntity(entity);
            System.out.println("yo");
            System.out.println(entity.getPlayerUUID());
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

}

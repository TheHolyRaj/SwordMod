package net.theholyraj.rajswordmod.world.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.theholyraj.rajswordmod.world.config.ModCommonConfigs;
import net.theholyraj.rajswordmod.world.entity.ModEntities;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.PropertyPermission;
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
            tauntEnemiesToClone(pPlayer,entity);
            pPlayer.getCooldowns().addCooldown(this, ModCommonConfigs.DARK_DECEIVER_COOLDOWN.get());
            pPlayer.getItemInHand(pUsedHand).setDamageValue(20);
            pPlayer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, ModCommonConfigs.SHADOW_CLONE_LIFESPAN.get(),1));
        }
        spawnSmokeParticles(pLevel,pPlayer);
        pLevel.playSound(pPlayer,pPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6f,0.6f);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    private void tauntEnemiesToClone(Player player, CloneEntity clone){
        Vec3 center = new Vec3(player.blockPosition().getX(),player.blockPosition().getY(),player.blockPosition().getZ());
        List<Mob> mobs = player.level().getEntitiesOfClass(Mob.class, new AABB(center,center).inflate(50), e-> true).stream()
                .sorted(Comparator.comparingDouble(ent -> ent.distanceToSqr(center))).toList();

        for (Mob mob: mobs){
            if (mob.getTarget() == player){
                mob.setTarget(clone);
            }
        }
    }

    private void spawnSmokeParticles(Level level, Player player) {
        double radius = 1.3; // Radius within which to spawn particles
        int particleCount = 20; // Number of smoke particles

        for (int i = 0; i < particleCount; i++) {
            // Generate random offsets within the radius
            double xOffset = (level.random.nextDouble() - 0.5) * 2 * radius;
            double zOffset = (level.random.nextDouble() - 0.5) * 2 * radius;
            double yOffset = (level.random.nextDouble() - 0.5) * 2 * 0.5; // Slight vertical variation
            double x = player.getX() + xOffset;
            double y = player.getY() + 0.5 + yOffset; // Slightly above the player's feet
            double z = player.getZ() + zOffset;

            // Add the smoke particle
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0);
        }
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("rajswordmod.hovertext.shadow_clone_sword"));
    }
}
